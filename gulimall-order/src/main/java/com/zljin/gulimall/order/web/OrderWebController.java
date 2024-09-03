package com.zljin.gulimall.order.web;

import com.zljin.gulimall.order.service.OrderService;
import com.zljin.gulimall.order.vo.OrderConfirmVo;
import com.zljin.gulimall.order.vo.OrderSubmitVo;
import com.zljin.gulimall.order.vo.SubmitOrderResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {

    @Resource
    OrderService orderService;

    @GetMapping("/{page}.html")
    public String listPage(@PathVariable("page") String page){
        return page;
    }

    /**
     * 去结算确认页
     * @param model
     * @param request
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
//        OrderConfirmVo confirmVo =  orderService.confirmOrder();
        OrderConfirmVo confirmVo =  new OrderConfirmVo();
        model.addAttribute("orderConfirmData",confirmVo);
        //展示订单确认的数据
        return "confirm";
    }


    /**
     * 下单功能
     * @param vo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes redirectAttributes){
        return  "pay";
    }
}
