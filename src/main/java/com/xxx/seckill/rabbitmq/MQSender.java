package com.xxx.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MQSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /*
        发送秒杀信息
     */
    public void sendSeckillMessage(String message){
        log.info("发送消息: "+message);
        rabbitTemplate.convertAndSend("seckillExchange","seckill.message",message);
    }
//    public void send(Object msg){
//        log.info("发送消息: "+msg);
//        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
//    }
//
//    public void directSend01(Object msg){
//        log.info("发送red消息: "+msg);
//        rabbitTemplate.convertAndSend("directExchange","queue.red",msg);
//    }
//
//    public void directSend02(Object msg){
//        log.info("发送green消息: "+msg);
//        rabbitTemplate.convertAndSend("directExchange","queue.green",msg);
//    }
//
//    public void topicSend01(Object msg){
//        log.info("发送消息(QUEUE01接收): "+msg);
//        rabbitTemplate.convertAndSend("topicExchange","queue.green",msg);
//    }
//
//    public void topicSend02(Object msg){
//        log.info("发送消息(QUEUE02和QUEUE01接收): "+msg);
//        rabbitTemplate.convertAndSend("topicExchange","gg.queue.green",msg);
//    }
//
//    public void topicSend01(String msg){
//        log.info("发送消息(被两个queue接收): "+msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("color","red");
//        properties.setHeader("speed","fast");
//        Message message = new Message(msg.getBytes(),properties);
//        rabbitTemplate.convertAndSend("headersExchange","",message);
//    }
//
//    public void topicSend02(String msg){
//        log.info("发送消息(被Queue01接收): "+msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("color","red");
//        properties.setHeader("speed","normal");
//        Message message = new Message(msg.getBytes(),properties);
//        rabbitTemplate.convertAndSend("headersExchange","",message);
//    }
}
