package com.mzlalal.base.entity.oss.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 尺墨飞虹订阅表DTO
 *
 * @author Mzlalal
 * @date 2022-05-29 21:29:53
 */
@Data
@Builder
@ApiModel("飞虹订阅列表DTO")
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_diary_subscribe", resultMap = "diarySubscribeMap")
public class DiarySubscribeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("订阅用户ID")
    private String subscribeUserId;

    @ApiModelProperty("订阅用户名")
    @TableField(exist = false)
    private String subscribeUsername;

    @ApiModelProperty("粉丝用户名")
    @TableField(exist = false)
    private String followerUsername;

    @ApiModelProperty("订阅状态,例:0=未订阅,1=订阅待确认,2=订阅被拒绝,3=已订阅,4=取消订阅")
    private Integer subscribeStatus;

    @ApiModelProperty("创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createName;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新人ID")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
