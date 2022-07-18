package com.shf.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shf.yygh.cmn.listener.DictListener;
import com.shf.yygh.cmn.mapper.DictMapper;
import com.shf.yygh.cmn.service.DictService;
import com.shf.yygh.common.result.Result;
import com.shf.yygh.model.cmn.Dict;
import com.shf.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    /**
     * 根据数据id查询子数据列表
     *
     * @param id
     * @return
     */
    @Override
    @Cacheable(value = "dict",keyGenerator="keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(wrapper);

//        向list集合每个dict对象中设置hasChildren
        dictList.stream().forEach(dict -> {
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        });
        return dictList;
    }

    /**
     * 导出数据字典接口
     * @param response
     */
    @Override
    public void exportDictData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

//        查询数据库
            List<Dict> dictList = baseMapper.selectList(null);

//            Dict--->DictEeVo
            ArrayList<DictEeVo> dictEeVos = new ArrayList<>();
            for (Dict dict : dictList) {
                DictEeVo dictEeVo = new DictEeVo();
                BeanUtils.copyProperties(dict, dictEeVo);
                dictEeVos.add(dictEeVo);
            }

//        调用方法进行写操作
            EasyExcel.write(response.getOutputStream(), DictEeVo.class)
                    .sheet("数据字典")
                    .doWrite(dictEeVos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 导入数据字典接口
     * @param multipartFile
     */
    @Override
    @CacheEvict(value = "dict",allEntries = true)
    public void importDictData(MultipartFile multipartFile) {
        try {
            EasyExcel.read(
                    multipartFile.getInputStream(),
                            DictEeVo.class,
                            new DictListener(baseMapper))
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据dictcode和value查询数据字典
     * @param dictCode
     * @param value
     * @return
     */
    @Override
    public String getDictName(String dictCode, String value) {
//        如果dictCode为空，直接根据value查询
        if (StringUtils.isEmpty(dictCode)) {
//            直接根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else { // 如果dictCode不为空，根据dictCode和value查询
//            根据dictcode查询dict对象，得到dict的id值
            Dict dictByDictCode = getDictByDictCode(dictCode);
            Long parent_id = dictByDictCode.getId();

//            根据parent_id和value进行查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", parent_id)
                    .eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }
    }

    /**
     * 根据dictCode获取下级节点
     * @param dictCode
     * @return
     */
    @Override
    public List<Dict> findByDictCode(String dictCode) {
//        根据dictCode获取对应id
        Dict dict = getDictByDictCode(dictCode);
//        根据id获取子节点
        List<Dict> childData = findChildData(dict.getId());
        return childData;
    }

    /**
     * 根据dictcode查询dict对象
     * @param dictCode
     * @return
     */
    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return codeDict;
    }


    /**
     * 判断id下面是否有子节点
     * @param id
     * @return
     */
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);

        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }
}
