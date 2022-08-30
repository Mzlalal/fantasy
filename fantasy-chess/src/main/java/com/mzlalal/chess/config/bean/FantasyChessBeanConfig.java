package com.mzlalal.chess.config.bean;

import com.mzlalal.base.config.bean.FantasyBaseBeanConfig;
import com.mzlalal.base.entity.chess.vo.HistoryMessageVo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * chess类加载注册
 *
 * @author Mzlalal
 * @date 2021/9/24 11:29
 */
@Configuration
public class FantasyChessBeanConfig extends FantasyBaseBeanConfig {

    /**
     * redisTemplate存储HistoryMessageVo对象
     *
     * @param redisConnectionFactory redis连接工厂
     * @return RedisTemplate<String, HistoryMessageVo>
     */
    @Bean("redisTemplateStoreHistoryMessage")
    public RedisTemplate<String, HistoryMessageVo> redisTemplateStoreHistoryMessage(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, HistoryMessageVo> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<HistoryMessageVo> jacksonSerialize = new Jackson2JsonRedisSerializer<>(HistoryMessageVo.class);
        // 创建redis模板通用操作
        this.createRedisTemplate(redisTemplate, jacksonSerialize);
        // 返回redis模板
        return redisTemplate;
    }
}
