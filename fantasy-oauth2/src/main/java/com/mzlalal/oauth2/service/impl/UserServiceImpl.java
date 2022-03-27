package com.mzlalal.oauth2.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.Page;
import com.mzlalal.oauth2.dao.UserDao;
import com.mzlalal.oauth2.service.UserService;
import com.mzlalal.oauth2.service.oauth2.GrantResponseEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 用户ServiceImpl
 *
 * @author Mzlalal
 * @date 2021-07-29 20:36:48
 */
@Service("userServiceImpl")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    /**
     * 密码加密操作
     */
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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
    public Optional<UserEntity> queryOneByMobile(String mobile) {
        return baseMapper.queryOneByMobile(mobile);
    }

    @Override
    public Optional<UserEntity> queryOneByMail(String mail) {
        return baseMapper.queryOneByMail(mail);
    }

    @Override
    public boolean queryExistByMobile(String mobile) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        return baseMapper.exists(queryWrapper);
    }

    @Override
    public boolean queryExistByMail(String mail) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mail", mail);
        return baseMapper.exists(queryWrapper);
    }

    @Override
    public boolean updateAccessTokenById(String id, UserEntity userEntity) {
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        return this.update(userEntity, updateWrapper);
    }

    @Override
    public List<VueSelect> queryUserIdVueSelectListByUsername(String username) {
        return baseMapper.queryUserIdVueSelectListByUsername(username);
    }

    @Override
    public List<VueSelect> queryUserMailVueSelectListByUsername(String username) {
        return baseMapper.queryUserMailVueSelectListByUsername(username);
    }

    @Override
    public void verifyWhenSaveOrUpdate(UserEntity user) {
        // 验证手机号格式是否正确
        GrantResponseEnum.PASSWORD.verifyUsername(user.getMobile());
        // 校验邮箱是否被注册
        AssertUtil.isFalse(this.queryExistByMobile(user.getMobile()), GlobalResult.MOBILE_HAS_BEEN_REGISTERED);
        // 验证邮箱是否正确
        if (StrUtil.isNotBlank(user.getMail())) {
            GrantResponseEnum.MAIL.verifyUsername(user.getMail());
            // 校验邮箱是否被注册
            AssertUtil.isFalse(this.queryExistByMail(user.getMail()), GlobalResult.MAIL_HAS_BEEN_REGISTERED);
        } else {
            // 默认使用雪花ID填充值
            user.setMail(IdUtil.getSnowflake().nextIdStr());
        }
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
