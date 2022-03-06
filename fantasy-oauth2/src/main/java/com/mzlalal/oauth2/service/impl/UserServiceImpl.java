package com.mzlalal.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.util.Page;
import com.mzlalal.oauth2.dao.UserDao;
import com.mzlalal.oauth2.service.UserService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

/**
 * ServiceImpl
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:48
 */
@Service("userServiceImpl")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Override
    public Page<UserEntity> queryPage(Po<UserEntity> po) {
        // 查询参数
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>(po.getEntity());
        // 创建分页条件
        com.github.pagehelper.Page<UserEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<UserEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new Page<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    @Override
    public Optional<UserEntity> queryOneByMobile(@NotBlank(message = "手机号不能为空") String mobile) {
        return baseMapper.queryOneByMobile(mobile);
    }

    @Override
    public Optional<UserEntity> queryOneByMail(@NotBlank(message = "邮件不能为空") String mail) {
        return baseMapper.queryOneByMail(mail);
    }

    @Override
    public boolean queryExistByMail(@NotBlank(message = "邮件不能为空") String mail) {
        return baseMapper.queryExistByMail(mail);
    }

    @Override
    public boolean updateAccessTokenById(@NotBlank(message = "用户ID不能为空") String id, UserEntity userEntity) {
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        return this.update(userEntity, updateWrapper);
    }
}
