package proj.util.Shiro;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import proj.util.Tip;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter extends FormAuthenticationFilter {
  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    if (request instanceof HttpServletRequest) {
      if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTION")) {
        return true;
      }
    }
    return super.isAccessAllowed(request, response, mappedValue);
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    httpServletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
    httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
    httpServletResponse.setCharacterEncoding("UTF-8");
    httpServletResponse.setContentType("application/json");
//    httpServletResponse.setStatus(405);
    httpServletResponse.getWriter().write(JSONObject.toJSON(Tip.fail(405, "未登录")).toString());
    return false;
  }
}
