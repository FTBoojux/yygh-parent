package com.boojux.servicestatistics.controller;

import com.boojux.yygh.order.client.OrderFeignClient;
import com.example.yygh.common.result.Result;
import com.example.yygh.vo.order.OrderCountQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/statistics")
public class StatisticsController {
    @Autowired
    private OrderFeignClient orderFeignClient;

    @GetMapping("getCountMap")
    public Result getCountMap(OrderCountQueryVo orderCountQueryVo){
        return Result.ok(orderFeignClient.getCountMap(orderCountQueryVo));
    }
}
