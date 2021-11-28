package com.mzlalal.base.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.apache.ibatis.javassist.ClassPool;
import org.springframework.boot.SpringBootVersion;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * controller请求信息打印拦截器
 *
 * @author sliver Mzlalal
 */
public class FantasyActionReportInterceptor implements HandlerInterceptor {
    /**
     * 标题
     */
    protected static final String TITLE = "\nSpringBoot-" + SpringBootVersion.getVersion() + " Action Report -------- ";
    /**
     * 时间格式化器
     */
    protected static final ThreadLocal<SimpleDateFormat> SDF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    /**
     * 在请求后打印
     * true 请求后 false 请求前
     */
    protected static boolean reportAfterInvocation = false;
    /**
     * 定位方法所在行数，依赖于java assist
     */
    protected static boolean locateMethodLineNumber = true;
    /**
     * 包含所有请求路径
     */
    private static final String[] DEFAULT_INCLUDE_PATH = new String[]{"/**"};
    /**
     * 忽略所有请求路径
     */
    private static final String[] DEFAULT_EXCLUDE_PATH = new String[]{"/**/*.css", "/**/*.js", "/**/*.html", "/**/*.jpg", "/**/*.png", "/**/*.ico", "/**/*.woff2"};
    /**
     * pool
     */
    private static final ClassPool POOL = ClassPool.getDefault();
    /**
     * 空参数
     */
    private static final String EMPTY_PARAM = "[]";

    public String[] getIncludePath() {
        return DEFAULT_INCLUDE_PATH;
    }

    public String[] getExcludePath() {
        return DEFAULT_EXCLUDE_PATH;
    }

    @Override
    @SuppressWarnings("all")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!reportAfterInvocation && handler instanceof HandlerMethod) {
            this.report(request, response, (HandlerMethod) handler);
        }
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (reportAfterInvocation && handler instanceof HandlerMethod) {
            this.report(request, response, (HandlerMethod) handler);
        }
    }

    /**
     * 打印参数
     * body类参数不答应
     *
     * @param request  请求
     * @param response 响应
     * @param handler  方法
     */
    public void report(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        StringBuilder sb = new StringBuilder(TITLE).append(SDF.get().format(new Date())).append(" --------------------\n");
        sb.append("Url         : ").append(request.getMethod()).append(" ").append(request.getRequestURI()).append("\n");
        // 获取bean的类型
        Class<?> cc = handler.getBeanType();
        // 获取行数,默认为1
        int lineNumber = this.getLineNumber(handler, cc);
        sb.append("Controller  : ").append(cc.getName()).append(".(").append(cc.getSimpleName()).append(".java:").append(lineNumber).append(")");
        sb.append("\nMethod      : ").append(handler.getMethod().getName());
        String paramStr = JSON.toJSONString(request.getParameterNames());
        if (!EMPTY_PARAM.equals(paramStr)) {
            sb.append("\nParameter   : ").append(paramStr);
        }
        sb.append("\n--------------------------------------------------------------------------------\n");
        System.out.println(sb.toString());
    }

    /**
     * 获取方法的函数
     *
     * @param handler 方法
     * @param cc      类
     * @return int 行数
     */
    @SneakyThrows
    private int getLineNumber(HandlerMethod handler, Class<?> cc) {
        int lineNumber = 1;
        if (locateMethodLineNumber) {
            lineNumber = POOL.get(cc.getName()).getDeclaredMethod(handler.getMethod().getName()).getMethodInfo().getLineNumber(0);
        }
        return lineNumber;
    }
}
