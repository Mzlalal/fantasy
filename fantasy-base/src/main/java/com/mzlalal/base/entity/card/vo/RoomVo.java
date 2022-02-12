package com.mzlalal.base.entity.card.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * VO
 *
 * @author Mzlalal
 * @date 2022-02-11 09:18:06
 */
@Data
@ApiModel("Vo")
public class RoomVo implements Serializable {
    private static final long serialVersionUID = -191205199168415856L;

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private String id;
    /**
     * 房间名
     */
    @ApiModelProperty("房间名")
    private String name;
    /**
     * 在线人数
     */
    @ApiModelProperty("在线人数")
    private Integer online;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

}
