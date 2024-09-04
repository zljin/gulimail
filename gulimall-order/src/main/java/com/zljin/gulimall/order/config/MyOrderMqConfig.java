package com.zljin.gulimall.order.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 1. 创建订单时消息会被发送至队列order.delay.queue，
 * 2. 经过TTL的时间后消息会变成死信以order.release.order的路由键经交换机转发至队列order.release.order.queue，
 * 3. 再通过监听该队列的消息来实现过期订单的处理
 *      3.1 如果该订单已支付，则无需处理
 *      3.2 否则说明该订单已过期，修改该订单的状态并通过路由键order.release.other发送消息至队列stock.release.stock.queue进行库存解锁
 */
@Configuration
public class MyOrderMqConfig {

    @Bean
    public Queue orderDelayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        //出现dead letter之后将dead letter重新发送到指定exchange
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        //出现dead letter之后将dead letter重新按照指定的routing-key发送
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        //控制消息的生存时间 1分钟
        arguments.put("x-message-ttl", 60000);
        return new Queue("order.delay.queue", true, false, false, arguments);
    }

    @Bean
    public Queue orderReleaseOrderQueue() {
        return new Queue("order.release.order.queue", true, false, false);
    }

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }


    @Bean
    public Binding orderReleaseOrderBingding() {
        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",
                null);
    }

    @Bean
    public Binding orderCreateOrderBingding() {
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",
                null);
    }

    @Bean
    public Binding orderReleaseOrderBinding() {
        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.other.#",
                null);
    }
}
