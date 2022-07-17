package com.shf.yygh.hosp.service;

import com.shf.yygh.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService {
    /**
     * 上传医院接口
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);
}
