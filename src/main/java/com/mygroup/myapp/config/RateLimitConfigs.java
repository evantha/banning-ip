package com.mygroup.myapp.config;

import io.micronaut.context.annotation.ConfigurationProperties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@ConfigurationProperties("ratelimit")
public class RateLimitConfigs {

    private boolean enabled;

    @Min(1)
    private int maxAllowed = 5;

    private Duration period = Duration.ofSeconds(10);

    @NotNull
    private Duration resetInterval = Duration.ofSeconds(20);

    /**
     * Checks whether the rate limiting should be enabled.
     * @return True if rate limiting needs to be checked
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(int maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    public Duration getPeriod() {
        return period;
    }

    public void setPeriod(Duration period) {
        this.period = period;
    }

    public Duration getResetInterval() {
        return resetInterval;
    }

    public void setResetInterval(Duration resetInterval) {
        this.resetInterval = resetInterval;
    }

    @Override
    public String toString() {
        return "RateLimitConfigs{" +
                "enabled=" + enabled +
                ", maxAllowed=" + maxAllowed +
                ", period=" + period.getSeconds() +
                ", resetInterval=" + resetInterval.getSeconds() +
                '}';
    }
}
