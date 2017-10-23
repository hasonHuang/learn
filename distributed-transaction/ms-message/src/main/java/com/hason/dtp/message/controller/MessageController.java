package com.hason.dtp.message.controller;

import com.hason.dtp.core.exception.ErrorCode;
import com.hason.dtp.core.exception.ErrorResult;
import com.hason.dtp.core.exception.ServiceException;
import com.hason.dtp.message.api.MessageApi;
import com.hason.dtp.message.entity.Message;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 消息 Controller
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/10/23
 */
@RestController
public class MessageController implements MessageApi {

    @RequestMapping(value = "/messages", method = POST)
    @Override
    public int saveMessageWaitingConfirm(@RequestBody Message message) {
        return 0;
    }

    @RequestMapping(value = "/messages/{messageId}/confirm-send", method = PATCH)
    @Override
    public void confirmAndSendMessage(@PathVariable String messageId) {
    }

    @RequestMapping(value = "/messages/send", method = POST)
    @Override
    public int saveAndSendMessage(@RequestBody Message message) {
        return 0;
    }

    @RequestMapping(value = "/messages/direct-send", method = POST)
    @Override
    public void directSendMessage(@RequestBody Message message) {

    }

    @Override
    public void reSendMessage(Message message) {

    }

    @Override
    public void reSendMessageByMessageId(String messageId) {

    }

    @Override
    public void setMessageToAreadlyDead(String messageId) {

    }

    @Override
    public Message getMessageByMessageId(String messageId) {
        return null;
    }

    @Override
    public void deleteMessageByMessageId(String messageId) {

    }

    @Override
    public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) {

    }
}
