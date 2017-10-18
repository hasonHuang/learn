package com.hason.dtp.account.dao;

import com.hason.dtp.account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户dao
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/10/18
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
