//package com.xxx.seckill.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQDirectConfig {
//    public static final String QUEUE01 = "queue_direct01";
//    public static final String QUEUE02 = "queue_direct02";
//    public static final String EXCHANGE = "directExchange";
//    public static final String ROUTINGKEY01 = "queue.red";
//    public static final String ROUTINGKEY02 = "queue.green";
//
//    @Bean
//    public Queue queue01(){
//        return new Queue(QUEUE01);
//    }
//
//    @Bean
//    public Queue queue02(){
//        return new Queue(QUEUE02);
//    }
//
//    @Bean
//    public DirectExchange directExchange(){
//        return new DirectExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binging01(){
//        return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTINGKEY01);
//    }
//
//    @Bean
//    public Binding binging02(){
//        return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTINGKEY02);
//    }
//}
