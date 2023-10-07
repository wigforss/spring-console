package org.kasource.spring.console.timeseries;

import org.junit.jupiter.api.Test;
import org.kasource.spring.console.timeseries.bucket.DoubleBucket;

import java.time.Duration;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class DoubleTimeSeriesTest {

    @Test
    void createAndPush() {
        DoubleTimeSeries timeSeries = new DoubleTimeSeries(3, 3);

        timeSeries.push(1.0D);
        timeSeries.push(2.0D);
        timeSeries.push(3.0D);
        timeSeries.push(4.0D);
        timeSeries.push(5.0D);
        timeSeries.push(6.0D);
        timeSeries.push(7.0D);
        timeSeries.push(8.0D);

        // Latest value should be returned first
        assertThat(timeSeries.stream().collect(Collectors.toList()), contains(8.0D, 7.0D, 6.0D, 5.0D, 4.0D, 3.0D, 2.0D, 1.0D));

    }

    @Test
    void bucketRetention() {
        DoubleTimeSeries timeSeries = new DoubleTimeSeries(3, 3);

        // Bucket
        timeSeries.push(1.0D);
        timeSeries.push(2.0D);
        timeSeries.push(3.0D);
        // Bucket
        timeSeries.push(4.0D);
        timeSeries.push(5.0D);
        timeSeries.push(6.0D);
        // Bucket
        timeSeries.push(7.0D);
        timeSeries.push(8.0D);
        timeSeries.push(9.0D);
        // Bucket
        timeSeries.push(10.0D);
        timeSeries.push(11.0D);
        timeSeries.push(12.0D);
        // Bucket
        timeSeries.push(13.0D);

        // Latest value should be returned first
        assertThat(timeSeries.stream().collect(Collectors.toList()), contains(13.0D, 12.0D, 11.0D, 10.0D, 9.0D, 8.0D, 7.0D));

    }

    @Test
    void createAndPushFilterByDuration() throws InterruptedException {
        DoubleTimeSeries timeSeries = new DoubleTimeSeries(3, 3);

        timeSeries.push(1.0D);
        Thread.sleep(100);
        timeSeries.push(2.0D);
        Thread.sleep(100);
        timeSeries.push(3.0D);
        Thread.sleep(100);
        timeSeries.push(4.0D);
        Thread.sleep(100);
        timeSeries.push(5.0D);
        Thread.sleep(100);
        timeSeries.push(6.0D);
        Thread.sleep(100);
        timeSeries.push(7.0D);
        Thread.sleep(100);
        timeSeries.push(8.0D);

        // Latest value should be returned first
        assertThat(timeSeries.stream(Duration.ofMillis(400)).collect(Collectors.toList()), contains(8.0D, 7.0D, 6.0D, 5.0D));

    }
}
