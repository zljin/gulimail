package com.zljin.gulimall.search.controller;

import com.zljin.gulimall.common.to.es.SkuEsModel;
import com.zljin.gulimall.common.utils.R;
import com.zljin.gulimall.search.vo.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class SearchController {

    @PostMapping("/search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        return R.ok();
    }

    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {
        return "list";
    }
}
