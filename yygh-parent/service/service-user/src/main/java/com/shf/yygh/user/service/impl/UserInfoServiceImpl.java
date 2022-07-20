package com.shf.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shf.yygh.common.exception.YyghException;
import com.shf.yygh.common.helper.JwtHelper;
import com.shf.yygh.common.result.ResultCodeEnum;
import com.shf.yygh.enums.AuthStatusEnum;
import com.shf.yygh.model.user.Patient;
import com.shf.yygh.model.user.UserInfo;
import com.shf.yygh.user.mapper.UserInfoMapper;
import com.shf.yygh.user.service.PatientService;
import com.shf.yygh.user.service.UserInfoService;
import com.shf.yygh.vo.user.LoginVo;
import com.shf.yygh.vo.user.UserAuthVo;
import com.shf.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PatientService patientService;

    /**
     * 用户手机号登录接口
     *
     * @param loginVo
     * @return
     */
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
//        从loginVo获取输入的手机号和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

//        判断手机号和验证码是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

//        判断手机验证码和输入的验证码是否一致
        String mobileCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(mobileCode)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR); // 验证码错误
        }

//        绑定手机号码
        UserInfo userInfo = null;
        if (!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = getByOpenid(loginVo.getOpenid()); // 通过openid查找用户
            if (null != userInfo) { // 用户存在
                userInfo.setPhone(loginVo.getPhone()); // 设置用户手机号
                updateById(userInfo); // 更新用户信息
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }

//        如果userinfo为空，进行正常的手机登录
//        判断是否第一次登录：根据手机号查询数据库，如果不存在相同手机号就是第一次登录
        if (null == userInfo) {
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", phone);
            userInfo = userInfoMapper.selectOne(wrapper);
            if (null == userInfo) { // 创建新用户
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                this.save(userInfo);
            }
        }

//        校验用户是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

//        不是第一次登录，直接登录
//        返回登录信息
        HashMap<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
//        判断用户名是否为空，如果用户名为空则把用户名设置为昵称
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
//        如果昵称也为空，把电话号码设置为用户名
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }

//        返回登录用户名
        map.put("name", name);

//        JWT生成TOKEN
        String token = JwtHelper.createToken(userInfo.getId(), name);
//        返回token信息
        map.put("token", token);

        return map;
    }

    /**
     * 根据openid查询用户信息
     *
     * @param openId
     * @return
     */
    @Override
    public UserInfo getByOpenid(String openId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openId);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        return userInfo;
    }

    /**
     * 用户认证接口
     * @param userId
     * @param userAuthVo
     */
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
//        根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);

//        设置认证信息
        //认证人姓名
        userInfo.setName(userAuthVo.getName());
        //其他认证信息
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());

        //进行信息更新
        baseMapper.updateById(userInfo);
    }


    /**
     * 用户列表（条件查询带分页）
     *
     * @param pageParam
     * @param userInfoQueryVo
     * @return
     */
    @Override
    public Page<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
//        UserInfoQueryVo获取条件值
        String name = userInfoQueryVo.getKeyword(); //用户名称
        Integer status = userInfoQueryVo.getStatus(); //用户状态
        Integer authStatus = userInfoQueryVo.getAuthStatus(); //认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin(); //开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd(); //结束时间

//        对条件值进行非空判断
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (!StringUtils.isEmpty(authStatus)) {
            wrapper.eq("auth_status", authStatus);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time", createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time", createTimeEnd);
        }

//        调用mapper的方法
        Page<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
//        编号变成对应的值
        pages.getRecords().stream().forEach(this::packageUserInfo);
        return pages;
    }

    /**
     * 用户锁定
     * @param userId
     * @param status
     */
    @Override
    public void lock(Long userId, Integer status) {
        if (status.intValue() == 0 || status.intValue() == 1) {
            UserInfo userInfo = getById(userId);
            userInfo.setStatus(status);
            updateById(userInfo);
        }
    }

    /**
     * 用户详情
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> show(Long userId) {
        HashMap<String, Object> map = new HashMap<>();
//        根据userid查询用户信息
        UserInfo userInfo = packageUserInfo(baseMapper.selectById(userId));
        map.put("userInfo", userInfo);
//        根据userId查询就诊人信息
        List<Patient> patientList = patientService.findAllUserId(userId);
        map.put("patientList", patientList);
        return map;
    }

    /**
     * 认证审批  2通过  -1不通过
     * @param userId
     * @param authStatus
     */
    @Override
    public void approval(Long userId, Integer authStatus) {
        if (authStatus.intValue() == 2 || authStatus.intValue() == -1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

    /**
     * 编号变成对应的值
     * @param userInfo
     * @return
     */
    private UserInfo packageUserInfo(UserInfo userInfo) {
//        处理认证状态编码
        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
//        处理用户状态 0 1
        String statusString = userInfo.getStatus().intValue() == 0 ? "锁定" : "正常";
        userInfo.getParam().put("statusString", statusString);
        return userInfo;
    }
}
