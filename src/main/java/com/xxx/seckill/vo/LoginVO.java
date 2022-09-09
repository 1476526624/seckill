package com.xxx.seckill.vo;


import com.xxx.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class LoginVO {
    @NotNull
//    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")
    @IsMobile
    private String mobile;
    @NotNull
    @Length(min = 32)
    private String password;
}
