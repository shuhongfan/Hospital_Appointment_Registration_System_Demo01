package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.model.acl.User;

public class TestRead {

    public static void main(String[] args) {
        // 读取文件路径
        String fileName = "F:\\excel\\01.xlsx";
        //调用方法实现读取操作
        EasyExcel.read(fileName, UserData.class,new ExcelListener()).sheet().doRead();
    }
}
