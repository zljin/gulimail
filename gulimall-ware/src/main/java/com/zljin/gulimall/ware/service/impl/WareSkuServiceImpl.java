package com.zljin.gulimall.ware.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zljin.gulimall.common.exception.NoStockException;
import com.zljin.gulimall.common.to.SkuHasStockVo;
import com.zljin.gulimall.common.to.mq.OrderTo;
import com.zljin.gulimall.common.to.mq.StockDetailTo;
import com.zljin.gulimall.common.to.mq.StockLockedTo;
import com.zljin.gulimall.common.utils.PageUtils;
import com.zljin.gulimall.common.utils.Query;
import com.zljin.gulimall.common.utils.R;
import com.zljin.gulimall.ware.dao.WareSkuDao;
import com.zljin.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.zljin.gulimall.ware.entity.WareOrderTaskEntity;
import com.zljin.gulimall.ware.entity.WareSkuEntity;
import com.zljin.gulimall.ware.feign.OrderFeignService;
import com.zljin.gulimall.ware.feign.ProductFeignService;
import com.zljin.gulimall.ware.service.WareOrderTaskDetailService;
import com.zljin.gulimall.ware.service.WareOrderTaskService;
import com.zljin.gulimall.ware.service.WareSkuService;
import com.zljin.gulimall.ware.vo.OrderVo;
import com.zljin.gulimall.ware.vo.SkuWareHasStock;
import com.zljin.gulimall.ware.vo.WareSkuLockVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {


    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    WareOrderTaskService orderTaskService;

    @Autowired
    WareOrderTaskDetailService orderTaskDetailService;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    OrderFeignService orderFeignService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 查询某一商品在某一个仓库的数据情况
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         * skuId: 1
         * wareId: 2
         */
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (StrUtil.isNotEmpty(skuId)) {
            queryWrapper.eq("sku_id", skuId);
        }

        String wareId = (String) params.get("wareId");
        if (StrUtil.isNotEmpty(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }


        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1、判断如果还没有这个库存记录新增
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (entities == null || entities.size() == 0) {
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setStock(skuNum);
            skuEntity.setWareId(wareId);
            skuEntity.setStockLocked(0);
            //TODO 远程查询sku的名字，如果失败，整个事务无需回滚
            //1、自己catch异常
            //TODO 还可以用什么办法让异常出现以后不回滚？高级
            try {
                R info = productFeignService.info(skuId);
                Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");
                if (MapUtil.getInt(info, "code") == 0) {
                    skuEntity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {

            }
            wareSkuDao.insert(skuEntity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds) {
        return skuIds.stream().map(skuId -> {
            SkuHasStockVo vo = new SkuHasStockVo();
            //查询当前sku的总库存量
            Long count = baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count == null ? false : count > 0);
            return vo;
        }).collect(Collectors.toList());
    }


    /**
     * 库存解锁的场景
     * 1. 下订单成功，订单过期没有支付被系统自动取消、被用户手动取消。都要解锁库存
     * 2. 下订单成功，库锁定存成功，接下来的业务调用失败，导致订单回滚。
     * @param vo
     * @return
     */
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {

        //step1: 保存库存工作单详情历史记录用来追溯--->即用来分布式事务回滚
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
        orderTaskService.save(taskEntity);


        //step2: 查询拥有skuId的所有仓库的wareId
        List<SkuWareHasStock> skuWareHasStocks = vo.getLocks().stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            //查询这个商品在哪里有库存
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            stock.setWareId(wareIds);
            return stock;
        }).collect(Collectors.toList());

        //step3: 开始锁定库存
        for (SkuWareHasStock hasStock : skuWareHasStocks) {
            Boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if (CollectionUtil.isEmpty(wareIds)) {//所有仓库都没货
                throw new NoStockException(skuId);
            }
            for (Long wareId : wareIds) {
                Long count = wareSkuDao.lockSkuStock(skuId, wareId, hasStock.getNum());
                if (count == 1) {//成功就返回1,锁定成功,否则重试下一个仓库
                    skuStocked = true;
                    //step4: 保存库存工作单详情细节表历史记录用来追溯--->即用来分布式事务回滚
                    WareOrderTaskDetailEntity entity = new WareOrderTaskDetailEntity(null, skuId, "", hasStock.getNum(), taskEntity.getId(), wareId, 1);
                    orderTaskDetailService.save(entity);

                    /**
                     * 2. 下订单成功，库锁定存成功，接下来的业务调用失败，导致订单回滚。
                     */
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setId(taskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtil.copyProperties(entity, stockDetailTo);
                    lockedTo.setDetail(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", lockedTo);

                    break;//任意找个仓库就行
                }
            }

            if (!skuStocked) {
                //当前商品所有仓库都没有锁住
                throw new NoStockException(skuId);
            }
        }
        return true;
    }


    /**
     * 1. 下订单成功，订单过期没有支付被系统自动取消、被用户手动取消。都要解锁库存
     * @param orderTo
     */
    @Transactional
    @Override
    public void unlockStock(OrderTo orderTo) {
        String orderSn = orderTo.getOrderSn();
        //查一下最新库存的状态，防止重复解锁库存
        WareOrderTaskEntity task = orderTaskService.getOrderTaskByOrderSn(orderSn);
        Long id = task.getId();
        //按照工作单找到所有 没有解锁的库存，进行解锁
        List<WareOrderTaskDetailEntity> entities = orderTaskDetailService.list(
                new QueryWrapper<WareOrderTaskDetailEntity>()
                        .eq("task_id", id)
                        .eq("lock_status", 1));
        for (WareOrderTaskDetailEntity entity : entities) {
            unLockStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum(),entity.getId());
        }
    }

    /**
     * 2. 下订单成功，库锁定存成功，接下来的业务调用失败，导致订单回滚
     * @param to
     */
    @Transactional
    @Override
    public void unlockStock(StockLockedTo to) {
        StockDetailTo detail = to.getDetail();
        Long detailId = detail.getId();
        WareOrderTaskDetailEntity byId = orderTaskDetailService.getById(detailId);
        if(byId == null){
            return;
        }
        Long id = to.getId();
        WareOrderTaskEntity taskEntity = orderTaskService.getById(id);
        String orderSn = taskEntity.getOrderSn();//根据订单号查询订单的状态
        R r = orderFeignService.getOrderStatus(orderSn);
        if(r.getCode() != 0){
            //消息拒绝以后重新放到队列里面，让别人继续消费解锁。
            throw new RuntimeException("远程服务失败");
        }
        OrderVo data = r.getData(new TypeReference<OrderVo>() {});
        //订单不存在 or 订单已经被取消了 and WareOrderTaskDetailEntity 是锁定的状态才可解锁库存
        if ((data == null || data.getStatus() == 4) && byId.getLockStatus() == 1) {
            unLockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId);
        }
    }

    private void unLockStock(Long skuId, Long wareId, Integer num, Long taskDetailId) {
        //库存解锁
        wareSkuDao.unlockStock(skuId, wareId, num);
        //更新库存工作单的状态
        WareOrderTaskDetailEntity entity = new WareOrderTaskDetailEntity();
        entity.setId(taskDetailId);
        entity.setLockStatus(2);//变为已解锁
        orderTaskDetailService.updateById(entity);
    }

}