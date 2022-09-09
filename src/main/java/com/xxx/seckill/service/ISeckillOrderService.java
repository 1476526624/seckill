package com.xxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.seckill.entity.Order;
import com.xxx.seckill.entity.SeckillOrder;
import com.xxx.seckill.entity.User;
import com.xxx.seckill.vo.GoodsVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lazar
 * @since 2022-09-01
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /**
     * 功能描述: 获取秒杀情况
     * @return orderId:成功,-1:秒杀失败.0:排队中
     */


    Long getResult(User user, Long goodsId);
}
