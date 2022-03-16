package org.gaohui.message.message;

import lombok.Data;

/**
 * 发送给指定人的私聊消息的 Message
 * @author GH
 */
@Data
public class SendToOneRequest implements Message {

    public static final String TYPE = "SEND_TO_ONE_REQUEST";

    /**
     * 所处的组织
     */
    private String orgId;
    /**
     * 消息是否已读
     */
    private Boolean read;
    /**
     * 发送时间
     */
    private String createtime;
    /**
     * 发送人
     */
    private String sendby;
    /**
     * 消息接受者
     */
    private String receiveby;
    /**
     * 内容
     */
    private String content;
    /**
     * 消息内容类型
     */
    private String type;
}
