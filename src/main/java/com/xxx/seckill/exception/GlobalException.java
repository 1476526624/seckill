package com.xxx.seckill.exception;

import com.xxx.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/*
当我们给一个继承了父类的子类上使用@Data @ToString @EqualsAndHashCode 注解时，IDE 会警告

其意思是，该注解在实现 ToString EqualsAndHashCode 方法时，不会考虑父类的属性，通过反编译的源码也是可以看到他是没有对父类的字段进行比较的
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private RespBeanEnum respBeanEnum;
}
