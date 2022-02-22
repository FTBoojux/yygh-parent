package com.boojux.serviceorder.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boojux.serviceorder.service.OrderService;
import com.example.yygh.common.helper.AuthContextHolder;
import com.example.yygh.common.result.Result;
import com.example.yygh.enums.OrderStatusEnum;
import com.example.yygh.model.order.OrderInfo;
import com.example.yygh.vo.order.OrderCountQueryVo;
import com.example.yygh.vo.order.OrderQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {
    @Autowired
    private OrderService orderService;
    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")
    public Result submitOrder(@PathVariable String scheduleId,
                              @PathVariable Long patientId ){
        return Result.ok(orderService.saveOrder(scheduleId,patientId));
    }
    @GetMapping("auth/{page}/{limit}")
    public Result list(@PathVariable Long page, @PathVariable Long limit,
        OrderQueryVo orderQueryVo, HttpServletRequest request){
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        IPage<OrderInfo> pageModel = orderService.selectPage(pageParam,orderQueryVo);
        return Result.ok(pageModel);
    }

    @GetMapping("auth/getStatusList")
    public Result getStatus(){
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @GetMapping("auth/getOrders/{orderId}")
    public Result getOrders(@PathVariable String orderId){
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return Result.ok(orderInfo);
    }

    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,@PathVariable Long limit,
                        OrderQueryVo orderQueryVo){
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        IPage<OrderInfo> pages = orderService.selectPage(pageParam,orderQueryVo);
        return Result.ok(pages);
    }

    @GetMapping("getStatusList")
    public Result getStatusList(){
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @GetMapping("show/{id}")
    public Result get(@PathVariable Long id){
        return Result.ok(orderService.show(id));
    }

    @GetMapping("auth/cancelOrder/{orderId}")
    public Result cancelOrder(@PathVariable("orderId") Long orderId){
        return Result.ok(orderService.cancelOrder(orderId));
    }

    @PostMapping("inner/getCountMap")
    public Map<String,Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo){
        return orderService.getCountMap(orderCountQueryVo);
    }
}
