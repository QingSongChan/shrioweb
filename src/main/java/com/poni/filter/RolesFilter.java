package com.poni.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/*
 *@author:PONI_CHAN
 *@date:2018/12/22 16:04
 */
//根据不同需求实现不同Filter
public class RolesFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {

        //得到当前主体
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] roles = (String[]) o;
        if (roles == null || roles.length == 0) {
            return true;
        }
        //遍历数据库中用户对应的roles
        for (String role : roles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }


}
