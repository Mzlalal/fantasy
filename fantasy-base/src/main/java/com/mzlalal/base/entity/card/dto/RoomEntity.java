package com.mzlalal.base.entity.card.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_room")
@EqualsAndHashCode(callSuper = true)
public class RoomEntity extends BaseEntity {
    private static final long serialVersionUID = -3290488096046399525L;

    /**
     * ID
     */
    @TableId
    @ApiModelProperty("ID")
    private String id;
    /**
     * 房间名
     */
    @ApiModelProperty("房间名")
    private String name;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

}
