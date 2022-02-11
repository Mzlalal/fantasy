package com.mzlalal.oauth2.service;

import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.service.BaseService;

import java.util.Optional;

/**
 * service
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:48
 */
public interface UserService extends BaseService<UserEntity> {
    /**
     * 根据手机号码获取用户信息
     *
     * @param mobile 手机号
     * @return UserEntity
     */
    Optional<UserEntity> findOneByMobile(String mobile);

    /**
     * 根据邮箱获取用户信息
     *
     * @param mail 邮箱
     * @return UserEntity
     */
    Optional<UserEntity> findOneByMail(String mail);

    /**
     * 根据邮箱判断用户是否存在
     *
     * @param mail 邮箱
     * @return 1存在 0 不存在
     */
    boolean existByMail(String mail);

    /**
     * 根据ID更新用户的访问令牌
     *
     * @param id         用户ID
     * @param userEntity 用户信息
     * @return true 成功 false失败
     */
    boolean updateAccessTokenById(String id, UserEntity userEntity);
}
