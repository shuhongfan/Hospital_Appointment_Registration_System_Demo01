package com.shf.yygh.hosp.controller.api;

import com.shf.yygh.common.result.Result;
import com.shf.yygh.hosp.service.DepartmentService;
import com.shf.yygh.hosp.service.HospitalService;
import com.shf.yygh.model.hosp.Hospital;
import com.shf.yygh.vo.hosp.DepartmentVo;
import com.shf.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation("查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(
            @PathVariable int page,
            @PathVariable int limit, HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospPage = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospPage);
    }

    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("findByHosname/{hosname}")
    public Result findByHosname(
            @ApiParam(name = "hosname", value="医院名称", required = true)
            @PathVariable String hosname) {
        return Result.ok(hospitalService.findByHosname(hosname));
    }

    @ApiOperation("根据医院编号获取科室")
    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode) {
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("{hoscode}")
    public Result item(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode) {
        return Result.ok(hospitalService.item(hoscode));
    }

}
