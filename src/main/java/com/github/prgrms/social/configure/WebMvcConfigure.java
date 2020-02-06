package com.github.prgrms.social.configure;

import com.github.prgrms.social.configure.support.SimpleOffsetPageRequest;
import com.github.prgrms.social.configure.support.SimpleOffsetPageableHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.List;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {

    // TODO offset,limit를 Query Parameter로 받아 Pageable 구현체를 생성해주는 HandlerMethodArgumentResolver 인터페이스 구현체를 com.github.prgrms.social.configure.support 패키지 아래에 구현하고 아래에서 설정
    @Bean
    public SimpleOffsetPageableHandlerMethodArgumentResolver simpleOffsetPageableHandlerMethodArgumentResolver() {
        SimpleOffsetPageableHandlerMethodArgumentResolver resolver = new SimpleOffsetPageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(new SimpleOffsetPageRequest(0,5));
        return resolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(simpleOffsetPageableHandlerMethodArgumentResolver());
    }

    private String baseApiPath = "api";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/**/*.css", "/**/*.html", "/**/*.js", "/**/*.jsx", "/**/*.png", "/**/*.ttf", "/**/*.woff", "/**/*.woff2")
            .setCachePeriod(0)
            .addResourceLocations("classpath:/static/");

        registry
            .addResourceHandler("/", "/**")
            .setCachePeriod(0)
            .addResourceLocations("classpath:/static/index.html")
            .resourceChain(true)
            .addResolver(new PathResourceResolver() {
                @Override
                protected Resource getResource(String resourcePath, Resource location) {
                    if (resourcePath.startsWith(baseApiPath) || resourcePath.startsWith(baseApiPath.substring(1))) {
                        return null;
                    }
                    return location.exists() && location.isReadable() ? location : null;
                }
            });
    }

}