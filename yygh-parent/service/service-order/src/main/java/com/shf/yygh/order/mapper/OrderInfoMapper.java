package com.shf.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shf.yygh.model.order.OrderInfo;
import com.shf.yygh.vo.order.OrderCountQueryVo;
import com.shf.yygh.vo.order.OrderCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    //    查询预约统计数据的方法
    List<OrderCountVo> selectOrderCount(@Param("vo") OrderCountQueryVo orderCountQueryVo);
}
