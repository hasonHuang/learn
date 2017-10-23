package com.hason.dtp.message.api;

import com.hason.dtp.message.entity.Message;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 可靠消息接口
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/10/20
 */
public interface MessageApi {


    /**
     * 预存储消息.
     */
    @RequestMapping(value = "/messages", method = POST)
    int saveMessageWaitingConfirm(@RequestBody Message message);

    /**
     * 确认并发送消息.
     */
    @RequestMapping(value = "/messages/{messageId}/confirm-send", method = PATCH)
    void confirmAndSendMessage(@PathVariable("messageId") String messageId);

    /**
     * 存储并发送消息.
     */
    @RequestMapping(value = "/messages/send", method = POST)
    int saveAndSendMessage(@RequestBody Message message);

    /**
     * 直接发送消息.
     */
    @RequestMapping(value = "/messages/direct-send", method = POST)
    void directSendMessage(@RequestBody Message message);

    /**
     * 重发消息.
     */
    @RequestMapping(value = "/messages", method = PUT)
    void reSendMessage(@RequestBody Message message);

    /**
     * 根据messageId重发某条消息.
     */
    @RequestMapping(value = "/messages/{messageId}/send", method = PUT)
    void reSendMessageByMessageId(@PathVariable String messageId);

    /**
     * 将消息标记为死亡消息.
     */
    @RequestMapping(value = "/messages/{messageId}/status/dead", method = PUT)
    void setMessageToAreadlyDead(@PathVariable String messageId);

    /**
     * 根据消息ID获取消息
     */
    @RequestMapping(value = "/messages/{messageId}", method = GET)
    Message getMessageByMessageId(@PathVariable String messageId);

    /**
     * 根据消息ID删除消息
     */
    @RequestMapping(value = "/messages/{messageId}", method = DELETE)
    void deleteMessageByMessageId(@PathVariable String messageId);

    /**
     * 重发某个消息队列中的全部已死亡的消息.
     */
    @RequestMapping(value = "/queues/names/{queueName}", method = PUT)
    void reSendAllDeadMessageByQueueName(@PathVariable String queueName, int batchSize);

    /**
     * 获取分页数据
     */
//    PageBean listPage(PageParam pageParam, Map<String, Object> paramMap);

}
