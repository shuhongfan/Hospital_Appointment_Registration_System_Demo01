package com.shf.yygh.hosp.service;


import com.shf.yygh.model.hosp.Department;
import com.shf.yygh.vo.hosp.DepartmentQueryVo;
import com.shf.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);

    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);

    /**
     * 查询医院所有科室列表
     * @param hoscode
     * @return
     */
    List<DepartmentVo> findDeptTree(String hoscode);

    //根据科室编号，和医院编号，查询科室名称
    String getDepName(String hoscode, String depcode);

    /**
     * 获取部门
     */
    Department getDepartment(String hoscode, String depcode);

}
