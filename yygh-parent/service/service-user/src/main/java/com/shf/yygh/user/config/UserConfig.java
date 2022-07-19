package com.shf.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.shf.yygh.user.mapper")
public class UserConfig {
}
