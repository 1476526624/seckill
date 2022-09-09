package com.xxx.seckill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@Configuration
//@EnableWebMvc   此注解会导致 no mapping for ... 问题 不要用 --->配置类大于配置(静态资源将不会从static寻址)
// 或者将拦截器继承WebMvcConfigurationSupport
/*
Spring Webmvc。
通过查看@EnableWebMvc的源码，
可以发现该注解就是为了引入一个DelegatingWebMvcConfiguration Java 配置类。
并翻看DelegatingWebMvcConfiguration的源码
会发现该类似继承于WebMvcConfigurationSupport的类。
其实不使用@EnableWebMvc注解也是可以实现配置Webmvc，
只需要将配置类继承于WebMvcConfigurationSupport类即可
WebMvcConfigurer 接口；

在Spring Boot 1.5版本都是靠重写WebMvcConfigurerAdapter的方法来添加自定义拦截器，
消息转换器等。SpringBoot 2.0 后，该类被标记为@Deprecated（弃用）。
官方推荐直接实现WebMvcConfigurer或者直接继承WebMvcConfigurationSupport，
方式一实现WebMvcConfigurer接口（推荐），方式二继承WebMvcConfigurationSupport类，
具体实现可看这篇文章。
 */
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private UserArgumentResolver userArgumentResolver;
    @Resource
    private AccessLimitInterceptor accessLimitInterceptor;


//    @Override  配置类大于配置
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }


}
