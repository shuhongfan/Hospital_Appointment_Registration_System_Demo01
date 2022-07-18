package com.shf.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shf.yygh.common.result.Result;
import com.shf.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    /**
     * 根据数据id查询子数据列表
     * @param id
     * @return
     */
    List<Dict> findChildData(Long id);

    /**
     * 导出数据字典接口
     * @param response
     */
    void exportDictData(HttpServletResponse response);

    /**
     * 导入数据字典接口
     * @param multipartFile
     */
    void importDictData(MultipartFile multipartFile);

    /**
     * 根据dictcode和value查询数据字典
     * @param dictCode
     * @param value
     * @return
     */
    String getDictName(String dictCode, String value);

    /**
     * 根据dictCode获取下级节点
     * @param dictCode
     * @return
     */
    List<Dict> findByDictCode(String dictCode);
}
