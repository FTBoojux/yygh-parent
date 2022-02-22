package com.boojux.yygh.hosp.receiver;

import com.boojux.yygh.hosp.service.ScheduleService;
import com.boojux.yygh.rabbit.config.MQConfig;
import com.boojux.yygh.rabbit.constant.MqConst;
import com.boojux.yygh.rabbit.service.RabbitService;
import com.example.yygh.model.hosp.Schedule;
import com.example.yygh.model.order.OrderMqVo;
import com.example.yygh.vo.msm.MsmVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HospitalReceiver {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private RabbitService rabbitService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER,durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
            key = {MqConst.ROUTING_ORDER}
    ))
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel){
        if(null != orderMqVo.getAvailableNumber()){
            Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
            schedule.setReservedNumber(orderMqVo.getReservedNumber());
            schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
            scheduleService.update(schedule);
        }else{
            Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
            int availableNumber = schedule.getAvailableNumber().intValue() + 1;
            schedule.setAvailableNumber(availableNumber);
            scheduleService.update(schedule);
        }

        MsmVo msmVo = orderMqVo.getMsmVo();
        if(null != msmVo){
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM,
                    MqConst.ROUTING_MSM_ITEM,msmVo);
        }
    }
}
