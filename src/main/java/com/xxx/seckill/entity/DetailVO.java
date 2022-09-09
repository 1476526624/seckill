package com.xxx.seckill.entity;

import com.xxx.seckill.vo.GoodsVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetailVO {
    private  User user;

    private GoodsVO goodsVO;

    private int secKillStatus;

    private  int remainSeconds;
}
