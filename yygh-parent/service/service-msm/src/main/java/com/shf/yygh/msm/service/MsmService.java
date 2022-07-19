package com.shf.yygh.msm.service;

public interface MsmService {
    /**
     * 发送验证码
     * @param phone
     * @param code
     * @return
     */
    boolean send(String phone, String code);
}
