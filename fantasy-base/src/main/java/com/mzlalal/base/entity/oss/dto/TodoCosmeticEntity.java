package com.mzlalal.base.entity.oss.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 化妆品待办DTO
 *
 * @author Mzlalal
 * @date 2026-01-03 13:33:07
 */
@Data
@ApiModel("化妆品待办DTO")
@TableName("t_todo_cosmetic")
@EqualsAndHashCode(callSuper = true)
public class TodoCosmeticEntity extends TodoNotifyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "化妆品名称不能为空")
    @ApiModelProperty("化妆品名称")
    private String cosmeticName;

    @NotBlank(message = "库存不能为空")
    @ApiModelProperty("库存")
    private String cosmeticStock;
}