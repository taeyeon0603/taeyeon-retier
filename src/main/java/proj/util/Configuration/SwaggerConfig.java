package proj.util.Configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnExpression("${swagger.enable: true}")
public class SwaggerConfig {
  @Bean
  public Docket createDocket() {
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(new ApiInfoBuilder().title("Swagger API Page")
        .description("具体配置可在项目中全局搜索本段文字来找到 :)")
        .version("1.0").build()
      ).select()
      .apis(RequestHandlerSelectors.basePackage("proj.modular.controller"))
      .paths(PathSelectors.any())
      .build();
  }
}
