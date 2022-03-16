package org.gaohui.message.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author gaohui  2022/02/24
 */
@Data
public class CharRecordVO {
    /**
     * 每页显示大小
     */
    private Long  size;

    /**
     * 当前页码
     */
    private  Long current;
    /**
     * 聊天对象
     */
    @NotBlank(message = "聊天对象不能为空!")
    private String member;

    /**
     * 聊天组织
     */
    @NotBlank(message = "聊天组织不能为空!")
    private String orgid;
}
