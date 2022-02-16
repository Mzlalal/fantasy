package com.mzlalal.base.entity.card.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 房间内的选手信息
 *
 * @author Mzlalal
 * @date 2022-02-10 17:11:39
 */
@Data
@Builder
@ApiModel("房间内的选手信息")
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_room_player")
@EqualsAndHashCode(callSuper = true)
public class RoomPlayerEntity extends BaseEntity {
    private static final long serialVersionUID = 2204355865868864707L;

    /**
     * ID
     */
    @TableId
    @ApiModelProperty("ID")
    private String id;
    /**
     * 选手名
     */
    @ApiModelProperty("选手名")
    private String playerName;
    /**
     * 选手状态=0-下桌结算,1-上桌
     */
    @ApiModelProperty("选手状态=0-下桌,1-上桌")
    private String playerStatus;
    /**
     * 选手积分
     */
    @ApiModelProperty("选手积分")
    private Integer playerScore;
    /**
     * 房间ID
     */
    @ApiModelProperty("房间ID")
    private String roomId;
    /**
     * 房间名
     */
    @ApiModelProperty("房间名")
    private String roomName;

}
