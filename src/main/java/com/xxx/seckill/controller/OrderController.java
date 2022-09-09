package com.xxx.seckill.controller;

import com.xxx.seckill.entity.User;
import com.xxx.seckill.service.IOrderService;
import com.xxx.seckill.vo.OrderDetailVO;
import com.xxx.seckill.vo.RespBean;
import com.xxx.seckill.vo.RespBeanEnum;
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
 * @since 2022-09-01
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Resource
    IOrderService orderService;
    /*
        订单详情
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user,Long orderId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERR);
        }
        OrderDetailVO detail = orderService.detail(orderId);
        return RespBean.success(detail);
    }
}
