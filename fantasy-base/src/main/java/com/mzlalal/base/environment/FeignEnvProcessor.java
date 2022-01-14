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
import java.util.concurrent.CopyOnWriteArrayList;

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
    /**
     * 本地已启动的服务ID列表
     */
    private static final List<String> localStartedServiceIdList = new CopyOnWriteArrayList<>();
    /**
     * feign的端口配置(feign-service.properties)
     */
    private static Properties feignProperties;

    /**
     * 1.获取需要动态调整feign url的服务列表
     * 2.根据服务列表查询本地服务的swagger地址,判断服务其否启动
     * 3.先设置feign url为动态调整后的值(若服务遍历serviceId为自身服务则直接设置动态url然后跳过)
     * 4.若swagger访问不到则替换为空(为空时使用feign的负载均衡访问服务列表中的服务)
     * 5.添加本地启动的服务至localStartedServiceIdList,给网关使用
     * 6.feign url添加到环境变量中提供给spring容器试用
     *
     * @param environment 环境
     * @param application 应用
     */
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
        feignProperties = PropertiesLoaderUtils.loadAllProperties("feign-service.properties");
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
            // 为空跳过
            if (StrUtil.isBlank(servicePort)) {
                System.out.println("服务ID:" + serviceId + "未设置feign访问端口(请查看feign-service.properties)");
                return;
            }
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
            // 丢弃异常
            try {
                // 发送请求,1.5秒超时
                HttpResponse response = HttpRequest.get(serviceRequestUrl).timeout(1500).execute();
                // 查看返回
                if (response != null && response.isOk()) {
                    // 添加到已启动的本地服务列表
                    localStartedServiceIdList.add(serviceId);
                    // 打印信息
                    System.out.println(StrUtil.format("^_^本地{}服务已启动，当前应用可以和{}微服务本地调试。{} -> {}"
                            , serviceId, serviceId, serviceId, feignServiceConfigValue));
                }
            } catch (Exception ignored) {
                // 微服务未启动,覆盖为空
                feignConfig.put(feignServiceConfigKey, "");
                // 打印信息
                System.out.println((StrUtil.format("-_-本地{}服务没有启动，当前应用无法和{}微服务调试。", serviceId, serviceId)));
            }
        });
        // 添加到环境中
        environment.getPropertySources()
                .addLast(new MapPropertySource("feign.config", feignConfig));
    }

    /**
     * 获取服务列表ID集合
     *
     * @return List<String> (CopyOnWriteArrayList)
     */
    public static List<String> getLocalStartedServiceIdList() {
        return localStartedServiceIdList;
    }

    /**
     * 根据服务ID获取feign的端口配置(feign-service.properties)
     *
     * @param serviceId 服务ID
     * @return String
     */
    public static String getFeignPort(String serviceId) {
        return feignProperties.getProperty(serviceId + ".service.port");
    }
}