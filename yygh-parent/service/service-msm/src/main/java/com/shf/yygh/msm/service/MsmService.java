package com.shf.yygh.msm.service;

import com.shf.yygh.vo.msm.MsmVo;

public interface MsmService {
    /**
     * 发送验证码
     * @param phone
     * @param code
     * @return
     */
    boolean send(String phone, String code);

    //    mq发送短信
    boolean send(MsmVo msmVo);
}
