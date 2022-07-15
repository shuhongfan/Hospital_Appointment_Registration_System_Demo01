package com.shf.mybatis_plus_demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    /**
     * Mybatisplus默认主键策略
     * ASSIGN_ID 通过雪花算法，生成19位随机值
     * 整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞，并且效率较高
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private Integer age;
    private String email;

    /**
     * 为自动填充属性添加注解
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 乐观锁
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    @TableLogic
    private Integer deleted;
}
