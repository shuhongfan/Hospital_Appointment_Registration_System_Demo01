package com.shf.yygh.user.api;

import com.shf.yygh.common.result.Result;
import com.shf.yygh.common.utils.AuthContextHolder;
import com.shf.yygh.model.user.UserInfo;
import com.shf.yygh.user.service.UserInfoService;
import com.shf.yygh.vo.user.LoginVo;
import com.shf.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;


    //    用户手机号登录接口
    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> info = userInfoService.loginUser(loginVo);
        return Result.ok(info);
    }

    //    用户认证接口
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
//        传递两个参数，第一个参数为用户id，第二个参数为认证数据vo对象
        Long userId = AuthContextHolder.getUserId(request);
        userInfoService.userAuth(userId, userAuthVo);
        return Result.ok();
    }

    //    获取用户id信息接口
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);

        return Result.ok(userInfo);
    }
}
