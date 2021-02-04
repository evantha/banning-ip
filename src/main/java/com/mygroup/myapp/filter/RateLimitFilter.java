package com.mygroup.myapp.filter;

import com.mygroup.myapp.config.RateLimitConfigs;
import com.mygroup.myapp.service.RateLimitService;
import io.micronaut.core.order.Ordered;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.FilterOrderProvider;
import io.micronaut.http.filter.OncePerRequestHttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.http.server.util.HttpClientAddressResolver;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Limits the incoming requests for a given IP, based on the given property values
 */
@Filter("/api/**")
public class RateLimitFilter extends OncePerRequestHttpServerFilter implements FilterOrderProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitFilter.class);
    private final HttpClientAddressResolver addressResolver;
    private final RateLimitConfigs rateLimitConfigs;
    private final RateLimitService rateLimitService;

    public RateLimitFilter(HttpClientAddressResolver addressResolver, RateLimitConfigs rateLimitConfigs, RateLimitService rateLimitService) {
        this.addressResolver = addressResolver;
        this.rateLimitConfigs = rateLimitConfigs;
        this.rateLimitService = rateLimitService;
        System.out.println(this.rateLimitConfigs);
        LOGGER.debug("filter is initiated with {}", this.rateLimitConfigs);
    }

    @Override
    protected Publisher<MutableHttpResponse<?>> doFilterOnce(HttpRequest<?> request, ServerFilterChain chain) {

        if (this.rateLimitConfigs.isEnabled()) {
            String key = addressResolver.resolve(request);
            if (this.rateLimitService.isBlocked(key)) {
                LOGGER.warn("{} has exceeded the request limit", key);
                return errorResponse();
            }
            this.rateLimitService.increment(key);
            return chain.proceed(request);
        } else {
            return chain.proceed(request);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private Publisher<MutableHttpResponse<?>>  errorResponse() {
        return Flowable.just(
                HttpResponse.status(HttpStatus.TOO_MANY_REQUESTS).
                        body("Maximum request limit has exceeded. Please try again later")
        );
    }

}
