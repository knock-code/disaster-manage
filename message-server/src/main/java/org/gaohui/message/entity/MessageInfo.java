package org.gaohui.message.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gaohui.common.pojo.BaseEntity;

/**
 * @author gaohui  2022/02/22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_message_info")
public class MessageInfo extends BaseEntity {

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
    @TableField("isread")
    private Boolean read;
    /**
     * 消息记录所属的组织
     */
    private String orgid;
}
