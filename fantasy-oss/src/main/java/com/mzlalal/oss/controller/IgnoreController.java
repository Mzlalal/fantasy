package com.mzlalal.oss.controller;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.entity.oss.DesensitizedNameEntity;
import com.mzlalal.base.util.AssertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工具
 *
 * @author Mzlalal
 * @date 2022/01/18 16:32
 */
@Slf4j
@Api(tags = "工具")
@Validated
@RestController
@RequestMapping("/ignore/api/v1/toolkit")
public class IgnoreController {

    @ApiOperation("根据空格分割人名并脱敏")
    @PostMapping(value = "/replaceNameBySpace", headers = "content-type=application/x-www-form-urlencoded")
    public String replaceNameBySpace(@ModelAttribute DesensitizedNameEntity desensitizedEntity) {
        // 验空
        AssertUtil.notBlank(desensitizedEntity.getName(), "数据为空,请从excel对应列复制数据");

        // 切割数据
        List<String> nameList = StrUtil.split(desensitizedEntity.getName(), StrUtil.SPACE);
        AssertUtil.notEmpty(nameList, "数据为空,请从excel对应列复制数据");

        // 脱敏符号
        String replaceSymbol = desensitizedEntity.processReplaceSymbol();
        // 返回结果
        StringBuilder sb = new StringBuilder();
        // 遍历姓名
        for (String item : nameList) {
            // 姓名最大长度
            if (StrUtil.length(item) > desensitizedEntity.getMaxNumberOfName()) {
                sb.append(item).append("\n");
                continue;
            }
            // 脱敏名字->李**
            String desensitized = DesensitizedUtil.desensitized(item, DesensitizedUtil.DesensitizedType.CHINESE_NAME);
            // 替换hutool返回的*,后追加自定义脱敏符号
            sb.append(StrUtil.replace(desensitized, "*", "")).append(replaceSymbol).append("\n");
        }
        return sb.toString();
    }
}
