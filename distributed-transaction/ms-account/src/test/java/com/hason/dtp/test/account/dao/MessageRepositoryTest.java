package com.hason.dtp.test.account.dao;

import com.hason.dtp.account.dao.MessageRepository;
import com.hason.dtp.message.entity.Message;
import com.hason.dtp.message.entity.constant.MessageDataType;
import com.hason.dtp.message.entity.constant.MessageStatus;
import com.hason.dtp.test.account.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * MessageRepository 单元测试
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/10/19
 */
public class MessageRepositoryTest extends BaseTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void testSave() {
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDataType(MessageDataType.JSON);
        message.setMessageBody("消息内容");
        message.setDead(Boolean.FALSE);
        message.setStatus(MessageStatus.SENDED);
        message.setCreateTime(LocalDateTime.now());
        message.setEditTime(LocalDateTime.now());
        message.setField1(UUID.randomUUID().toString());

        messageRepository.save(message);


        System.out.println(message);
    }

}
