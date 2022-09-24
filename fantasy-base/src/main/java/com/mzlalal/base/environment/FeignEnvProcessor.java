package com.mzlalal.base.environment;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.mzlalal.base.common.GlobalConstant;
import com.mzlalal.base.util.AssertUtil;
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
     * 每个微服务对应一个配置: url = "${fantasy-oauth2.feign.url}"
     */
    private final String feignUrlFormat = "{}.feign.url";
    /**
     * 微服务配置的名字
     * 每个微服务对应一个配置: url -> "${fantasy-oauth2.feign.url}" = "http://localhost:9000"
     */
    private final String feignUrlValueFormat = "http://localhost:{}";
    /**
     * 微服务swagger文档地址
     */
    private final String serviceSwaggerUrl = "http://localhost:{}/doc.html";
    /**
     * 是否已经加载
     * 使用了bootstrap启动,导致EnvironmentPostProcessor执行两次
     * 如果加载过了,则改为true
     */
    private static boolean alreadyLoaded = false;
    /**
     * 本地已启动的服务ID列表
     */
    private static final List<String> LOCAL_SERVICE_STARTED_ID_LIST = new CopyOnWriteArrayList<>();
    /**
     * feign的端口配置(feign-service.properties)
     * feign.service.ids->设置动态配置的服务ID
     * xx.service.port->本地服务启动的端口
     */
    private static Properties feignServiceProperties;

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
        // 是否已加载
        if (alreadyLoaded) {
            return;
        }
        // 更改状态,防止重复进入
        alreadyLoaded = true;
        // 获取运行环境
        String activeProfile = environment.getProperty("spring.profiles.active");
        // 应用名
        String applicationName = environment.getProperty("spring.application.name");
        // 加载配置文件
        feignServiceProperties = PropertiesLoaderUtils.loadAllProperties("feign-service.properties");
        // 获取需要本地运行的微服务名称
        String enable = feignServiceProperties.getProperty("feign.service.enable");
        if (Boolean.FALSE.toString().equalsIgnoreCase(enable) || GlobalConstant.PRODUCT.equals(activeProfile)) {
            return;
        }
        String serviceIds = feignServiceProperties.getProperty("feign.service.ids");
        // 获取微服务名称切割成数组
        List<String> serviceIdList = StrUtil.split(serviceIds, ",");
        // Feign配置
        Map<String, Object> feignConfig = MapUtil.newHashMap(serviceIdList.size());
        // 遍历请求微服务swagger文档地址
        serviceIdList.parallelStream().forEach(serviceId -> {
            // 检查服务端口
            String servicePort = getServicePort(serviceId);
            AssertUtil.notBlank(servicePort, "serviceId:{}未设置feign访问端口(请检查feign-service.properties)"
                    , serviceId);
            // 微服务的URL指向配置名
            String feignUrlKey = StrUtil.format(this.feignUrlFormat, serviceId);
            // 微服务URL配置值
            String feignUrlValue = StrUtil.format(this.feignUrlValueFormat, servicePort);
            // 设置默认值为本地服务启动,若请求失败则设置为空
            feignConfig.put(feignUrlKey, feignUrlValue);
            // 微服务名相同直接设置
            if (StrUtil.equals(serviceId, applicationName)) {
                // 打印信息
                System.out.println(StrUtil.format("^_^本地{}服务已启动，当前应用可以和{}微服务本地调试。{}->{}"
                        , serviceId, serviceId, serviceId, feignUrlValue));
                return;
            }
            // 请求swagger路径
            String serviceSwaggerUrl = StrUtil.format(this.serviceSwaggerUrl, servicePort);
            // 异常则设置为空
            try {
                // 发送请求,0.5秒超时
                HttpResponse response = HttpRequest.get(serviceSwaggerUrl).timeout(500).execute();
                // 查看返回
                if (response.isOk()) {
                    // 添加到已启动的本地服务列表
                    LOCAL_SERVICE_STARTED_ID_LIST.add(serviceId);
                    // 打印信息
                    System.out.println(StrUtil.format("^_^本地{}服务已启动，当前应用可以和{}微服务本地调试。{}->{}"
                            , serviceId, serviceId, serviceId, feignUrlValue));
                }
            } catch (Exception ignored) {
                // 为空时获取会获取注册中心服务列表
                feignConfig.put(feignUrlKey, "");
                // 打印信息
                System.out.println((StrUtil.format("-_-本地{}服务没有启动，当前应用无法和{}微服务调试。", serviceId, serviceId)));
            }
        });
        // 添加到环境中
        environment.getPropertySources()
                .addLast(new MapPropertySource("feign.config", feignConfig));
    }

    /**
     * 获取本地服务已启动列表ID集合
     *
     * @return List<String> (CopyOnWriteArrayList)
     */
    public static List<String> getLocalServiceStartedIdList() {
        return LOCAL_SERVICE_STARTED_ID_LIST;
    }

    /**
     * 根据服务ID获取feign的端口配置(feign-service.properties)
     *
     * @param serviceId 服务ID
     * @return String
     */
    public static String getServicePort(String serviceId) {
        return feignServiceProperties.getProperty(serviceId + ".service.port");
    }
}
