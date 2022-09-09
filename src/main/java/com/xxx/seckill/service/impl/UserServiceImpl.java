package com.xxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.seckill.entity.User;
import com.xxx.seckill.exception.GlobalException;
import com.xxx.seckill.mapper.UserMapper;
import com.xxx.seckill.service.IUserService;
import com.xxx.seckill.util.CookieUtil;
import com.xxx.seckill.util.MD5Utils;
import com.xxx.seckill.util.UUIDUtil;
import com.xxx.seckill.vo.LoginVO;
import com.xxx.seckill.vo.RespBean;
import com.xxx.seckill.vo.RespBeanEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lazar
 * @since 2022-08-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    UserMapper userMapper;

    @Resource
//  Raw use of xxx -> 声明没有加泛型
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public RespBean doLogin(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

//        //参数校验 使用自定@IsMobile在Controller层校验,这里就不需要校验了
//        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
//            return  RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if(!MobileValidatorUtil.isMobile(mobile)){
//            return  RespBean.error(RespBeanEnum.Mobile_ERROR);
//        }
        //获取用户
        User user = userMapper.selectById(mobile);
        if(null==user){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //判断密码是否正确
        if(!MD5Utils.fromPassToDBPass(password,user.getSlat()).equals(user.getPassword())){
//            return  RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();
//        request.getSession().setAttribute(ticket,user);
        //将用户信息存入redis
        redisTemplate.opsForValue().set("user:"+ticket,user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user =  (User)redisTemplate.opsForValue().get("user:" + userTicket);
        if(null!=user){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }

        return user;
    }

    /*
        更新密码
     */
    @Override
    public RespBean updatePassword(String userTicket, String password,
                                   HttpServletRequest request,HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if(user==null){
            throw  new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Utils.inputPassToDBPass(password,user.getSlat()));
        int res = userMapper.updateById(user);
        if(1==res){
            redisTemplate.delete("user:"+userTicket);
            return  RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
