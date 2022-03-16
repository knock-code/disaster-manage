package org.gaohui.message.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gaohui  2022/03/10
 */
@Data
public class HelpInfoVO {
    private String id;
    /**
     * 发布时间
     */
    private Date createtime;
    /**
     * 创建人
     */
    private String createby;
    /**
     * 求助内容
     */
    private String content;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 求助类型
     */
    private String[] type;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * 是否完成
     */
    private Boolean isfinish;
    /**
     * 是否置顶
     */
    private Boolean istop;
    /**
     * 所属灾害id
     */
    private String disasterid;
    /**
     * 求助标题
     */
    private String title;
}
