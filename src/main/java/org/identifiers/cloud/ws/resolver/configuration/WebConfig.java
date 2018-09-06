package org.identifiers.cloud.ws.resolver.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * Project: resolver
 * Package: org.identifiers.cloud.ws.resolver.configuration
 * Timestamp: 2018-09-06 10:54
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {
        pathMatchConfigurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer contentNegotiationConfigurer) {
        // Default
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer) {
        // Default
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {
        // Default
    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        // Default
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        // Default
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
        // Default
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        // Default
    }

    @Override
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
        // Default
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry viewResolverRegistry) {
        // Default
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {
        // Default
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {
        // Default
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> list) {
        // Default
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> list) {
        // Default
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
        // Default
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
        // Default
    }

    @Override
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}
