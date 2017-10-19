package com.hason.dtp.account.dao;

import com.hason.dtp.account.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 事务消息 Repository
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/10/19
 */
public interface MessageRepository extends JpaRepository<Message, String>, JpaSpecificationExecutor<Message> {
}
