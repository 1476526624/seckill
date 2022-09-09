package com.xxx.seckill.vo;

import com.xxx.seckill.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVO {
    private Order order;

    private GoodsVO goodsVo;
}
