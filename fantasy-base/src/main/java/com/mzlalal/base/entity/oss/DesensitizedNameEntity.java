package com.mzlalal.base.entity.oss;

import com.mzlalal.base.entity.global.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * 人名脱敏参数实体
 *
 * @author Mzlalal
 * @date 2022/1/18 16:38
 */
@Data
@ApiModel("人名脱敏参数")
@EqualsAndHashCode(callSuper = true)
public class DesensitizedNameEntity extends BaseEntity {

    @NotBlank
    @ApiModelProperty(value = "从excel复制的列数据", required = true)
    private String name;

    @Range(min = 0, max = 10)
    @ApiModelProperty(value = "脱敏符号数量,最大10,最小0,默认为3,例: 2->李**; 3->李***", example = "3", required = true, position = 1)
    private int numberOfSymbol = 3;

    @NotBlank
    @ApiModelProperty(value = "脱敏符号,例:*", example = "*", required = true, position = 2)
    private String symbol = "*";

    @Range(min = 0, max = 100)
    @ApiModelProperty(value = "姓名最大长度为N,最大100,最小0,默认为5,超过此数量按机构处理", example = "5", required = true, position = 3)
    private int maxNumberOfName = 5;

    /**
     * 生成根据参数配置的脱敏符号
     *
     * @return string
     */
    public String processReplaceSymbol() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfSymbol; i++) {
            sb.append(symbol);
        }
        return sb.toString();
    }
}
