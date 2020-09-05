package ce.logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class LoggerConfig implements WebMvcConfigurer {

    @Autowired
    private RequestInterceptor RequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(RequestInterceptor).addPathPatterns("/*");
    }

    
}