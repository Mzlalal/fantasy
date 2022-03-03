package com.mzlalal.base.entity.chess.req;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * 房间内的选手下桌请求
 *
 * @author Mzlalal
 * @date 2022/2/16 10:53
 */
@Data
@Builder
@ApiModel("房间内的选手下桌请求")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlayerOutOrJoinRoomReq extends BaseEntity {
    private static final long serialVersionUID = 4964848440556470458L;

    @NotBlank(message = "房间ID不能为空")
    @ApiModelProperty("房间ID")
    String roomId;

    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    String userId;

    @NotBlank(message = "用户名称不能为空")
    @ApiModelProperty("用户名称")
    String username;
}
