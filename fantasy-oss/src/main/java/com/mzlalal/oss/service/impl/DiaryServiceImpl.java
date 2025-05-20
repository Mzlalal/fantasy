package com.mzlalal.oss.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.global.po.Po;
import com.mzlalal.base.entity.oss.dto.DiaryEntity;
import com.mzlalal.base.entity.oss.vo.DiaryStatVo;
import com.mzlalal.base.oauth2.Oauth2Context;
import com.mzlalal.base.util.FantasyPage;
import com.mzlalal.oss.dao.DiaryDao;
import com.mzlalal.oss.service.DiaryService;
import com.mzlalal.oss.service.DiarySubscribeService;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ServiceImpl
 *
 * @author Mzlalal
 * @date 2022-04-28 20:08:41
 */
@Service("diaryServiceImpl")
@AllArgsConstructor
public class DiaryServiceImpl extends ServiceImpl<DiaryDao, DiaryEntity> implements DiaryService {

    /**
     * 订阅service
     */
    private final DiarySubscribeService diarySubscribeService;

    /**
     * 根据 PagePara 查询分页
     *
     * @param po 分页参数
     * @return 分页信息
     */
    @Override
    public FantasyPage<DiaryEntity> queryPage(Po<DiaryEntity> po) {
        String diaryContent = po.getEntity().getDiaryContent();
        // 创建分页条件
        Page<DiaryEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询结果集
        List<DiaryEntity> entityList = baseMapper.selectList(Wrappers.<DiaryEntity>lambdaQuery()
            .like(StrUtil.isNotBlank(diaryContent), DiaryEntity::getDiaryContent, diaryContent)
            .orderByAsc(DiaryEntity::getDiaryDate));
        // 返回结果
        return new FantasyPage<>(entityList, pageResult.getTotal(), po.getPageInfo());
    }

    /**
     * 查询飞虹根据日期分组
     *
     * @param po 查询条件
     * @return Result<Map < Date, List < DiaryEntity>>>
     */
    @Override
    public Result<Map<String, List<DiaryEntity>>> queryDiaryGroupByDate(Po<DiaryEntity> po) {
        // 搜索日记内容
        String diaryContent = po.getEntity().getDiaryContent();
        // 我订阅的用户列表
        List<String> subscribeUserIdList = diarySubscribeService.queryMySubscribeUserIdList();
        // 添加自己的用户ID
        subscribeUserIdList.add(Oauth2Context.getUserIdElseThrow());
        // 创建分页条件
        Page<DiaryEntity> pageResult = this.createPageQuery(po.getPageInfo());
        // 查询最近时间
        List<Date> recentDate = baseMapper.queryRecentDate(subscribeUserIdList, diaryContent);
        if (CollUtil.isEmpty(recentDate)) {
            return Result.ok(MapUtil.newHashMap());
        }
        // 查询包括我自己的
        subscribeUserIdList.add(Oauth2Context.getUserIdElseThrow());
        // 查询结果集
        List<DiaryEntity> entityList = baseMapper.selectList(Wrappers.<DiaryEntity>lambdaQuery()
            // 订阅用户列表(包括自己)
            .in(DiaryEntity::getCreateBy, subscribeUserIdList)
            // 大于等于当前分页最小的时间
            .ge(DiaryEntity::getDiaryDate, CollUtil.min(recentDate))
            // 搜索日记内容
            .like(StrUtil.isNotBlank(diaryContent), DiaryEntity::getDiaryContent, diaryContent)
            // 根据创建时间排序
            .orderByDesc(DiaryEntity::getDiaryDate)
            .orderByDesc(DiaryEntity::getCreateTime)
        );
        // 根据日期分组
        Map<String, List<DiaryEntity>> collect = entityList.stream()
            .collect(Collectors.groupingBy(DiaryEntity::getDiaryDateStr, LinkedHashMap::new, Collectors.toList()));
        // 返回结果
        Result<Map<String, List<DiaryEntity>>> result = Result.ok(collect);
        // 设置总页码/总行数
        result.getPage().setTotalPage(pageResult.getPages());
        result.getPage().setTotalCount(pageResult.getTotal());
        return result;
    }

    @Override
    public Result<List<Map<String, Object>>> queryDiaryContentStatistics() {
        // 我订阅的用户列表
        List<String> subscribeUserIdList = this.diarySubscribeService.queryMySubscribeUserIdList();
        // 添加自己的用户id
        subscribeUserIdList.add(Oauth2Context.getUserIdElseThrow());

        // 订阅用户列表的字符统计
        List<DiaryStatVo> contentStatistics = this.baseMapper.queryDiaryContentStatistics(subscribeUserIdList);

        // 计算总字符数
        long totalWordSum = contentStatistics.stream()
            .mapToLong(DiaryStatVo::getTotalWord)
            .sum();

        // 转换为目标格式
        List<Map<String, Object>> result = contentStatistics.stream()
            .map(stat -> {
                Map<String, Object> map = new HashMap<>();
                map.put("userName", stat.getUserName());
                // 计算百分比并保留2位小数
                double percent = totalWordSum > 0 ? stat.getTotalWord().doubleValue() / totalWordSum * 100 : 0;
                map.put("percent", Math.round(percent * 100) / 100.0);
                // 总字符数量
                map.put("totalWord", stat.getTotalWord());
                return map;
            })
            .collect(Collectors.toList());

        return Result.ok(CollUtil.sortByProperty(result, "percent"));
    }
}