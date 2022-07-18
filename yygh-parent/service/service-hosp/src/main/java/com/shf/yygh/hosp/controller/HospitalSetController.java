package com.shf.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shf.yygh.common.exception.YyghException;
import com.shf.yygh.common.result.Result;
import com.shf.yygh.common.utils.MD5;
import com.shf.yygh.hosp.service.HospitalSetService;
import com.shf.yygh.model.hosp.HospitalSet;
import com.shf.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    //    1 查询医院设置表的所有信息
    @ApiOperation("查询医院设置表的所有信息")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
//        调用service方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //    2. 逻辑删除医院设置
    @ApiOperation("逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //    3.条件查询带分页
    @ApiOperation("条件查询带分页")
    @PostMapping("findPage/{current}/{limit}")
    public Result findPageHospSet(
            @PathVariable Long current,
            @PathVariable Long limit,
            @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
//        创建page对象，传递当前页、每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
//        构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();

        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(hoscode)) {
            wrapper.like("hoscode", hoscode);
        }

//        调用方法实现分页
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);

        return Result.ok(hospitalSetPage);
    }

    //    4. 添加医院设置
    @ApiOperation("添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
//        设置状态  1使用  0不能使用
        hospitalSet.setStatus(1);
//        签名秘钥
        Random random = new Random();
        String encrypt = MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setSignKey(encrypt);

//        调用Service保存
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }


    //    5. 根据id获取医院设置
    @ApiOperation("根据id获取医院设置")
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id) {
//        try {
//            int i = 1 / 0;
//        } catch (Exception e) {
//            throw new YyghException("失败",201);
//        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //    6. 修改医院设置
    @ApiOperation("修改医院设置")
    @PutMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //    7. 批量删除医院设置
    @ApiOperation("批量删除医院设置")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    //    8.医院设置锁定和解锁
    @ApiOperation("医院设置锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(
            @PathVariable Long id,
            @PathVariable Integer status) {
//        根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
//        设置状态
        hospitalSet.setStatus(status);
//        调用方法
        boolean res = hospitalSetService.updateById(hospitalSet);
        if (res) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //    9. 发送签名秘钥
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
//        TODO 发送短信
        return Result.ok();
    }
}
