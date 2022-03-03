package com.mzlalal.base.entity.chess.req;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

/**
 * 转账分数请求
 *
 * @author Mzlalal
 * @date 2022/2/12 10:53
 */
@Data
@Builder
@ApiModel("转账分数请求")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransferScoreReq extends BaseEntity {
    private static final long serialVersionUID = 2086775977074489498L;

    @NotBlank(message = "房间ID不能为空")
    @ApiModelProperty("房间ID")
    private String roomId;

    @NotBlank(message = "转账发起人不能为空")
    @ApiModelProperty("转账发起人")
    private String from;

    @NotBlank(message = "转账接收人不能为空")
    @ApiModelProperty("转账接收人")
    private String to;

    @Digits(integer = 4, fraction = 0, message = "转账数额只能为1到9999之间的正整数")
    @ApiModelProperty("转账数额")
    private Integer change;

    @NotBlank(message = "转账消息不能为空")
    @ApiModelProperty("转账消息")
    private String message;
}
