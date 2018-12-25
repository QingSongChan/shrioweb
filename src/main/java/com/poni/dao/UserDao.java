package com.poni.dao;

import com.poni.vo.User;

import java.util.List;

/*
 *@author:PONI_CHAN
 *@date:2018/12/20 15:39
 */
public interface UserDao {

    User getUserByUsername(String username);

    List<String> queryRolesByUsername(String username);

    List<String> queryPermissionByUsername(String rolename);

}
