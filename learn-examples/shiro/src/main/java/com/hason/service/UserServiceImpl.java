package com.hason.service;

import com.hason.dao.UserDao;
import com.hason.entity.User;
import com.hason.util.PasswordHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户接口
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/26
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

//    private PasswordHelper passwordHelper = new PasswordHelper();
    @Autowired
    private PasswordHelper passwordHelper;

    @Autowired
    private UserDao userDao;

    @Override
    public User createUser(User user) {
        passwordHelper.encrypt(user);
        userDao.createUser(user);
        return user;
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user = userDao.findOne(userId);
        user.setPassword(newPassword);
        passwordHelper.encrypt(user);
        userDao.updateUser(user);
    }

    @Override
    public void correlationRoles(Long userId, Long... roleIds) {
        userDao.correlationRoles(userId, roleIds);
    }

    @Override
    public void uncorrelationRoles(Long userId, Long... roleIds) {
        userDao.uncorrelationRoles(userId, roleIds);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Set<String> findRoles(String username) {
        return userDao.findRoles(username);
    }

    @Override
    public Set<String> findPermissions(String username) {
        return userDao.findPermissions(username);
    }
}
