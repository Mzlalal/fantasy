package com.mzlalal.base.entity.oss.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 纪念日DTO
 *
 * @author Mzlalal
 * @date 2022-03-23 19:39:24
 */
@Data
@Builder
@ApiModel("纪念日DTO")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_day_matter")
public class DayMatterEntity implements Serializable {
    private static final long serialVersionUID = 4081398648210481074L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("备注")
    private String matterMemo;

    @ApiModelProperty("事件日期")
    private Date matterDate;

    @ApiModelProperty("提醒间隔")
    private Integer matterInterval;

    @ApiModelProperty("置顶状态")
    private String matterTopStatus;

    @ApiModelProperty("与这些人分享")
    private String matterShareSet;

    @ApiModelProperty("是否删除")
    private Integer isHide;

    @ApiModelProperty("创建人ID")
    private String createBy;

    @ApiModelProperty("创建人")
    private String createName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人ID")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("更新人")
    private String updateName;

}
