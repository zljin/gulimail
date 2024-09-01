package com.zljin.gulimall.search.controller;

import com.zljin.gulimall.common.exception.BizCodeEnum;
import com.zljin.gulimall.common.to.es.SkuEsModel;
import com.zljin.gulimall.common.utils.R;
import com.zljin.gulimall.search.service.SkuEsModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
public class EsSaveController {

    @Resource
    SkuEsModelService skuEsModelService;

    @PostMapping("/es/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean b = skuEsModelService.productStatusUp(skuEsModels);
        if (b) {
            return R.ok();
        } else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
