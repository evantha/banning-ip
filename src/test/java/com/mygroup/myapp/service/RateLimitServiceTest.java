package com.mygroup.myapp.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.testing.FakeTicker;
import com.mygroup.myapp.config.RateLimitConfigs;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class RateLimitServiceTest {

    private static RateLimitService SERVICE;
    private static final FakeTicker TICKER = new FakeTicker();


    @BeforeAll
    static void setUp() {
        RateLimitConfigs rateLimitConfigs = new RateLimitConfigs();
        rateLimitConfigs.setEnabled(true);
        rateLimitConfigs.setMaxAllowed(2);
        rateLimitConfigs.setResetInterval(Duration.ofSeconds(3600));
        rateLimitConfigs.setPeriod(Duration.ofSeconds(60));

        LoadingCache<String, Integer> cache = CacheBuilder.newBuilder().
                expireAfterWrite(rateLimitConfigs.getResetInterval().getSeconds(), TimeUnit.SECONDS).
                ticker(TICKER).
                build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });
        SERVICE = new RateLimitService(cache, rateLimitConfigs);
    }

    @Test
    void limitWithInAllowedPeriodTest() {
        String testIp1 = "testIP-1";
        String testIp2 = "testIP-2";

        assertFalse(SERVICE.isBlocked(testIp1));
        assertFalse(SERVICE.isBlocked(testIp2));

        SERVICE.increment(testIp1);
        SERVICE.increment(testIp1);
        SERVICE.increment(testIp2);

        assertTrue(SERVICE.isBlocked(testIp1));
        assertFalse(SERVICE.isBlocked(testIp2));

        TICKER.advance(Duration.ofMinutes(30));
        assertTrue(SERVICE.isBlocked(testIp1));
        assertFalse(SERVICE.isBlocked(testIp2));

        TICKER.advance(Duration.ofMinutes(30));
        assertFalse(SERVICE.isBlocked(testIp1));
        assertFalse(SERVICE.isBlocked(testIp2));
    }

    @Test
    void limitBeyondAllowedPeriodTest() {
        String testIp1 = "testIP-1";
        String testIp2 = "testIP-2";

        assertFalse(SERVICE.isBlocked(testIp1));
        assertFalse(SERVICE.isBlocked(testIp2));

        SERVICE.increment(testIp1);
        SERVICE.increment(testIp2);

        assertFalse(SERVICE.isBlocked(testIp1));
        assertFalse(SERVICE.isBlocked(testIp2));

        TICKER.advance(Duration.ofMinutes(60));
        SERVICE.increment(testIp1);
        assertFalse(SERVICE.isBlocked(testIp1));
        assertFalse(SERVICE.isBlocked(testIp2));

    }

}