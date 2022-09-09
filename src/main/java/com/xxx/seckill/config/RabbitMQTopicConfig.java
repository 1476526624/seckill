package com.xxx.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    工作中大部分用主题模式
 */
@Configuration
public class RabbitMQTopicConfig {

    public static final String QUEUE = "seckillQueue";
    public static final String EXCHANGE = "seckillExchange";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding  binding(){
        return BindingBuilder.bind(queue()).to(topicExchange())
                .with("seckill.#");
    }
//    public static final String EXCHANGE = "topicExchange";
//    public static final String Queue01 = "queue_topic01";
//    public static final String Queue02 = "queue_topic02";
//    public static final String RoutingKEY01 = "#.queue.#";
//    public static final String RoutingKEY02 = "*.queue.*";
//
//    @Bean
//    public Queue topicQueue01(){
//        return new Queue(Queue01);
//    }
//
//    @Bean
//    public Queue topicQueue02(){
//        return new Queue(Queue02);
//    }
//
//    @Bean
//    public TopicExchange topicExchange(){
//        return new TopicExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binging01(){
//        return BindingBuilder.bind(topicQueue01()).to(topicExchange()).with(RoutingKEY01);
//    }
//
//    @Bean
//    public Binding binging02(){
//        return BindingBuilder.bind(topicQueue02()).to(topicExchange()).with(RoutingKEY02);
//    }
}
