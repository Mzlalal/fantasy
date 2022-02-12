package com.mzlalal.base.entity.card.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 房间内的选手VO
 *
 * @author Mzlalal
 * @date 2022-02-10 17:11:39
 */
@Data
@ApiModel("房间内的选手VO")
public class RoomPlayerVo implements Serializable {
    private static final long serialVersionUID = -3123926843031753167L;

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private String id;
    /**
     * 选手名
     */
    @ApiModelProperty("选手名")
    private String playName;
    /**
     * 选手状态=0-下桌结算,1-上桌
     */
    @ApiModelProperty("选手状态=0-下桌结算,1-上桌")
    private String playStatus;
    /**
     * 选手积分
     */
    @ApiModelProperty("选手积分")
    private Integer playScore;
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
