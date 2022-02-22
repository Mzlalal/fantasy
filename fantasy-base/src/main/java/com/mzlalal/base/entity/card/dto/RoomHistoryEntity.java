package com.mzlalal.base.entity.card.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@TableName(value = "t_room_history" , autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
public class RoomHistoryEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    @ApiModelProperty("ID")
    private String id;
    /**
     * 房间内的选手ID集合
     */
    @ApiModelProperty("房间内的选手ID集合")
    private String playerIdSet;
    /**
     * 房间内的选手状态
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty("房间内的选手状态集合")
    private List<RoomPlayerEntity> playerStatusSet;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;
}
