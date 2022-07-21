package com.shf.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shf.yygh.model.hosp.Hospital;
import com.shf.yygh.model.hosp.HospitalSet;
import com.shf.yygh.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {
    /**
     * //2 根据传递过来医院编码，查询数据库，查询签名
     * @param hoscode
     * @return
     */
    String getSignKey(String hoscode);

    /**
     * //获取医院签名信息
     * @param hoscode
     * @return
     */
    SignInfoVo getSignInfoVo(String hoscode);
}
