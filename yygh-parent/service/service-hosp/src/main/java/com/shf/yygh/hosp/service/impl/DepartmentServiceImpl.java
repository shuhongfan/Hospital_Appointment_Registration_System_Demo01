package com.shf.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.shf.yygh.hosp.repository.DepartmentRepository;
import com.shf.yygh.hosp.service.DepartmentService;
import com.shf.yygh.model.hosp.Department;
import com.shf.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 上传科室接口
     *
     * @param paramMap
     */
    @Override
    public void save(Map<String, Object> paramMap) {
        String paramMapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramMapString, Department.class);

//        根据医院编号和科室编号查询
        Department departmentExist =
                departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());

        if (departmentExist !=null) {
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
//        创建pageable对象，设置当前页和每页记录数
        Pageable pageable = PageRequest.of(page-1, limit);

//        创建Example对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Example<Department> example = Example.of(department, matcher);
        Page<Department> all = departmentRepository.findAll(example, pageable);
        return all;
    }

    /**
     * 删除科室接口
     * @param hoscode
     * @param depcode
     */
    @Override
    public void remove(String hoscode, String depcode) {
//        根据医院编号和科室编号查询
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            departmentRepository.deleteById(department.getId());
        }
    }
}
