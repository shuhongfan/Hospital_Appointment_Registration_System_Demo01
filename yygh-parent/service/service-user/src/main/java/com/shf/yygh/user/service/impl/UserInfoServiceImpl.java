package com.shf.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shf.yygh.common.exception.YyghException;
import com.shf.yygh.common.helper.JwtHelper;
import com.shf.yygh.common.result.ResultCodeEnum;
import com.shf.yygh.model.user.UserInfo;
import com.shf.yygh.user.mapper.UserInfoMapper;
import com.shf.yygh.user.service.UserInfoService;
import com.shf.yygh.vo.user.LoginVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    /**
     * 用户手机号登录接口
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
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        UserInfo userInfo = baseMapper.selectOne(wrapper);

//        判断是否第一次登录：根据手机号查询数据库，如果不存在相同手机号就是第一次登录
//        第一次使用手机号登录,创建新用户
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            baseMapper.insert(userInfo);
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
}
