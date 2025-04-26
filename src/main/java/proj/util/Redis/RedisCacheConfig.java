package proj.util.Redis;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {
  @Bean
  public CacheManager cacheManager(RedisConnectionFactory factory) {
    RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext
      .SerializationPair
      .fromSerializer(new GenericJackson2JsonRedisSerializer());
    RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
      .entryTtl(Duration.ofMinutes(10))
      .serializeValuesWith(pair);

    return RedisCacheManager
      .builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
      .cacheDefaults(configuration)
      .build();
  }
}
