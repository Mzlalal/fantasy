package com.mzlalal.oss.service;

import com.mzlalal.base.entity.oauth2.ClientEntity;
import com.mzlalal.base.service.BaseService;

/**
 * service
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:47
 */
public interface ClientService extends BaseService<ClientEntity> {

    /**
     * 根据client id获取
     *
     * @param clientId 客户端ID
     * @return ClientEntity
     */
    ClientEntity getOneByClientId(String clientId);
}
