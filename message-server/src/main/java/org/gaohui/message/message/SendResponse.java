package org.gaohui.message.message;

import lombok.Data;

/**
 * 发送消息响应结果的 Message
 * @author GH
 */
@Data
public class SendResponse implements Message {

    public static final String TYPE = "SEND_RESPONSE";

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应提示
     */
    private String message;

}
