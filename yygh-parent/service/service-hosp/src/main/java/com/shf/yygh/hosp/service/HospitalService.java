package com.shf.yygh.hosp.service;


import com.shf.yygh.model.hosp.Hospital;
import com.shf.yygh.vo.hosp.HospitalQueryVo;
import com.shf.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    /**
     * 上传医院接口
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);

    Page selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    /**
     * 更新医院的上线状态
     * @param id
     * @param status
     */
    void updateStatus(String id, Integer status);

    /**
     * 医院详情信息
     * @param id
     * @return
     */
    Map<String,Object> getHospById(String id);

    /**
     * 获取医院名称
     * @param hoscode
     * @return
     */
    String getHospName(String hoscode);

    List<Hospital> findByHosname(String hosname);

    /**
     * 医院预约挂号详情
     */

    Map<String,Object> item(String hoscode);
}
