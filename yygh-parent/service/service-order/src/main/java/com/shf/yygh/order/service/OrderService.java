package com.shf.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shf.yygh.model.order.OrderInfo;
import com.shf.yygh.vo.order.OrderQueryVo;

import java.util.Map;

public interface OrderService extends IService<OrderInfo> {
    /**
     * 创建订单
     * @param scheduleId
     * @param patientId
     * @return
     */
    Long saveOrder(String scheduleId, Long patientId);

    /**
     * 根据订单id查询订单详情
     * @param orderId
     * @return
     */
    OrderInfo getOrder(String orderId);

    /**
     * 订单列表（条件查询带分页）
     * @param pageParam
     * @param orderQueryVo
     * @return
     */
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);


    /**
     * 获取订单
     * @param id
     * @return
     */
    Map<String, Object> show(Long id);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    Boolean cancelOrder(Long orderId);
}
