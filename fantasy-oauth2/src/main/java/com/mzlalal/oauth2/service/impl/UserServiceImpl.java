package com.mzlalal.oauth2.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oauth2.UserEntity;
import com.mzlalal.base.util.Page;
import com.mzlalal.oauth2.dao.UserDao;
import com.mzlalal.oauth2.service.UserService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

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
        // 异步查询总行数 selectList一定要在future之后
        Future<Integer> future = ThreadUtil.execAsync(() -> baseMapper.selectCount(wrapper));
        // 查询结果集
        List<UserEntity> entityList = baseMapper.selectList(wrapper);
        // 获取总行数结果
        Integer count = this.getTotalResult(future, log);
        // 返回结果
        return new Page<>(entityList, count, po.getPageInfo());
    }

    @Override
    public Optional<UserEntity> findOneByMobile(@NotBlank(message = "手机号不能为空") String mobile) {
        return baseMapper.findOneByMobile(mobile);
    }

    @Override
    public Optional<UserEntity> findOneByMail(@NotBlank(message = "邮件不能为空") String mail) {
        return baseMapper.findOneByMail(mail);
    }

    @Override
    public boolean existByMail(@NotBlank(message = "邮件不能为空") String mail) {
        return baseMapper.existByMail(mail);
    }

    @Override
    public boolean updateAccessTokenById(@NotNull(message = "用户ID不能为空") Long id, UserEntity userEntity) {
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        return this.update(userEntity, updateWrapper);
    }
}
