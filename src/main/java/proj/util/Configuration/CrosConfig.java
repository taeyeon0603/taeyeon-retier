package proj.util.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import proj.util.Log;

@Configuration
public class CrosConfig extends WebMvcConfigurationSupport {
  @Autowired
  private Log logger;

  @Override
  protected void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedOrigins("*")
      .allowCredentials(true)
      .allowedMethods("POST", "GET", "PUT", "DELETE")
      .maxAge(3600);
    super.addCorsMappings(registry);
    logger.info("跨域配置完成");
  }

  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    super.addResourceHandlers(registry);
    logger.info("已经可以访问静态资源了");
  }
}
