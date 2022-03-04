package com.mzlalal.base.entity.chess.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 房间DTO
 *
 * @author Mzlalal
 * @date 2022-02-11 09:18:06
 */
@Data
@Builder
@ApiModel("房间")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_room")
public class RoomEntity implements Serializable {
    private static final long serialVersionUID = -3290488096046399525L;

    @TableId
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("房间号")
    private Integer code;

    @ApiModelProperty("房间名")
    private String name;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
