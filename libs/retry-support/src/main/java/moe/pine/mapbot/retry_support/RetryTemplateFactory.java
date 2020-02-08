package moe.pine.mapbot.retry_support;

import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RetryTemplateFactory {
    @SafeVarargs
    public static RetryTemplate create(
        int maxAttempts,
        long initialInterval,
        double multiplier,
        Class<? extends Throwable>... exceptions
    ) {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions =
            Stream.of(exceptions)
                .map(exception -> Map.entry(exception, true))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
        RetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts, retryableExceptions);

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(initialInterval);
        backOffPolicy.setMultiplier(multiplier);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

    @SafeVarargs
    public static RetryTemplate createUnlimited(
        long initialInterval,
        long maxInterval,
        double multiplier,
        Class<? extends Throwable>... exceptions
    ) {
        AlwaysRetryPolicy alwaysRetryPolicy = new AlwaysRetryPolicy();
        Map<Class<? extends Throwable>, RetryPolicy> policyMap =
            Stream.of(exceptions)
                .map(exception -> Map.entry(exception, alwaysRetryPolicy))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        ExceptionClassifierRetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy();
        retryPolicy.setPolicyMap(policyMap);

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(initialInterval);
        backOffPolicy.setMaxInterval(maxInterval);
        backOffPolicy.setMultiplier(multiplier);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }
}
