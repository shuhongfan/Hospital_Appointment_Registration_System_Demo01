package com.shf.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shf.yygh.model.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
}
