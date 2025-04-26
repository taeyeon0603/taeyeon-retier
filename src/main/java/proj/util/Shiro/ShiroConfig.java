package proj.util.Shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proj.util.Log;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
  @Autowired
  private RedisSessionDAO redisSessionDAO;
  @Autowired
  private Log logger;

  @Bean
  public CustomRealm myShiroRealm() {
    return new CustomRealm();
  }

  /**
   * CORE!!
   * 安全事务管理器
   *
   * @return
   */
  @Bean
  public SecurityManager securityManager() {
    DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
    webSecurityManager.setRealm(myShiroRealm()); //权限环境
    webSecurityManager.setSessionManager(sessionManager()); //应用会话管理器
    logger.info("安全事务管理器配置完成");
    return webSecurityManager;
  }

  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);
    Map<String, String> map = new HashMap<>();

    map.put("/**", "authc");
    map.put("/static/**", "anon"); //静态文件不拦截
    map.put("/index/**", "anon"); //大屏接口不限制
    map.put("/login", "anon"); //登录接口 不拦截
    map.put("/swagger-ui.html", "anon"); //swagger
    map.put("/doc.html", "anon"); //swagger
    map.put("/swagger-resources/**", "anon"); //swagger
    map.put("/webjars/**", "anon");
    map.put("/v2/**", "anon"); //swagger

//    shiroFilterFactoryBean.setLoginUrl("/login.html"); //这里是指登录的静态页面位置而不是接口
    shiroFilterFactoryBean.setSuccessUrl("/index.html");
    shiroFilterFactoryBean.setUnauthorizedUrl("/error.html");

    Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
    filters.put("authc", new LoginFilter());

    shiroFilterFactoryBean.setFilters(filters);
    shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

    return shiroFilterFactoryBean;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
    advisor.setSecurityManager(securityManager);
    return advisor;
  }

  @Bean
  public CustomSessionListener sessionListener() {
    return new CustomSessionListener();
  }

  @Bean
  public SessionManager sessionManager() {
    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    Collection<SessionListener> listeners = new ArrayList<>();
    listeners.add(sessionListener());
    sessionManager.setSessionDAO(redisSessionDAO);
    sessionManager.setSessionListeners(listeners); //添加监听器
    sessionManager.setSessionIdUrlRewritingEnabled(false); //是否在url尾部显示JSessionId
    sessionManager.setSessionValidationInterval(-1000); //会话失效扫描周期：1秒
    sessionManager.isDeleteInvalidSessions();
    Cookie cookie = sessionManager.getSessionIdCookie();
    cookie.setName("retire");

    return sessionManager;
  }
}
