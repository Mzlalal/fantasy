package com.mzlalal.oauth2.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.oauth2.ClientEntity;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.oauth2.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 校验客户端请求的授权方式是否存在
 *
 * @author Mzlalal
 * @date 2022/1/24 22:17
 */
@Service("clientVerifyResponseTypeService")
public class ClientVerifyResponseTypeService {
    /**
     * 客户端
     */
    private final ClientService clientService;

    @Autowired
    public ClientVerifyResponseTypeService(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * 根据客户端ID查询响应类型是否存在
     * 1. 根据客户端ID查询
     * 2. 根据","切割客户端授权方式
     * 3. 比对参数授权方式是否存在切割数组中
     * 4. 返回客户端
     *
     * @param clientId     客户端ID
     * @param responseType 授权方式
     * @return ClientEntity
     */
    public ClientEntity verifyResponseType(String clientId, String responseType) {
        // 客户端ID是否为空
        AssertUtil.notBlank(clientId, GlobalResult.OAUTH_FAIL);
        // 授权方式是否为空
        AssertUtil.notBlank(responseType, GlobalResult.RESPONSE_TYPE_NOT_CORRECT);
        // 检查客户端
        ClientEntity client = clientService.getOneByClientId(clientId);
        AssertUtil.notNull(client, GlobalResult.OAUTH_FAIL);
        // 获取授权方式
        String[] grantTypeArray = StrUtil.splitToArray(client.getResponseType(), ",");
        // 不存在数组中则抛出异常
        AssertUtil.arrayNotContains(grantTypeArray, responseType, GlobalResult.OAUTH_RESPONSE_TYPE_NOT_CORRECT);
        // 返回client
        return client;
    }
}
