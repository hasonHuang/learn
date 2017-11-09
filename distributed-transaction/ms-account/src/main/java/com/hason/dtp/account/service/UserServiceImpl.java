package com.hason.dtp.account.service;

import static com.hason.dtp.core.utils.CheckUtil.notNull;

import com.hason.dtp.account.dao.UserRepository;
import com.hason.dtp.account.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户业务实现类
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/11/9
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User save(User user) {
        notNull(user, "arg.null", "用户对象");
        notNull(user.getUsername(), "arg.null", "用户名");
        notNull(user.getPassword(), "arg.null", "用户密码");
        notNull(user.getBalance(), "arg.null", "用户余额");
        notNull(user.getPoint(), "arg.null", "用户积分");
        userRepository.save(user);
        return user;
    }

    @Override
    public User get(Long userId) {
        notNull(userId, "arg.null", "用户ID");
        return userRepository.findOne(userId);
    }
}
