package org.kasource.spring.console.timeseries.bucket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kasource.spring.console.timeseries.BucketFullException;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class GenericBucketTest {

    @Test
    void createAndPush() {
        GenericBucket<BigDecimal> valueBucket = new GenericBucket<>(new BigDecimal[4]);

        valueBucket.push(BigDecimal.valueOf(1L));
        valueBucket.push(BigDecimal.valueOf(2L));
        valueBucket.push(BigDecimal.valueOf(3L));
        valueBucket.push(BigDecimal.valueOf(4L));

        assertThat(valueBucket.isFull(), is(true));
        assertThat(valueBucket.getIndex(), is(3));
        // Latest value should be returned first
        assertThat(valueBucket.stream().collect(Collectors.toList()),
                contains(BigDecimal.valueOf(4L), BigDecimal.valueOf(3L), BigDecimal.valueOf(2L), BigDecimal.valueOf(1L)));

    }

    @Test
    void createAndPushNotFull() {
        GenericBucket<BigDecimal> valueBucket = new GenericBucket<>(new BigDecimal[8]);

        valueBucket.push(BigDecimal.valueOf(1L));
        valueBucket.push(BigDecimal.valueOf(2L));
        valueBucket.push(BigDecimal.valueOf(3L));
        valueBucket.push(BigDecimal.valueOf(4L));

        assertThat(valueBucket.isFull(), is(false));
        assertThat(valueBucket.getIndex(), is(3));
        // Latest value should be returned first
        assertThat(valueBucket.stream().collect(Collectors.toList()),
                contains(BigDecimal.valueOf(4L), BigDecimal.valueOf(3L), BigDecimal.valueOf(2L), BigDecimal.valueOf(1L)));

    }

    @Test
    void bucketOverflow() {
        GenericBucket<BigDecimal> valueBucket = new GenericBucket<>(new BigDecimal[2]);

        valueBucket.push(BigDecimal.valueOf(1L));
        valueBucket.push(BigDecimal.valueOf(2L));

        assertThat(valueBucket.isFull(), is(true));
        assertThat(valueBucket.getIndex(), is(1));

        BucketFullException throwable = Assertions.assertThrows(BucketFullException.class, () ->
                valueBucket.push(BigDecimal.valueOf(3L))
        );
        assertThat(throwable, is(notNullValue()));
    }

    @Test
    void clear() {
        GenericBucket<BigDecimal> valueBucket = new GenericBucket<>(new BigDecimal[2]);

        valueBucket.push(BigDecimal.valueOf(1L));
        valueBucket.push(BigDecimal.valueOf(2L));

        assertThat(valueBucket.isEmpty(), is(false));
        assertThat(valueBucket.isFull(), is(true));

        valueBucket.clear();

        assertThat(valueBucket.isEmpty(), is(true));
        assertThat(valueBucket.isFull(), is(false));
    }
}
