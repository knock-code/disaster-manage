package org.gaohui.message.message;

/**
 * 发送给所有人的群聊消息的 Message
 * @author GH
 */
public class SendToAllRequest implements Message {

    public static final String TYPE = "SEND_TO_ALL_REQUEST";

    /**
     * 内容
     */
    private String content;

}
