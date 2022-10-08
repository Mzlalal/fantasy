package com.mzlalal.base.entity.chess.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 结算完成的房间DTO
 *
 * @author Mzlalal
 * @date 2022-02-22 11:23:57
 */
@Data
@Builder
@ApiModel("结算完成的房间")
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_room_history", autoResultMap = true)
public class RoomHistoryEntity implements Serializable {
    private static final long serialVersionUID = -5719743286587838808L;

    /**
     * ID(用户ID)
     */
    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("房间名")
    private String roomName;

    @ApiModelProperty("房间内的选手ID集合")
    private String playerIdSet;

    @ApiModelProperty("房间内的选手状态集合")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<RoomPlayerEntity> playerStatusSet;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
