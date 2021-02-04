package com.mygroup.myapp.service;

import com.google.common.cache.LoadingCache;
import com.mygroup.myapp.config.RateLimitConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.concurrent.ExecutionException;

@Singleton
public class RateLimitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitService.class);
    private final LoadingCache<String, Integer> cache;
    private final RateLimitConfigs rateLimitConfigs;

    public RateLimitService(LoadingCache<String, Integer> cache, RateLimitConfigs rateLimitConfigs) {
        this.cache = cache;
        this.rateLimitConfigs = rateLimitConfigs;
    }

    /**
     * Increments the value by one for the given key.
     * @param key   The cache key
     */
    public void increment(String key) {
        int value = 0;
        try {
            value = this.cache.get(key);
            LOGGER.info("IP {} has hit {} times", key, value + 1);
        } catch (ExecutionException e) {
            value = 0;
        }
        this.cache.put(key, ++value);

        if (this.isBlocked(key)) {
            // publish an event to rabbitmq
        }
    }

    /**
     * Checks that the given key has reached the maximum hits allowed.
     * @param key The cache key
     * @return    True if the given key has reached its max
     */
    public boolean isBlocked(String key) {
        try {
            return this.cache.get(key) >= this.rateLimitConfigs.getMaxAllowed();
        } catch (ExecutionException e) {
            return false;
        }
    }
}
