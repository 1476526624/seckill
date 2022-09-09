package com.xxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.seckill.entity.Goods;
import com.xxx.seckill.vo.GoodsVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lazar
 * @since 2022-09-01
 */
public interface IGoodsService extends IService<Goods> {

    /*
        获取商品列表
     */
    List<GoodsVO> findGoodsVO();

    GoodsVO findGoodsVoByGoodsId(Long goodsId);
}
