package com.shf.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.shf.yygh.cmn.client.DictFeignClient;
import com.shf.yygh.hosp.repository.HospitalRepository;
import com.shf.yygh.hosp.service.HospitalService;

import com.shf.yygh.model.hosp.Hospital;
import com.shf.yygh.model.hosp.HospitalSet;
import com.shf.yygh.vo.hosp.HospitalQueryVo;
import com.shf.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    /**
     * 上传医院接口
     *
     * @param paramMap
     */
    @Override
    public void save(Map<String, Object> paramMap) {
//        把参数map集合转换对象Hospital
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

//        判断是否存在数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);

//        如果不存在，进行添加
        if (hospitalExist != null) {
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {//        如果存在,进行修改
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }


    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
//        创建pageable对象
        PageRequest pageable = PageRequest.of(page - 1, limit);
//        创建条件匹配器
        ExampleMatcher matching =
                ExampleMatcher
                        .matching()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase(true);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

//        创建对象
        Example<Hospital> example = Example.of(hospital, matching);

//        调用方法实现查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

//        获取查询list集合，遍历进行医院等级封装
        pages.getContent().stream().forEach(this::setHospitalHosType);

        return pages;
    }

    /**
     * 更新医院的上线状态
     *
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(String id, Integer status) {
//        根据id查询医院信息
        Hospital hospital = hospitalRepository.findById(id).get();
//        设置修改的值
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    /**
     * 医院详情信息
     * @param id
     * @return
     */
    @Override
    public Map<String,Object> getHospById(String id) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        Hospital hospital = hospitalRepository.findById(id).get();
        Hospital hospitalHosType = setHospitalHosType(hospital);
//        医院基本信息
        result.put("hospital", hospital);
        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);

        return result;
    }

    /**
     * 获取查询list集合，遍历进行医院等级封装
     * @param hospital
     * @return
     */
    private Hospital setHospitalHosType(Hospital hospital) {
//        根据dictCode和value获取医院等级名称
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
//        查询省市区
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("hostypeString", hostypeString);
        hospital.getParam().put("fullAddress", provinceString + cityString + districtString);
        return hospital;
    }

}
