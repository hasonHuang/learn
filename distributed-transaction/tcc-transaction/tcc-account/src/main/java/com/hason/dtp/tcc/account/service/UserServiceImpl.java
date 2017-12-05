package com.hason.dtp.tcc.account.service;

import com.hason.dtp.core.exception.ErrorCode;
import com.hason.dtp.core.exception.ServiceException;
import com.hason.dtp.core.utils.JsonMapper;
import com.hason.dtp.core.utils.result.Result;
import com.hason.dtp.tcc.account.dao.UserRepository;
import com.hason.dtp.tcc.account.entity.User;
import org.mengyun.tcctransaction.Compensable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.hason.dtp.core.utils.CheckUtil.notFalse;
import static com.hason.dtp.core.utils.CheckUtil.notNull;

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

    @Compensable(confirmMethod = "confirmRegister", cancelMethod = "cancelRegister")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User register(User user) {
        notNull(user, "arg.null", "用户对象");
        notNull(user.getUsername(), "arg.null", "用户名");

        User exists = userRepository.findByUsername(user.getUsername());
        notFalse(exists == null, "arg.illegal", "用户名");

        user.setCreateTime(LocalDateTime.now());
        user.setModifiedTime(user.getCreateTime());
        user = save(user);

        // 注册积分账户

        // 注册资金账户

        return user;
    }

    @Override
    public User confirmRegister(User user) {
        return null;
    }

    @Override
    public User cancelRegister(User user) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User save(User user) {
        notNull(user, "arg.null", "用户对象");
        notNull(user.getUsername(), "arg.null", "用户名");
        notNull(user.getPassword(), "arg.null", "用户密码");
        userRepository.save(user);
        return user;
    }

    @Override
    public User get(Long userId) {
        notNull(userId, "arg.null", "用户ID");
        return userRepository.findOne(userId);
    }

}
