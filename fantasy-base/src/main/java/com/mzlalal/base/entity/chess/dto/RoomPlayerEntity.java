package com.mzlalal.base.entity.chess.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class RoomPlayerEntity implements Serializable {
    private static final long serialVersionUID = 2204355865868864707L;

    /**
     * ID(用户ID)
     */
    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("选手名")
    private String playerName;

    @ApiModelProperty("选手状态=0-下桌,1-上桌")
    private String playerStatus;

    @ApiModelProperty("选手积分")
    private Integer playerScore;

    @ApiModelProperty("房间ID")
    private String roomId;

    @ApiModelProperty("房间名")
    private String roomName;

}
