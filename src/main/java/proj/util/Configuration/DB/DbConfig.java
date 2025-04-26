package proj.util.Configuration.DB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import proj.util.Log;

import javax.sql.DataSource;

@Configuration
public class DbConfig {
  @Autowired
  private Log logger;

  @Primary
  @Bean(name = "primaryDataSource")
  @Qualifier("primaryDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.master")
  public DataSource mysqlDataSource1() {
    logger.info("第一数据源(mysql数据库)配置完成");
    return DataSourceBuilder.create().build();
  }

//  @Bean(name = "secondDataSource")
//  @Qualifier("secondDataSource")
//  @ConfigurationProperties(prefix = "spring.datasource.second")
//  public DataSource secondDataSource() {
//    logger.info("第二数据源(sqlserver链接数据库)配置完成");
//    return DataSourceBuilder.create().build();
//  }
}
