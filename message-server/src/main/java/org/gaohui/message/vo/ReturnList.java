package org.gaohui.message.vo;

import lombok.Data;
import org.gaohui.common.dto.UserDTO;

import java.io.Serializable;

/**
 * @author gaohui  2022/02/23
 */
@Data
public class ReturnList implements Serializable {
    /**
     * 用户信息
     */
    private UserDTO user;

    /**
     * 消息信息
     */
    private MessageInfoVO message;

    /**
     * 未读消息总数
     */
    private Integer count;
}
