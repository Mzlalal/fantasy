package com.mzlalal.oauth2.controller;

import cn.hutool.core.collection.CollUtil;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.ClientEntity;
import com.mzlalal.base.feign.client.ClientFeignApi;
import com.mzlalal.oauth2.service.ClientService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端controller
 *
 * @author Mzlalal
 * @date 2021-08-23 09:55:38
 */
@Api(tags = "客户端")
@RestController
@RequestMapping("/api/v1/oauth/client")
public class ClientController implements ClientFeignApi {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Result<ClientEntity> list(@RequestBody Po<ClientEntity> po) {
        return Result.ok(clientService.queryPage(po));
    }

    @Override
    public Result<ClientEntity> info(@PathVariable("id") Long id) {
        return Result.ok(clientService.getById(id));
    }

    @Override
    public Result<Void> save(@RequestBody ClientEntity client) {
        return clientService.save(client) ? Result.ok() : Result.fail();
    }

    @Override
    public Result<Void> update(@RequestBody ClientEntity client) {
        return clientService.updateById(client) ? Result.ok() : Result.fail();
    }

    @Override
    public Result<Void> delete(@RequestBody String[] ids) {
        return clientService.removeByIds(CollUtil.newArrayList(ids)) ? Result.ok() : Result.fail();
    }

}
