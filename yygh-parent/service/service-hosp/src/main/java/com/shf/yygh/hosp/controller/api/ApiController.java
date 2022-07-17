package com.shf.yygh.hosp.controller.api;



import com.shf.yygh.common.exception.YyghException;
import com.shf.yygh.common.helper.HttpRequestHelper;
import com.shf.yygh.common.result.Result;
import com.shf.yygh.common.result.ResultCodeEnum;
import com.shf.yygh.common.utils.MD5;
import com.shf.yygh.hosp.repository.ScheduleRepository;
import com.shf.yygh.hosp.service.DepartmentService;
import com.shf.yygh.hosp.service.HospitalService;
import com.shf.yygh.hosp.service.HospitalSetService;
import com.shf.yygh.hosp.service.ScheduleService;
import com.shf.yygh.model.hosp.Department;
import com.shf.yygh.model.hosp.Hospital;
import com.shf.yygh.model.hosp.Schedule;
import com.shf.yygh.vo.hosp.DepartmentQueryVo;
import com.shf.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "删除科室")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
//必填
        String hosScheduleId = (String)paramMap.get("hosScheduleId");
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }


        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }


    @ApiOperation(value = "获取排班分页列表")
    @PostMapping("schedule/list")
    public Result schedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
//非必填
        String depcode = (String)paramMap.get("depcode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));

        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleService.selectPage(page , limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }


    //    上传排班接口
    @ApiOperation(value = "上传排班")
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        scheduleService.save(paramMap);
        return Result.ok();
    }


    /**
     * 删除科室接口
     * @param request
     * @return
     */
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//必须参数校验 略
        String hoscode = (String) paramMap.get("hoscode");
//必填
        String depcode = (String) paramMap.get("depcode");
        if (StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
//签名校验
        if (!HttpRequestHelper.isSignEquals(paramMap, hospitalSetService.getSignKey(hoscode))) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.remove(hoscode, depcode);
        return Result.ok();

    }

    //    查询科室接口
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request) {
//        获取传递过来的科室信息
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestParameterMap);

//        医院编号
        String hoscode = (String) paramMap.get("hoscode");
//        当前页和每页记录数
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

//        调用service方法
        Page<Department> pageModel =
                departmentService.findPageDepartment(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    //    上传科室接口
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {

//        获取传递过来的科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

//        获取医院编号
        String hoscode = (String) paramMap.get("hoscode");

        //        1.获取医院系统传递过来的签名，签名进行MD5加密
        String hospSign = (String) paramMap.get("sign");
//        2. 根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);

//        3.把数据库查询签名进行MD5加密
        String signKeyMd5 = MD5.encrypt(signKey);

//        4. 判断签名是否一致
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

//        调用service方法
        departmentService.save(paramMap);
        return Result.ok();
    }

    //    上传医院接口
    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request) {
//        获取传递过来医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

//        1.获取医院系统传递过来的签名，签名进行MD5加密
        String hospSign = (String) paramMap.get("sign");
//        2. 根据传递过来的医院编码，查询数据库，查询签名
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

//        3.把数据库查询签名进行MD5加密
        String signKeyMd5 = MD5.encrypt(signKey);

//        4. 判断签名是否一致
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        paramMap.put("logoData", logoData);

//        调用service的方法
        hospitalService.save(paramMap);
        return Result.ok();
    }

    /**
     * 查询医院
     */
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
//        获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

//        获取医院编号
        String hoscode = (String) paramMap.get("hoscode");

        //        1.获取医院系统传递过来的签名，签名进行MD5加密
        String hospSign = (String) paramMap.get("sign");
//        2. 根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);

//        3.把数据库查询签名进行MD5加密
        String signKeyMd5 = MD5.encrypt(signKey);

//        4. 判断签名是否一致
        if (!hospSign.equals(signKeyMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

//        调用service方法实现根据医院编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }
}
