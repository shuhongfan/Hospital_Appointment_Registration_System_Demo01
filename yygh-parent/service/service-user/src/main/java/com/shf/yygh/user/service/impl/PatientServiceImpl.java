package com.shf.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shf.yygh.cmn.client.DictFeignClient;
import com.shf.yygh.enums.DictEnum;
import com.shf.yygh.model.user.Patient;
import com.shf.yygh.user.mapper.PatientMapper;
import com.shf.yygh.user.mapper.UserInfoMapper;
import com.shf.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private DictFeignClient dictFeignClient;

    /**
     * 获取当前登录用户id
     *
     * @param userId
     * @return
     */
    @Override
    public List<Patient> findAllUserId(Long userId) {
//        根据userid查询所有就诊人信息列表
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Patient> patientList = baseMapper.selectList(wrapper);

//        通过远程调用，得到编码对应的具体内容，查询数据字典表内容
        patientList.stream().forEach(this::packPatient);
        return patientList;
    }

    /**
     * 根据id获取就诊人信息
     * @param id
     * @return
     */
    @Override
    public Patient getPatientId(long id) {
        return packPatient(baseMapper.selectById(id));
    }

    //    Patient 其他对象封装
    private Patient packPatient(Patient patient) {
        //根据证件类型编码，获取证件类型具体值
        String certificatesTypeString = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());

        //联系人证件类型
        String contactsCertificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getContactsCertificatesType());
        //省
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        //市
        String cityString = dictFeignClient.getName(patient.getCityCode());
        //区
        String districtString = dictFeignClient.getName(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
        return patient;
    }
}
