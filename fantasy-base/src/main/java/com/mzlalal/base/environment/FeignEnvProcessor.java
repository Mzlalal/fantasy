package com.mzlalal.base.environment;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Feign启动设置服务地址
 *
 * @author Mzlalal
 */
@Order(0)
public class FeignEnvProcessor implements EnvironmentPostProcessor {

    /**
     * 微服务配置的名字
     * 每个微服务对应一个配置: url = "${fantasy-oauth2.feign.service.config}"
     */
    private final String serviceConfigKey = "{}.feign.service.config";
    /**
     * 微服务配置的名字
     * 每个微服务对应一个配置: url = "${fantasy-oauth2.feign.service.config}" -> url = "http://localhost:9000/fantasy-oauth2"
     */
    private final String serviceConfigValue = "http://localhost:{}/{}";
    /**
     * 微服务swagger文档地址
     */
    private final String serviceSwaggerUrl = "http://localhost:{}/{}/doc.html";
    /**
     * 是否已经加载
     * 使用了bootstrap启动,导致EnvironmentPostProcessor执行两次
     * 如果加载过了,则改为true
     */
    private static boolean alreadyLoaded = false;

    @Override
    @SneakyThrows
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 是否加载
        if (alreadyLoaded) {
            return;
        }
        // 更改状态
        alreadyLoaded = true;
        // 获取运行环境
        String activeProfile = environment.getProperty("spring.profiles.active");
        // 应用名
        String applicationName = environment.getProperty("spring.application.name");
        // 生产环境
        String productProfile = "product";
        // 加载配置文件
        Properties feignProperties = PropertiesLoaderUtils.loadAllProperties("feign-service.properties");
        // 获取需要本地运行的微服务名称
        String serviceIds = feignProperties.getProperty("feign.service.ids");
        // 获取微服务名称切割成数组
        List<String> serviceIdList = StrUtil.split(serviceIds, ",");
        // Feign配置
        Map<String, Object> feignConfig = MapUtil.newHashMap(serviceIdList.size());
        // 遍历请求微服务swagger文档地址
        serviceIdList.parallelStream().forEach(serviceId -> {
            // 微服务端口
            String servicePort = feignProperties.getProperty(serviceId + ".service.port");
            // 微服务的URL指向配置名
            String feignServiceConfigKey = StrUtil.format(this.serviceConfigKey, serviceId);
            // 生产环境直接覆盖为空
            if (productProfile.equals(activeProfile)) {
                // 微服务未启动,覆盖为空
                feignConfig.put(feignServiceConfigKey, "");
                return;
            }
            // 微服务URL配置值
            String feignServiceConfigValue = StrUtil.format(this.serviceConfigValue, servicePort, serviceId);
            // 放入配置中,下方微服务未启动则覆盖为空
            feignConfig.put(feignServiceConfigKey, feignServiceConfigValue);
            // 微服务名相同直接设置
            if (StrUtil.equals(serviceId, applicationName)) {
                // 打印信息
                System.out.println(StrUtil.format("^_^本地{}服务已启动，当前应用可以和{}微服务本地调试。{}->{}"
                        , serviceId, serviceId, serviceId, feignServiceConfigValue));
                return;
            }
            // 请求swagger路径
            String serviceRequestUrl = StrUtil.format(serviceSwaggerUrl, servicePort, serviceId);
            // 发送请求,1.5秒超时
            HttpResponse response = null;
            // 丢弃异常
            try {
                response = HttpRequest.get(serviceRequestUrl).timeout(1500).execute();
            } catch (Exception ignored) {
                // 微服务未启动,覆盖为空
                feignConfig.put(feignServiceConfigKey, "");
                // 打印信息
                System.out.println((StrUtil.format("-_-本地{}服务没有启动，当前应用无法和{}微服务调试。", serviceId, serviceId)));
            }
            // 查看返回
            if (response != null && response.isOk()) {
                // 打印信息
                System.out.println(StrUtil.format("^_^本地{}服务已启动，当前应用可以和{}微服务本地调试。{} -> {}"
                        , serviceId, serviceId, serviceId, feignServiceConfigValue));
            }
        });
        // 添加到环境中
        environment.getPropertySources()
                .addLast(new MapPropertySource("feign.config", feignConfig));
    }
}