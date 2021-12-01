package com.mzlalal.base.environment;

import cn.hutool.core.map.MapUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * banner参数设置
 *
 * @author Mzlalal
 * @date 2021/7/5 22:36
 **/
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BannerEnvProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // banner配置
        Map<String, Object> bannerConfig = MapUtil.newHashMap();

        // JVM参数信息
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        bannerConfig.put("jvm.memory.total", totalMemory);
        bannerConfig.put("jvm.memory.max", maxMemory);

        // 添加到环境中
        environment.getPropertySources()
                .addLast(new MapPropertySource("banner.config", bannerConfig));
    }
}
