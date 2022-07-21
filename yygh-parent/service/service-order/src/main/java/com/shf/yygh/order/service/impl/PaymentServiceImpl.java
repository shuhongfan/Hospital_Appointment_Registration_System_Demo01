package com.shf.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shf.yygh.common.exception.YyghException;
import com.shf.yygh.common.helper.HttpRequestHelper;
import com.shf.yygh.common.result.ResultCodeEnum;
import com.shf.yygh.enums.OrderStatusEnum;
import com.shf.yygh.enums.PaymentStatusEnum;
import com.shf.yygh.enums.PaymentTypeEnum;
import com.shf.yygh.hosp.client.HospitalFeignClient;
import com.shf.yygh.model.order.OrderInfo;
import com.shf.yygh.model.order.PaymentInfo;
import com.shf.yygh.order.mapper.PaymentInfoMapper;
import com.shf.yygh.order.service.OrderService;
import com.shf.yygh.order.service.PaymentService;
import com.shf.yygh.order.service.RefundInfoService;
import com.shf.yygh.vo.order.SignInfoVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HospitalFeignClient hospitalFeignClient;



    /**
     * 保存交易记录
     *
     * @param orderInfo
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    @Override
    public void savePaymentInfo(OrderInfo orderInfo, Integer paymentType) {
//        根据订单id和支付类型，查询支付记录表是否存在相同订单
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderInfo.getId());
        queryWrapper.eq("payment_type", paymentType);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {  // 存在订单
            return;
        }

        // 保存交易记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        String subject = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd") + "|" + orderInfo.getHosname() + "|" + orderInfo.getDepname() + "|" + orderInfo.getTitle();
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(orderInfo.getAmount());

//        创建订单
        baseMapper.insert(paymentInfo);
    }

    /**
     * 支付成功更新订单状态
     *
     * @param out_trade_no
     * @param resultMap
     */
    @Override
    public void paySuccess(String out_trade_no, Map<String, String> resultMap) {
//        1.根据订单编号得到支付记录
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("out_trade_no", out_trade_no);
        wrapper.eq("payment_type", PaymentTypeEnum.WEIXIN.getStatus());
        PaymentInfo paymentInfo = baseMapper.selectOne(wrapper);

//        2.更新支付记录信息
        PaymentInfo paymentInfoUpd = new PaymentInfo();
        paymentInfoUpd.setPaymentStatus(PaymentStatusEnum.PAID.getStatus());
        paymentInfoUpd.setTradeNo(resultMap.get("transaction_id"));
        paymentInfoUpd.setCallbackTime(new Date());
        paymentInfoUpd.setCallbackContent(resultMap.toString());
        baseMapper.updateById(paymentInfoUpd);

//        3.根据订单号得到订单信息
        OrderInfo orderInfo = orderService.getById(paymentInfo.getOrderId());

//        4.更新订单信息
        orderInfo.setOrderStatus(OrderStatusEnum.PAID.getStatus());//修改订单状态
        orderService.updateById(orderInfo);

//        5.调用医院接口，更新订单支付信息
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(orderInfo.getHoscode());
        if(null == signInfoVo) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("hoscode",orderInfo.getHoscode());
        reqMap.put("hosRecordId",orderInfo.getHosRecordId());
        reqMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(reqMap, signInfoVo.getSignKey());
        reqMap.put("sign", sign);

        JSONObject result = HttpRequestHelper.sendRequest(reqMap, signInfoVo.getApiUrl() + "/order/updatePayStatus");

        if(result.getInteger("code") != 200) {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        }

    }

    /**
     * 获取支付记录信息
     * @param orderId
     * @param status
     * @return
     */
    @Override
    public PaymentInfo getPaymentInfo(Long orderId, Integer status) {
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        wrapper.eq("payment_type", status);
        return baseMapper.selectOne(wrapper);
    }

}
