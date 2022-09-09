package com.xxx.seckill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxx.seckill.entity.Goods;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVO extends Goods {

    private BigDecimal seckillPrice;

    private Integer stockCount;

//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

}
