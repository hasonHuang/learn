package com.hason.dtp.message.service;

import com.hason.dtp.core.exception.ServiceException;
import com.hason.dtp.message.config.properties.QueueMessageProperties;
import com.hason.dtp.message.dao.MessageRepository;
import com.hason.dtp.message.entity.Message;
import com.hason.dtp.message.entity.constant.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * （消息状态确认子系统）消息状态服务实现类
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/11/8
 */
@Service
public class MessageStatusServiceImpl implements MessageStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageStatusServiceImpl.class);

    @Autowired
    private QueueMessageProperties properties;
    @Autowired
    private List<StatusHandlerService> statusHandlerServices;

    @Autowired
    private MessageRepository messageRepository;

    /** 页大小 */
    private static final int PAGE_SIZE = 1000;
    /** 每次处理多少页 */
    private static final int PAGE_PER_HANDLE = 3;

    @Override
    public void handleWaitConfirmTimeout() {
        // 取出所有待确认、已超时的消息
        int pageNum = 1;
        // 获取消息的最后过期时间
        LocalDateTime expireTime = getLastExpireTime();
        while (pageNum <= PAGE_PER_HANDLE) {
            Pageable pageable = new PageRequest(pageNum++, PAGE_SIZE);
            Page<Message> page = messageRepository.findByStatusAndCreateTime(
                    MessageStatus.TO_BE_CONFIRMED, Boolean.FALSE, expireTime, pageable);
            handleWaitConfirmTimeout(page.getContent());
        }
    }

    private void handleWaitConfirmTimeout(Collection<Message> messages) {
        for (Message message : messages) {
            // 单条消息处理（目前该状态的消息全部是积分队列，如果后期有业务扩充,需做队列判断，做对应的业务处理）
            for (StatusHandlerService handlerService : statusHandlerServices) {
                try {
                    if (handlerService.canHandle(message)) {
                        handlerService.handle(message);
                        break;
                    }
                } catch (ServiceException e) {
                    LOGGER.warn("消息处理异常，消息表ID：" + message.getId());
                }
            }
        }
    }

    @Override
    public void handleSendingTimeout() {

    }

    /**
     * 获取消息目前已经过期的时间（时间 = 现在 - 生存时间）
     *
     * @return LocalDateTime
     */
    private LocalDateTime getLastExpireTime() {
        return LocalDateTime.now().minusSeconds(properties.getExpire());
    }

}
