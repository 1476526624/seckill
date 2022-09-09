package com.xxx.seckill.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxx.seckill.entity.SeckillMessage;
import com.xxx.seckill.entity.SeckillOrder;
import com.xxx.seckill.entity.User;
import com.xxx.seckill.service.IGoodsService;
import com.xxx.seckill.service.IOrderService;
import com.xxx.seckill.util.JsonUtil;
import com.xxx.seckill.vo.GoodsVO;
import com.xxx.seckill.vo.RespBean;
import com.xxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MQReceiver {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private IOrderService orderService;

    /*
        下单操作
     */
    @RabbitListener(queues = "seckillQueue")
    public void receive(String messgae){
        log.info("接受的消息: "+messgae);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(messgae, SeckillMessage.class);
        Long goodId = seckillMessage.getGoodId();
        User user = seckillMessage.getUser();
        GoodsVO goodsVO = goodsService.findGoodsVoByGoodsId(goodId);
        if(goodsVO.getStockCount()<1){
            return;
        }
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) valueOperations.get("order:"+user.getId()+":"+goodId);
        if(seckillOrder!=null){
            return;
        }
        //下单操作
        orderService.secKill(user,goodsVO);

    }
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg){
//        log.info("接受消息: "+msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg){
//        log.info("QUEUE01接受消息: "+msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg){
//        log.info("QUEUE01接受消息: "+msg);
//    }
//
//    @RabbitListener(queues = "queue_direct01")
//    public void receive03(Object msg){
//        log.info("QUEUE01接受消息: "+msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg){
//        log.info("QUEUE02接受消息: "+msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void topicReceive01(Object msg){
//        log.info("topicQUEUE01接受消息: "+msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void topicReceive02(Object msg){
//        log.info("topicQUEUE02接受消息: "+msg);
//    }
//
//    @RabbitListener(queues = "queue_header01")
//    public void headersReceive01(Message msg){
//        log.info("QUEUE01接受对象: "+msg);
//        log.info("QUEUE01接受消息: "+new String(msg.getBody()));
//    }
//
//    @RabbitListener(queues = "queue_header02")
//    public void headersReceive02(Message msg){
//        log.info("QUEUE02接受对象: "+msg);
//        log.info("QUEUE02接受消息: "+new String(msg.getBody()));
//    }
}
