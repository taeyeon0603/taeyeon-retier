package proj.util.Configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class Constant {
  @Value("${headersToTry}")
  private String headersToTry;
  @Value("${wzs-lc-ip}")
  private String allowIp;
  @Value("${spring.profiles.active}")
  private String profile;
  @Value("${shiro.sessionExpired}")
  private Long sessionExpired;
  @Value("${pic.path}")
  private String picPath;
  @Value("${pic.url}")
  private String picUrl;
  @Value("${workFile.path}")
  private String workFilePath;
  @Value("${workFile.url}")
  private String workFileUrl;
//  @Value("${retire.importTemplate.url}")
//  private String importTemplateUrl;
}
