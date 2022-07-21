package com.shf.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shf.yygh.model.order.OrderInfo;
import com.shf.yygh.model.order.PaymentInfo;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {
    /**
     * 保存交易记录
     * @param order
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    void savePaymentInfo(OrderInfo order, Integer paymentType);

    /**
     * 支付成功更新订单状态
     *
     * @param out_trade_no
     * @param resultMap
     */
    void paySuccess(String out_trade_no, Map<String, String> resultMap);


    /**
     * 获取支付记录信息
     * @param orderId
     * @param status
     * @return
     */
    PaymentInfo getPaymentInfo(Long orderId, Integer status);
}
