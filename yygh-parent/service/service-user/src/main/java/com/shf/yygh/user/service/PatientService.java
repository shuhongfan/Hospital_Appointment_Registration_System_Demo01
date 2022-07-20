package com.shf.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shf.yygh.model.user.Patient;

import java.util.List;

public interface PatientService extends IService<Patient> {
    /**
     * 获取当前登录用户id
     * @param userId
     * @return
     */
    List<Patient> findAllUserId(Long userId);

    /**
     * 根据id获取就诊人信息
     * @param id
     * @return
     */
    Patient getPatientId(long id);
}
