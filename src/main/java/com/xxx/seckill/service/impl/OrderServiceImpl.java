package com.xxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.seckill.entity.Order;
import com.xxx.seckill.entity.SeckillGoods;
import com.xxx.seckill.entity.SeckillOrder;
import com.xxx.seckill.entity.User;
import com.xxx.seckill.exception.GlobalException;
import com.xxx.seckill.mapper.OrderMapper;
import com.xxx.seckill.service.IGoodsService;
import com.xxx.seckill.service.IOrderService;
import com.xxx.seckill.service.ISeckillGoodsService;
import com.xxx.seckill.service.ISeckillOrderService;
import com.xxx.seckill.util.MD5Utils;
import com.xxx.seckill.util.UUIDUtil;
import com.xxx.seckill.vo.GoodsVO;
import com.xxx.seckill.vo.OrderDetailVO;
import com.xxx.seckill.vo.RespBean;
import com.xxx.seckill.vo.RespBeanEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lazar
 * @since 2022-09-01
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Resource
    private ISeckillGoodsService seckillGoodsService;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    ISeckillOrderService seckillOrderService;
    @Resource
    IGoodsService goodsService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Transactional
    @Override
    public Order secKill(User user, GoodsVO goods) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        //秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
                .eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
//        seckillGoodsService.updateById(seckillGoods);
        boolean res = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count-1")
                .eq("goods_id", goods.getId()).gt("stock_count", 0));

        if(seckillGoods.getStockCount()<1) {
            //判断是否还有库存
            valueOperations.set("isStockEmpty:"+goods.getId(),"");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel((byte)1);
        order.setStatus((byte)0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);  //底层调用 (this.getBaseMapper().insert(entity));
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder);
        return order;
    }

    /*
        订单详情
     */
    @Override
    public OrderDetailVO detail(Long orderId) {
        if(orderId==null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVO goods = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVO detail = new OrderDetailVO();
        detail.setOrder(order);
        detail.setGoodsVo(goods);
        return detail;
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Utils.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+":"+goodsId,str,60, TimeUnit.SECONDS);
        return str;
    }

    /*
        校验秒杀地址
     */
    @Override
    public boolean checkPath(User user, Long goodsId,String path) {
        if(user==null || goodsId<0 || StringUtils.isEmpty(path)){
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    /*
        校验验证码
     */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if(user==null || StringUtils.isEmpty(captcha) || goodsId<0){
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}
