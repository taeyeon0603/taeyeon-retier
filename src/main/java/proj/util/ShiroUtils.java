package proj.util;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import proj.modular.entity.Master.User;

@Component
public class ShiroUtils {

  public <T> T getSessionAttr(String attrName, Class<T> tClass) {
    Object obj = SecurityUtils.getSubject().getSession().getAttribute(attrName);
    if (obj != null) {
      return JSON.parseObject(JSON.toJSONString(obj), tClass);
    }
    return null;
  }

  public void setSessionAttr(String attrName, Object attrValue) {
    SecurityUtils.getSubject().getSession().setAttribute(attrName, attrValue);
  }

  public void setRequestURI(String uri) {
    Session session = SecurityUtils.getSubject().getSession();
    session.setAttribute("requestURI", uri);
  }

  public String getRequestURI() {
    Object attr = SecurityUtils.getSubject().getSession().getAttribute("requestURI");
    return attr == null ? null : attr.toString();
  }

  public String getCurUserId() {
    Session session = SecurityUtils.getSubject().getSession();
    Object user = session.getAttribute("user");
    if (user == null) {
      return null;
    }
    return ((User) user).getId();
  }
}
