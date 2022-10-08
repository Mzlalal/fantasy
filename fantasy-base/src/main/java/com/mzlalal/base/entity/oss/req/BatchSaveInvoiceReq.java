package com.mzlalal.base.entity.oss.req;

import com.mzlalal.base.common.GlobalConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 批量保存发票请求
 *
 * @author Mzlalal
 * @date 2022/10/15 23:58
 **/
@Data
@ApiModel("批量保存发票请求")
public class BatchSaveInvoiceReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发票名称")
    private String invoiceName;

    @ApiModelProperty("发票文件扩展类型")
    @NotBlank(message = "发票文件扩展类型不能为空")
    private String invoiceExtName;

    @ApiModelProperty("发票源地址")
    @NotBlank(message = "发票源地址不能为空")
    @Pattern(regexp = GlobalConstant.URL_REG_EXP, message = "存在发票源地址不是一个合法的URL")
    private String invoiceSourceUrl;

    @ApiModelProperty("返回提示")
    private String tip;
}
