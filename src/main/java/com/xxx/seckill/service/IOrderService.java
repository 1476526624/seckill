package com.xxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.seckill.entity.Order;
import com.xxx.seckill.entity.User;
import com.xxx.seckill.vo.GoodsVO;
import com.xxx.seckill.vo.OrderDetailVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lazar
 * @since 2022-09-01
 */
public interface IOrderService extends IService<Order> {

    Order secKill(User user, GoodsVO goods);

    /*
        订单详情
     */
    OrderDetailVO detail(Long orderId);

    /*
        功能描述: 获取秒杀地址
     */
    String createPath(User user, Long goodsId);

    /*
        校验秒杀地址
     */
    boolean checkPath(User user, Long goodsId,String path);

    /*
        校验验证码
     */
    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
