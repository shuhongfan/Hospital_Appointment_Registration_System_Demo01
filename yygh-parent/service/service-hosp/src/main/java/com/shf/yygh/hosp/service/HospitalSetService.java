package com.shf.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shf.yygh.model.hosp.Hospital;
import com.shf.yygh.model.hosp.HospitalSet;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);

}
