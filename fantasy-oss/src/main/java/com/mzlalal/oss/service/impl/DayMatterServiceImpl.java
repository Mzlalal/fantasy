package com.mzlalal.oss.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.DayMatterEntity;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oss.dao.DayMatterDao;
import com.mzlalal.oss.service.DayMatterService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 纪念日ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-03-23 19:39:24
 */
@Service("dayMatterServiceImpl")
public class DayMatterServiceImpl extends ServiceImpl<DayMatterDao, DayMatterEntity> implements DayMatterService {

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public FantasyPage<DayMatterEntity> queryPage(Po<DayMatterEntity> po) {
        // 查询参数
        QueryWrapper<DayMatterEntity> wrapper = new QueryWrapper<>();
        // 根据用户邮箱查询
        String mail = Oauth2Context.getElseThrow().getMail();
        wrapper.apply(StrUtil.isNotBlank(mail), "FIND_IN_SET('" + mail + "', matter_mail_set)");
        // 排序
        wrapper.orderByDesc("update_time");
        // 创建分页条件
        Page<DayMatterEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<DayMatterEntity> entityList = baseMapper.selectList(wrapper);
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }
}