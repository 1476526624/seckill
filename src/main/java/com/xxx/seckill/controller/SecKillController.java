package com.xxx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.captcha.ArithmeticCaptcha;
import com.xxx.seckill.config.AccessLimit;
import com.xxx.seckill.entity.Order;
import com.xxx.seckill.entity.SeckillMessage;
import com.xxx.seckill.entity.SeckillOrder;
import com.xxx.seckill.entity.User;
import com.xxx.seckill.exception.GlobalException;
import com.xxx.seckill.rabbitmq.MQSender;
import com.xxx.seckill.service.IGoodsService;
import com.xxx.seckill.service.IOrderService;
import com.xxx.seckill.service.ISeckillOrderService;
import com.xxx.seckill.util.JsonUtil;
import com.xxx.seckill.vo.GoodsVO;
import com.xxx.seckill.vo.RespBean;
import com.xxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.reactive.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
    Windows δΌεε QPS=792
 */
@Controller
@RequestMapping("/seckill")
@Slf4j
public class SecKillController implements InitializingBean {

    @Resource
    IGoodsService goodsService;
    @Resource
    private ISeckillOrderService seckillOrderService;
    @Resource
    private IOrderService orderService;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private MQSender mqSender;

    private final Map<Long,Boolean> emptyStockMap = new HashMap<>();

    @Resource
    private RedisScript<Long> script;

    @RequestMapping("/doSeckill2")
    public String doSecKill2(Model model, User user,Long goodsId){
        if(user==null) return "login";
        model.addAttribute("user",user);
        GoodsVO goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //ε€ζ­εΊε­
        if(goods.getStockCount()<1){
            model.addAttribute("errMsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //ε€ζ­ζ―ε¦ιε€ζ’θ΄­
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId));
        if(seckillOrder!=null){
            model.addAttribute("errMsg",RespBeanEnum.REPEAT_ERROR.getMessage());
            return "secKillFail";
        }
        Order order = orderService.secKill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";
    }

    /*
        εθ½ζθΏ°: η§ζ
        windowδΌεεQPS: 785
        ηΌε­QPS: 1356
        δΌεQPS:
     */
    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path, User user, Long goodsId){

        if(user==null) return RespBean.error(RespBeanEnum.SESSION_ERR);

        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        boolean check = orderService.checkPath(user,goodsId,path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //ε€ζ­ζ―ε¦ιε€ζ’θ΄­
        SeckillOrder seckillOrder = (SeckillOrder) valueOperations.get("order:"+user.getId()+":"+goodsId);
        if(seckillOrder!=null){
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        //ιθΏεε­ζ θ?°,εε°Redisθ?Ώι?
        if(emptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //εε­ζ§ζδ½
        //ι’εεΊε­
//        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        Long stock = redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);

        if(stock<0){
            emptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods");
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        //ζ₯ζΆε°0ζΆ,εη«―ε±η€Ίζ­£ε¨ε€ηδΈ­
        return RespBean.success(0);
    /*
        GoodsVO goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //ε€ζ­εΊε­
        if(goods.getStockCount()<1){
            model.addAttribute("errMsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //ε€ζ­ζ―ε¦ιε€ζ’θ΄­
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
//                .eq("goods_id", goodsId));
        Order seckillOrder = (Order) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            model.addAttribute("errMsg",RespBeanEnum.REPEAT_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        Order order = orderService.secKill(user,goods);

        return RespBean.success(order);
        */

    }

    /**
     * εθ½ζθΏ°: θ·εη§ζζε΅
     * @return orderId:ζε,-1:η§ζε€±θ΄₯.0:ζιδΈ­
     */



    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId){
        if (user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERR);
        }
        Long orderId = seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }

    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response){
        if(user==null || goodsId==null){ //goodsId<0?? ε―δ»₯δΈθ¦θΏδΈͺζ‘δ»Άε§\
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //θ?Ύη½?θ―·ζ±ε€΄δΈΊθΎεΊεΎηηη±»ε
        response.setContentType("image/jpg");
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        //ηζιͺθ―η ,ε°η»ζζΎε₯Redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text(),
                300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        }catch (IOException e){
            log.error("ιͺθ―η ηζε€±θ΄₯",e.getMessage());
        }
    }


    @AccessLimit(second=5,maxCount=5)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERR);
        }


        boolean check = orderService.checkCaptcha(user,goodsId,captcha);
        if(!check){
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(user,goodsId);
        return RespBean.success(str);
    }


    /*
        εε§εζΆ,ζ§θ‘ηζΉζ³
        η³»η»εε§ε,ζεεεΊε­ζ°ιε θ½½ε°redis
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> list = goodsService.findGoodsVO();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        list.forEach(goodsVO ->{
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVO.getId(),goodsVO.getStockCount());
            emptyStockMap.put(goodsVO.getId(),false);
        });

    }
}
