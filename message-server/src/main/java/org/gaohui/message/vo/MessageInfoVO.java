package org.gaohui.message.vo;

import lombok.Data;
import org.gaohui.message.entity.MessageInfo;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author gaohui  2022/02/22
 */
@Data
public class MessageInfoVO {

    /**
     * id
     */
    private String id;
    /**
     * 发送者
     */
    private String sendby;
    /**
     * 接受者
     */
    private String receiveby;
    /**
     * 消息类型
     */
    private String type;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息是否已读
     */
    private Boolean read;
    /**
     * 消息发送的时间
     */
    private Date createtime;

    public MessageInfoVO(MessageInfo messageInfo) {
        if(messageInfo!=null) {
            BeanUtils.copyProperties( messageInfo,this,new String[] {""});
        }
    }
}
