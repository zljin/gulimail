package com.zljin.gulimall.product.web;

import com.zljin.gulimall.product.entity.CategoryEntity;
import com.zljin.gulimall.product.service.CategoryService;
import com.zljin.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    /**
     * 渲染一级分类数据
     * @param model
     * @return
     */
    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntities =  categoryService.getLevel1Categorys();
        // 视图解析器进行拼串：
        model.addAttribute("categorys",categoryEntities);
        // classpath:/templates/ +返回值+  .html
        return "index";
    }

    /**
     * 渲染二级三级分类数据
     * @return
     */
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }
}
