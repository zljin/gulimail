package com.zljin.gulimall.search.controller;

import com.zljin.gulimall.common.exception.BizCodeEnum;
import com.zljin.gulimall.common.to.es.SkuEsModel;
import com.zljin.gulimall.common.utils.R;
import com.zljin.gulimall.search.service.SkuEsModelService;
import com.zljin.gulimall.search.vo.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class SearchController {

    @Resource
    SkuEsModelService skuEsModelService;

    @PostMapping("/search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean b = skuEsModelService.productStatusUp(skuEsModels);
        if (b) {
            return R.ok();
        } else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }

    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {
        return "list";
    }
}
