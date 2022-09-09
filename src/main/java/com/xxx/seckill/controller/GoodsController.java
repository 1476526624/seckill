package com.xxx.seckill.controller;

import com.xxx.seckill.entity.DetailVO;
import com.xxx.seckill.entity.User;
import com.xxx.seckill.service.IGoodsService;
import com.xxx.seckill.service.IUserService;
import com.xxx.seckill.vo.GoodsVO;
import com.xxx.seckill.vo.RespBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    IUserService userService;

    @Resource
    IGoodsService goodsService;

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Resource
    ThymeleafViewResolver thymeleafViewResolver;
    /*
        跳转到商品列表页面
        Windows优化前QPS : 1332
        Linux优化前QPS : 207
     */
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user,HttpServletRequest request, HttpServletResponse response){
        //Redis中获取页面,如果不为空,直接返回页面
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVO());

        //如果为空,手动渲染,存入Redis并返回
        WebContext webContext = new WebContext(request,response
                ,request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine()
                .process("goodsList",webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
    }
//    @RequestMapping("/toList")
////    public String toList(/*HttpSession session*/HttpServletRequest request
////            ,HttpServletResponse response , Model model, @CookieValue("userTicket") String ticket){
//    public String toList(Model model, User user){
////        if(StringUtils.isEmpty(ticket)){
////            return "login";
////        }
//////        User user = (User)session.getAttribute(ticket);
////        User user = userService.getUserByCookie(ticket,request,response);
////        if(user==null){
////            return "login";
////        }
//        model.addAttribute("user",user);
//        model.addAttribute("goodsList",goodsService.findGoodsVO());
//        return "goodsList";
//    }

    /*
        跳转商品详情页
     */
    @RequestMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(Model model,User user,@PathVariable Long goodsId,HttpServletRequest request, HttpServletResponse response){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //Redis中获取页面,如果不为空,直接返回页面
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        GoodsVO goodsVO = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVO.getStartDate();
        Date endDate = goodsVO.getEndDate();
        Date now = new Date();
        //秒杀应该前端写,后端传过去还要是时间
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds;
        //秒杀还未开始
        if(now.before(startDate)){
            remainSeconds = (int)((startDate.getTime()- now.getTime())/1000);
        }else if(now.after(endDate)){
            //秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        }else{
            //秒杀中
            secKillStatus = 1;
            remainSeconds = 0;
        }


        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("goods",goodsService.findGoodsVoByGoodsId(goodsId));
        WebContext webContext = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine()
                .process("goodsDetail",webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail2(Model model, User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response){

        GoodsVO goodsVO = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVO.getStartDate();
        Date endDate = goodsVO.getEndDate();
        Date now = new Date();
        //秒杀应该前端写,后端传过去还要是时间?
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds;
        //秒杀还未开始
        if(now.before(startDate)){
            remainSeconds = (int)((startDate.getTime()- now.getTime())/1000);
        }else if(now.after(endDate)){
            //秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        }else{
            //秒杀中
            secKillStatus = 1;
            remainSeconds = 0;
        }
        DetailVO detailVO = new DetailVO();
        detailVO.setUser(user);
        detailVO.setGoodsVO(goodsVO);
        detailVO.setSecKillStatus(secKillStatus);
        detailVO.setRemainSeconds(remainSeconds);

        System.out.println(detailVO);
        System.out.println(detailVO.getGoodsVO().getId());
        return RespBean.success(detailVO);
    }

}
