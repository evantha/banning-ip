package com.mygroup.myapp.factory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mygroup.myapp.config.RateLimitConfigs;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Factory
public class CacheFactory {

    private final RateLimitConfigs rateLimitConfigs;

    public CacheFactory(RateLimitConfigs rateLimitConfigs) {
        this.rateLimitConfigs = rateLimitConfigs;
    }

    @Bean
    @Singleton
    LoadingCache<String, Integer> cache() {
        System.out.println("default");
        return CacheBuilder.newBuilder().
                expireAfterWrite(this.rateLimitConfigs.getResetInterval().getSeconds(), TimeUnit.SECONDS).
                build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });
    }
}
