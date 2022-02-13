package com.mzlalal.base.entity.card.req;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * 查询房间选手的历史消息
 *
 * @author Mzlalal
 * @date 2022/2/12 10:53
 */
@Data
@Builder
@ApiModel("查询房间选手的历史消息")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlayerHistoryMessageReq extends BaseEntity {
    private static final long serialVersionUID = 4964848440456470458L;

    @NotBlank(message = "房间ID不能为空")
    @ApiModelProperty("房间ID")
    private String roomId;

    @NotBlank(message = "选手不能为空")
    @ApiModelProperty("选手ID")
    private String from;
}
