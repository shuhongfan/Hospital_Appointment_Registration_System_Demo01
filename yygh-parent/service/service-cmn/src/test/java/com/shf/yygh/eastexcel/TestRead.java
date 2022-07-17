package com.shf.yygh.eastexcel;

import com.alibaba.excel.EasyExcel;

public class TestRead {
    public static void main(String[] args) {
//        读取文件路径
        String fileName = "C:\\Users\\shf\\Documents\\CODE\\Hospital_Appointment_Registration_System_Demo01\\yygh-parent\\service\\service-cmn\\src\\test\\java\\com\\shf\\yygh\\eastexcel\\01.xlsx";

//        调用方法实现读操作
        EasyExcel.read(fileName,UserData.class,new ExcelListener())
                .sheet()
                .doRead();

    }
}
