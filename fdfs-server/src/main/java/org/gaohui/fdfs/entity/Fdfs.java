package org.gaohui.fdfs.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author gaohui
 * @since 2021-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_fdfs")
@ApiModel(value="Fdfs对象", description="")
public class Fdfs implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增主键")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createby;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createtime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatetime;

    @ApiModelProperty(value = "是否删除0false 1true")
    @TableLogic
    private Boolean flag;

    @ApiModelProperty(value = "存储路径")
    private String path;

    @ApiModelProperty(value = "文件大小")
    private BigDecimal size;

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "文件后缀")
    private String suffix;

    @ApiModelProperty(value = "文件类型")
    private String type;

    @ApiModelProperty(value = "图片宽度")
    private String width;

    @ApiModelProperty(value = "图片高度")
    private String height;

}
