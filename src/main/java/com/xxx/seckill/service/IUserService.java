package com.xxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.seckill.entity.User;
import com.xxx.seckill.vo.LoginVO;
import com.xxx.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lazar
 * @since 2022-08-30
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response);

    /*
        根据cookie获取用户
     */
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    /*
        更新密码
     */
    RespBean updatePassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response);
}
