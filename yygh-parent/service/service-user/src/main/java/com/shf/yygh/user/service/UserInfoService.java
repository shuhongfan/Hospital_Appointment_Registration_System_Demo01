package com.shf.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shf.yygh.model.user.UserInfo;
import com.shf.yygh.vo.user.LoginVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    /**
     * 用户手机号登录接口
     * @param loginVo
     * @return
     */
    Map<String, Object> loginUser(LoginVo loginVo);

    /**
     * 根据openid查询用户信息
     * @param openId
     * @return
     */
    UserInfo getByOpenid(String openId);
}
