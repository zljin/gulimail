package com.zljin.gulimall.order.web;

import com.zljin.gulimall.common.exception.NoStockException;
import com.zljin.gulimall.order.service.OrderService;
import com.zljin.gulimall.order.vo.OrderConfirmVo;
import com.zljin.gulimall.order.vo.OrderSubmitVo;
import com.zljin.gulimall.order.vo.SubmitOrderResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
public class OrderWebController {

    @Resource
    OrderService orderService;

    @GetMapping("/{page}.html")
    public String listPage(@PathVariable("page") String page) {
        return page;
    }

    /**
     * 去结算确认页
     *
     * @param model
     * @param request
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData", confirmVo);
        //展示订单确认的数据
        return "confirm";
    }


    /**
     * 下单功能
     *
     * @param vo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes redirectAttributes) {
        try {
            SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);
            if (responseVo.getCode() == 0) {
                //下单成功来到支付选择页
                model.addAttribute("submitOrderResp", responseVo);
                return "pay";
            } else {
                String msg = "下单失败；";
                switch (responseVo.getCode()) {
                    case 1:
                        msg += "订单信息过期，请刷新再次提交";
                        break;
                    case 2:
                        msg += "订单商品价格发生变化，请确认后再次提交";
                        break;
                    case 3:
                        msg += "库存锁定失败，商品库存不足";
                        break;
                    default:
                }
                redirectAttributes.addFlashAttribute("msg", msg);
            }
        } catch (NoStockException e) {
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
        } catch (Exception e) {
            log.error("error:{}", e.toString());
        }
        return "redirect:http://order.gulimall.com/toTrade";
    }
}
