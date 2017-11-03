package com.hason.dtp.message.api;

import com.hason.dtp.message.entity.Message;

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
    int saveMessageWaitingConfirm(Message message);


    /**
     * 确认并发送消息.
     */
    void confirmAndSendMessage(String messageId);


    /**
     * 存储并发送消息.
     */
    int saveAndSendMessage(Message message);


    /**
     * 直接发送消息.
     */
    void directSendMessage(Message message);


    /**
     * 重发消息.
     */
    void reSendMessage(Message message);


    /**
     * 根据messageId重发某条消息.
     */
    void reSendMessageByMessageId(String messageId);


    /**
     * 将消息标记为死亡消息.
     */
    void setMessageToAreadlyDead(String messageId);


    /**
     * 根据消息ID获取消息
     */
    Message getMessageByMessageId(String messageId);

    /**
     * 根据消息ID删除消息
     */
    void deleteMessageByMessageId(String messageId);

    /**
     * 重发某个消息队列中的全部已死亡的消息.
     */
    void reSendAllDeadMessageByQueueName(String queueName, int batchSize);

    /**
     * 获取分页数据
     */
//    PageBean listPage(PageParam pageParam, Map<String, Object> paramMap);

}
