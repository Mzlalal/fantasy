package com.mzlalal.oauth2.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.component.VueSelect;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.AssertUtil;
import com.mzlalal.base.util.FantasyPage;
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
    public FantasyPage<UserEntity> queryPage(Po<UserEntity> po) {
        // 查询参数
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        String mail = po.getEntity().getMail();
        String mobile = po.getEntity().getMobile();
        String username = po.getEntity().getUsername();
        // 都不为空
        if (StrUtil.isAllNotBlank(mail, mobile, username)) {
            // like查询
            wrapper.like("mobile", mobile).or().like("mail", mail).or().like("username", username);
        }
        // 创建分页条件
        Page<UserEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<UserEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
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
    public void verifyWhenSave(UserEntity user) {
        // 验证手机号格式是否正确
        GrantResponseEnum.PASSWORD.verifyUsername(user.getMobile());
        // 校验邮箱是否被注册
        AssertUtil.isFalse(this.queryExistByMobile(user.getMobile()), GlobalResult.MOBILE_HAS_BEEN_REGISTERED);
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 邮箱
        String mail = user.getMail();
        // 邮箱选填,没有邮箱就填充默认值(在待办,纪念日中需要邮箱,做个伪必填)
        if (StrUtil.isBlank(mail)) {
            // 默认使用雪花ID填充值
            user.setMail(IdUtil.getSnowflake().nextIdStr());
            return;
        }
        // 验证邮箱
        GrantResponseEnum.MAIL.verifyUsername(mail);
        // 校验邮箱是否被注册
        AssertUtil.isFalse(this.queryExistByMail(mail), GlobalResult.MAIL_HAS_BEEN_REGISTERED);
    }

    @Override
    public void verifyWhenUpdate(UserEntity user) {
        // 旧的用户信息
        UserEntity oldUser = Oauth2Context.getElseThrow();
        // 旧的用户名
        String oldUsername = oldUser.getUsername();
        // 新的用户名
        String newUsername = user.getUsername();
        // 校验用户名
        AssertUtil.notBlank(newUsername, "用户名不能为空");
        AssertUtil.isTrue(newUsername.length() < 9, "用户名长度需要在1和8之间");
        // 如果名字相同,则不修改
        if (StrUtil.equals(newUsername, oldUsername)) {
            user.setUsername(null);
        }
        // 旧的手机号
        String oldMobile = oldUser.getMobile();
        // 新的手机号
        String newMobile = user.getMobile();
        // 验证手机号格式是否正确
        GrantResponseEnum.PASSWORD.verifyUsername(newMobile);
        // 如果名字相同,则不修改
        if (StrUtil.equals(oldMobile, newMobile)) {
            user.setMobile(null);
        } else {
            // 校验邮箱是否被注册
            AssertUtil.isFalse(this.queryExistByMobile(newMobile), GlobalResult.MOBILE_HAS_BEEN_REGISTERED);
        }

        // 如果密码填写了,则加密
        if (StrUtil.isNotBlank(user.getPassword())) {
            // 密码加密
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 为空则不需要修改
            user.setPassword(null);
        }

        // 旧的邮箱
        String oldMail = oldUser.getMail();
        // 新的邮箱
        String newMail = user.getMail();
        // 邮箱选填,没有邮箱就填充默认值(在待办,纪念日中需要邮箱,做个伪必填)
        if (StrUtil.isBlank(newMail)) {
            // 默认使用雪花ID填充值
            user.setMail(IdUtil.getSnowflake().nextIdStr());
            return;
        }
        // 验证邮箱是否正确
        GrantResponseEnum.MAIL.verifyUsername(newMail);
        // 如果邮箱相同,则不修改
        if (StrUtil.equals(oldMail, newMail)) {
            user.setMail(null);
        } else {
            // 校验邮箱是否被注册
            AssertUtil.isFalse(this.queryExistByMail(newMail), GlobalResult.MAIL_HAS_BEEN_REGISTERED);
        }
    }
}