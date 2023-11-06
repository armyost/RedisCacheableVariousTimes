package com.example.demo;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
        private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

        @Bean
        public RedisCacheManager contentCacheManager(RedisConnectionFactory connectionFactory) {
                logger.info("Creating custom cache configuration");

                Map<String, RedisCacheConfiguration> cacheNamesConfigurationMap = new HashMap<>();

                for (CacheType cacheType : CacheType.values()) {
                        cacheNamesConfigurationMap.put(cacheType.getCacheName(), RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(cacheType.getExpiredAfterWrite())));
                }

                return new RedisCacheManager(RedisCacheWriter.lockingRedisCacheWriter(connectionFactory),
                                // set default expiration for all other caches to 1Day (optional)
                                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(24 * 60 * 60)),
                                cacheNamesConfigurationMap);

        }

        // Map<String, RedisCacheConfiguration> cacheNamesConfigurationMap = new
        // HashMap<>();
        // // set cache with name "cache name 1" to expire after 100 seconds
        // cacheNamesConfigurationMap.put("cache name 1",
        // RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(100)));
        // // set cache with name "cache name 2" to expire after 200 seconds
        // cacheNamesConfigurationMap.put("cache name 2",
        // RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(200)));

        // return new
        // RedisCacheManager(RedisCacheWriter.lockingRedisCacheWriter(connectionFactory),
        // // set default expiration for all other caches to 300 seconds (optional)
        // RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(300)),
        // cacheNamesConfigurationMap);

}