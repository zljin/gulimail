package com.zljin.gulimall.search.web;

import com.zljin.gulimall.search.service.SkuEsModelService;
import com.zljin.gulimall.search.vo.SearchParam;
import com.zljin.gulimall.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchListController {

    @Resource
    SkuEsModelService skuEsModelService;

    @GetMapping({"/","/list.html"})
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {
        param.set_queryString(request.getQueryString());
        SearchResult result = skuEsModelService.search(param);
        model.addAttribute("result",result);
        return "list";
    }
}
