package com.gdy.boke.config;

import com.gdy.boke.intercepter.AuthIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@PropertySource("classpath:intercepter/intercepter-path.properties")
@Import({AuthIntercepter.class})
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthIntercepter authIntercepter;

    @Value("#{'${authToken.path}'.split(',')}")
    private  String[] authPath;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authIntercepter).addPathPatterns(authPath).order(1);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将templates目录下的CSS、JS文件映射为静态资源，防止Spring把这些资源识别成thymeleaf模版
        registry.addResourceHandler("/templates/**.js").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/templates/**.css").addResourceLocations("classpath:/templates/");
        //其他静态资源
        registry.addResourceHandler("/static/**","/assets/**","/uekind/**").addResourceLocations("classpath:/static/","classpath:/assets/","classpath:/uekind/");

    }
}
