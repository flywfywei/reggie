package com.wfy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wfy.common.JacksonObjectMapper;
import com.wfy.interceptor.EmployeeLoginCheckInterceptor;
import com.wfy.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author wfy
 * @version 1.0
 */
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加用户登录拦截器
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/front/page/**", "/front/index.html", "/")
                .excludePathPatterns("/front/page/login.html");
        //添加员工登录拦截器
        registry.addInterceptor(new EmployeeLoginCheckInterceptor())
                .addPathPatterns("/backend/page/**", "/backend/index.html")
                .excludePathPatterns("/backend/page/login/login.html");
    }

    /**
     * 扩展 mvc框架 的消息转换器
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //设置消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用 JacksonObjectMapper 将 Java 对象转为 json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器追加到 mvc框架 的消息转换器集合中
        converters.add(0, messageConverter);//添加索引需靠前，原因是默认的消息转换器中有可以处理所有类型的转换器
    }
}
