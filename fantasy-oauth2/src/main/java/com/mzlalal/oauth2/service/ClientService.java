package com.mzlalal.oauth2.service;

import com.mzlalal.base.entity.oauth2.dto.ClientEntity;
import com.mzlalal.base.service.BaseService;

/**
 * service
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:47
 */
public interface ClientService extends BaseService<ClientEntity> {

    /**
     * 根据clientKey获取
     *
     * @param clientKey 客户端Key
     * @return ClientEntity
     */
    ClientEntity getOneByClientKey(String clientKey);
}
