package com.shf.yygh.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shf.yygh.model.user.UserInfo;
import com.shf.yygh.vo.user.LoginVo;
import com.shf.yygh.vo.user.UserAuthVo;
import com.shf.yygh.vo.user.UserInfoQueryVo;

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

    /**
     * 用户认证接口
     * @param userId
     * @param userAuthVo
     */
    void userAuth(Long userId, UserAuthVo userAuthVo);

    /**
     * 用户列表（条件查询带分页）
     * @param pageParam
     * @param userInfoQueryVo
     * @return
     */
    Page<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    /**
     * 用户锁定
     * @param userId
     * @param status
     */
    void lock(Long userId, Integer status);

    /**
     * 用户详情
     * @param userId
     * @return
     */
    Map<String, Object> show(Long userId);

    /**
     * 认证审批
     * @param userId
     * @param authStatus
     */
    void approval(Long userId, Integer authStatus);
}
