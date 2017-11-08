package com.hason.dtp.message.service;

import com.hason.dtp.message.config.properties.QueueMessageProperties;
import com.hason.dtp.message.dao.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * （消息状态确认子系统）消息状态服务实现类
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/11/8
 */
@Service
public class MessageStatusServiceImpl implements MessageStatusService {

    @Autowired
    private QueueMessageProperties properties;
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void handleWaitConfirmTimeout() {
        // 取出所有待确认、已超时的消息
        //
    }

    @Override
    public void handleSendingTimeout() {

    }

}
