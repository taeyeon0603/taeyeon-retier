package proj.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Spring RestTemplate
 * Http请求工具
 */
public class HttpUtils {
  public static Object doPost(String url, JSONObject headersJson, JSONObject paramsJson) {
    RestTemplate template = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    headersJson.forEach((k, v) -> headers.add(k, v.toString()));
    paramsJson.forEach((k, v) -> params.add(k, v.toString()));
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
    ResponseEntity res = template.postForEntity(url, entity, String.class);
    return res.getBody();
  }

  public static Object doGet(String url) {
    RestTemplate template = new RestTemplate();
    ResponseEntity res = template.getForEntity(url, String.class);
    return res.getBody();
  }

  public static byte[] doExchange(String url, JSONObject headersJson) {
    HttpHeaders headers = new HttpHeaders();
    headersJson.forEach((k, v) -> headers.add(k, v.toString()));
    HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
    RestTemplate template = new RestTemplate();
    ResponseEntity<byte[]> res = template.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
    return res.getBody();
  }
}
