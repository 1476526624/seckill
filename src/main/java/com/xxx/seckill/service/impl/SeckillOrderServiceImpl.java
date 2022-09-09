package com.xxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.seckill.entity.Order;
import com.xxx.seckill.entity.SeckillGoods;
import com.xxx.seckill.entity.SeckillOrder;
import com.xxx.seckill.entity.User;
import com.xxx.seckill.mapper.OrderMapper;
import com.xxx.seckill.mapper.SeckillOrderMapper;
import com.xxx.seckill.service.ISeckillGoodsService;
import com.xxx.seckill.service.ISeckillOrderService;
import com.xxx.seckill.vo.GoodsVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lazar
 * @since 2022-09-01
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Resource
    private SeckillOrderMapper seckillOrderMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    /** 功能描述: 获取秒杀情况
     * @return orderId:成功,-1:秒杀失败.0:排队中
     */
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId())
                .eq("goods_id", goodsId));
        if(null != seckillOrder){
            return seckillOrder.getOrderId();
        }else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            return -1L;
        }else {
            return 0L;
        }
    }
}
