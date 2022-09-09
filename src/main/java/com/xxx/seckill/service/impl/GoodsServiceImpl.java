package com.xxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.seckill.entity.Goods;
import com.xxx.seckill.mapper.GoodsMapper;
import com.xxx.seckill.service.IGoodsService;
import com.xxx.seckill.vo.GoodsVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lazar
 * @since 2022-09-01
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVO> findGoodsVO() {
        return goodsMapper.findGoodsVO();
    }

    @Override
    public GoodsVO findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVOByGoodsId(goodsId);
    }
}
