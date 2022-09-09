package com.xxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.seckill.entity.Order;
import com.xxx.seckill.entity.SeckillGoods;
import com.xxx.seckill.entity.SeckillOrder;
import com.xxx.seckill.entity.User;
import com.xxx.seckill.mapper.OrderMapper;
import com.xxx.seckill.mapper.SeckillGoodsMapper;
import com.xxx.seckill.service.ISeckillGoodsService;
import com.xxx.seckill.service.ISeckillOrderService;
import com.xxx.seckill.vo.GoodsVO;
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
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements ISeckillGoodsService {


}
