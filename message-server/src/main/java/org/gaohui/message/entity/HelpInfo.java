package org.gaohui.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gaohui.common.pojo.BaseEntity;

/**
 * @author gaohui  2022/03/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_help_info")
public class HelpInfo extends BaseEntity {
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
    private String type;
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
