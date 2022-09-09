package com.xxx.seckill.controller;

import com.xxx.seckill.service.IUserService;
import com.xxx.seckill.vo.LoginVO;
import com.xxx.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Resource
    private IUserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    //加上@Valid注解之后Login里的@NotNull等注解才会生效
    public RespBean doLogin(@Valid LoginVO loginVO, HttpServletRequest request, HttpServletResponse response){

//        log.info("{}",loginVO);
        return userService.doLogin(loginVO,request,response);
    }
}
