package com.mzlalal.oauth2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 用户查询
 *
 * @author Mzlalal
 * @date 2021-05-20 21:53:44
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    /**
     * 根据手机号码获取用户信息
     *
     * @param mobile 手机号
     * @return UserEntity
     */
    Optional<UserEntity> queryOneByMobile(@Param("mobile") String mobile);

    /**
     * 根据邮箱获取用户信息
     *
     * @param mail 邮箱
     * @return UserEntity
     */
    Optional<UserEntity> queryOneByMail(@Param("mail") String mail);

    /**
     * 根据邮箱判断用户是否存在
     *
     * @param mail 邮箱
     * @return 1存在 0 不存在
     */
    boolean queryExistByMail(@Param("mail") String mail);
}
