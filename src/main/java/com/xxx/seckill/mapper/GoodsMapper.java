package com.xxx.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.seckill.entity.Goods;
import com.xxx.seckill.vo.GoodsVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lazar
 * @since 2022-09-01
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /*
        获取商品列表
     */
    List<GoodsVO> findGoodsVO();

    GoodsVO findGoodsVOByGoodsId(Long GoodsId);
}
