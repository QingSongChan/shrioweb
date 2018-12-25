package com.poni.controller;

import com.poni.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 *@author:PONI_CHAN
 *@date:2018/12/19 17:20
 */
@Controller
public class UserController {

    @RequestMapping(value = "/subLogin", method = RequestMethod.POST,
            produces = "application/json;charset=utf-8")
    @ResponseBody
    public String subLogin(User user) {
        Subject subject = SecurityUtils.getSubject();   //Subject获得主体
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            //记住密码，判断前端传来的boolean
            token.setRememberMe(user.isRemeberMe());
            subject.login(token);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
        if (subject.hasRole("admin")) {
            return "欢迎光临admin管理员，你没有任何权限！";
        }
        return "你好路人甲";
    }

//    @RequiresRoles("admin")
    @RequestMapping(value = "/testRole",method = RequestMethod.GET)
    @ResponseBody
    public String testRole(){
        return "admin success";
    }

//    @RequiresPermissions("user:update")
    @RequestMapping(value = "/testRole1",method = RequestMethod.GET)
    @ResponseBody
    public String testRole1(){
        return "manager success";
    }

    @RequestMapping(value = "/testPerm",method = RequestMethod.GET)
    @ResponseBody
    public String testPerm(){
        return "manager1 success";
    }


}