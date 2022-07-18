package com.shf.yygh.cmn.controller;

import com.shf.yygh.cmn.service.DictService;
import com.shf.yygh.common.result.Result;
import com.shf.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(description = "数据字典管理")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {

    @Autowired
    private DictService dictService;


    //    根据数据id查询子数据列表
    @GetMapping("findChildData/{id}")
    @ApiOperation("根据数据id查询子数据列表")
    public Result findChildData(@PathVariable("id") Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    //    导出数据字典接口
    @GetMapping("exportData")
    public Result exportDict(HttpServletResponse response) {
        dictService.exportDictData(response);
        return Result.ok();
    }

    //    导入数据字典接口
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        dictService.importDictData(file);
        return Result.ok();
    }

    // 根据dictcode和value查询数据字典
    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode, @PathVariable("value") String value) {
        String dictName = dictService.getDictName(dictCode, value);
        return dictName;
    }

    //根据value查询数据字典
    @GetMapping("getName/{value}")
    public String getName(@PathVariable("value") String value) {
        String dictName = dictService.getDictName("", value);
        return dictName;
    }

    //根据dictCode获取下级节点
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public Result<List<Dict>> findByDictCode(
            @ApiParam(name = "dictCode", value = "节点编码", required = true)
            @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }


}
