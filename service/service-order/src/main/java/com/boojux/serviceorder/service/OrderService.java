package com.boojux.serviceorder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yygh.model.order.OrderInfo;
import com.example.yygh.vo.order.OrderCountQueryVo;
import com.example.yygh.vo.order.OrderQueryVo;

import java.util.Map;

public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId,Long patientId);

    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    OrderInfo getOrder(String orderId);

    Map<String,Object> show(Long id);

    Boolean cancelOrder(Long orderId);

    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);
}
