package org.kasource.spring.console.config.properties;

import java.time.Duration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring-console", ignoreUnknownFields = false)
public class SpringConsoleConfig {

    private Monitor monitor = new Monitor();

    private EventPublishing eventPublishing = new EventPublishing();

    @Data
    public static class EventPublishing {
        private static final long INFINITE_TIMEOUT = -1L;
        private long timeoutMillis = INFINITE_TIMEOUT;
    }

    @Data
    public static class Monitor {
        private static final int DEFAULT_COMPARE_SCALE = 5;

        /** The interval to poll and check meter values **/
        private Duration pollInterval = Duration.ofSeconds(1);

        /** Scale (number of decimals before rounding) to use when comparing value **/
        private int compareScale = DEFAULT_COMPARE_SCALE;
    }
}
