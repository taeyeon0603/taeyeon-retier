package proj.util.Redis;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class Redis {
  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  public void incr(String k, Long l) {
    redisTemplate.opsForValue().increment(k, l);
  }

  public <T> void set(String key, T value) {
    redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
  }

  public <T> void setWithTtl(String key, T value, Integer sec) {
    redisTemplate.opsForValue().set(key, JSON.toJSONString(value), sec, TimeUnit.SECONDS);
  }

  public <T> T get(String key, Class<T> valueClass) {
    String value = redisTemplate.opsForValue().get(key);
    return !StringUtils.isEmpty(value) ? JSON.parseObject(value, valueClass) : null;
  }

  public <T> List<T> getList(String key, Class<T> valueClass) {
    String value = redisTemplate.opsForValue().get(key);
    return !StringUtils.isEmpty(value) ? JSON.parseArray(value, valueClass) : null;
  }

  public String get(String key) {
    String value = redisTemplate.opsForValue().get(key);
    return !StringUtils.isEmpty(value) ? JSON.parseObject(value, String.class) : null;
  }

  public Boolean hasKeyInMap(String key) {
    return redisTemplate.hasKey(key);
  }

  public Boolean hasKeyInMap(String mapName, String key) {
    return redisTemplate.opsForHash().hasKey(mapName, key);
  }

  public Set<String> keys(String k) {
    return redisTemplate.keys(k);
  }

  public Boolean expire(String key, Long l, TimeUnit unit) {
    return redisTemplate.expire(key, l, unit);
  }

  public Long getExpire(String key) {
    return redisTemplate.getExpire(key, TimeUnit.SECONDS);
  }

  public Boolean delete(String key) {
    return redisTemplate.delete(key);
  }

  public List<String> multiGet(String key) {
    Set<String> keys = redisTemplate.keys(key);
    if (keys != null) {
      return redisTemplate.opsForValue().multiGet(keys);
    } else {
      return new ArrayList<>();
    }
  }

  public Map<Object, Object> multiGetInMap(String mapName) {
    return redisTemplate.opsForHash().entries(mapName);
  }

  public Object getInMap(String mapName, String key) {
    return redisTemplate.opsForHash().get(mapName, key);
  }

  public void putInMap(String mapName, String key, Object value) {
    redisTemplate.opsForHash().put(mapName, key, value);
  }

  public void putAll(String mapName, Map<Object, Object> map) {
    redisTemplate.opsForHash().putAll(mapName, map);
  }

  public Map<Object, Object> getMap(String mapName) {
    return redisTemplate.opsForHash().entries(mapName);
  }

  public void deleteInMap(String mapName, String key) {
    redisTemplate.opsForHash().delete(mapName, key);
  }

  public void setBytesInMap(String key, String field, byte[] value) {
    redisTemplate.execute((RedisCallback<Boolean>) conn -> conn.hSet(key.getBytes(), field.getBytes(), value));
  }

  public byte[] getBytesInMap(String key, String field) {
    return redisTemplate.execute((RedisCallback<byte[]>) conn -> conn.hGet(key.getBytes(), field.getBytes()));
  }

  public Map<byte[], byte[]> getBytesMap(String key) {
    return redisTemplate.execute((RedisCallback<Map<byte[], byte[]>>) conn -> conn.hGetAll(key.getBytes()));
  }

  public <T> Set<T> getSet(String key, Class<T> valueClass) {
    Set<String> values = redisTemplate.opsForSet().members(key);
    if (values == null) {
      return null;
    }
    return values.stream().map(v -> JSON.parseObject(v, valueClass)).collect(Collectors.toSet());
  }

  public <T> void addInSet(String key, T value) {
    redisTemplate.opsForSet().add(key, JSON.toJSONString(value));
  }

  public <T> void addAllInSet(String key, Collection<T> collection) {
    for (T value : collection) {
      redisTemplate.opsForSet().add(key, JSON.toJSONString(value));
    }
  }
}
