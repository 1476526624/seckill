package com.xxx.seckill.controller;

import com.xxx.seckill.entity.User;
import com.xxx.seckill.rabbitmq.MQSender;
import com.xxx.seckill.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lazar
 * @since 2022-08-30
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private MQSender mqSender;
    /*
        用户信息(测试)
     */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }

    /*
        功能描述 : 测试发送RabbitMQ消息
     */
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq(String msg){
//        mqSender.send("hello");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void mq01(String msg){
//        mqSender.send("hello");
//    }
//
//    @RequestMapping("/mq/direct01")
//    @ResponseBody
//    public void mq02(){
//        mqSender.directSend01("Hello,Red");
//    }
//
//    @RequestMapping("/mq/direct02")
//    @ResponseBody
//    public void mq03(){
//        mqSender.directSend02("Hello,Green");
//    }
//
//    @RequestMapping("/mq/topic01")
//    @ResponseBody
//    public void mq04(){
//        mqSender.topicSend01("Hello,topic01");
//    }
//
//    @RequestMapping("/mq/topic02")
//    @ResponseBody
//    public void mq05(){
//        mqSender.topicSend02("Hello,topic02");
//    }
//
//    @RequestMapping("/mq/header01")
//    @ResponseBody
//    public void mq06(){
//        mqSender.topicSend01("Hello,header01");
//    }
//
//    @RequestMapping("/mq/header02")
//    @ResponseBody
//    public void mq07(){
//        mqSender.topicSend02("Hello,header02");
//    }
}
