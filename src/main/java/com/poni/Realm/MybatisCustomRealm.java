package com.poni.Realm;

import com.poni.dao.UserDao;
import com.poni.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/*
 *@author:PONI_CHAN
 *@date:2018/12/19 17:15
 */
@Service("realm")
public class MybatisCustomRealm extends AuthorizingRealm {

    @Resource
    public UserDao userDao;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String username = (String) principals.getPrimaryPrincipal();

        Set<String> roles= getRolesByUserName(username);
        Set<String> permissions = getPermissionByUsername(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //1.从主体传过来的认证信息中，获得用户名
        String username = (String) authenticationToken.getPrincipal();

        //2.通过用户名到数据库中获取凭证
        String password = getPasswordByUsername(username);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, "MybatisCustomRealm");
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username));
        return authenticationInfo;
    }

    private Set<String> getPermissionByUsername(String username) {

        Set<String> sets = new HashSet<>();
        sets.add("user:update");
        sets.add("user:delete");
        return sets;
    }

    private Set<String> getRolesByUserName(String username) {

        System.out.println("从数据库中获取授权数据");

        // 从数据库或者缓存中获取角色数据
//        List<String> list = userDao.queryRolesByUsername(username);
//        return new HashSet<>(list);
        Set<String> sets = new HashSet<>();
        sets.add("admin");
        sets.add("manager");
        return sets;
    }

    /**
     * 模拟数据库查询凭证
     *
     * @param username
     * @return
     */
    private String getPasswordByUsername(String username) {
        User user = userDao.getUserByUsername(username);

        if (user != null) {
            return user.getPassword();
        }

        return null;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("666777", "zzm");
        System.out.println(md5Hash.toString());
    }
}