package org.kasource.spring.console.timeseries.bucket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kasource.spring.console.timeseries.BucketFullException;

import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class DoubleBucketTest {

    @Test
    void createAndPush() {
        DoubleBucket valueBucket = new DoubleBucket(4);

        valueBucket.push(1.0D);
        valueBucket.push(2.0D);
        valueBucket.push(3.0D);
        valueBucket.push(4.0D);

        assertThat(valueBucket.isFull(), is(true));
        assertThat(valueBucket.getIndex(), is(3));
        // Latest value should be returned first
        assertThat(valueBucket.stream().collect(Collectors.toList()), contains(4.0D, 3.0D, 2.0D, 1.0D));

    }

    @Test
    void createAndPushNotFull() {
        DoubleBucket valueBucket = new DoubleBucket(8);

        valueBucket.push(1.0D);
        valueBucket.push(2.0D);
        valueBucket.push(3.0D);
        valueBucket.push(4.0D);

        assertThat(valueBucket.isFull(), is(false));
        assertThat(valueBucket.getIndex(), is(3));
        // Latest value should be returned first
        assertThat(valueBucket.stream().collect(Collectors.toList()), contains(4.0D, 3.0D, 2.0D, 1.0D));

    }

    @Test
    void bucketOverflow() {
        DoubleBucket valueBucket = new DoubleBucket(2);

        valueBucket.push(1.0D);
        valueBucket.push(2.0D);

        assertThat(valueBucket.isFull(), is(true));
        assertThat(valueBucket.getIndex(), is(1));

        BucketFullException throwable = Assertions.assertThrows(BucketFullException.class, () ->
                valueBucket.push(3.0D)
        );
        assertThat(throwable, is(notNullValue()));
    }

    @Test
    void clear() {
        DoubleBucket valueBucket = new DoubleBucket(2);

        valueBucket.push(1.0D);
        valueBucket.push(2.0D);

        assertThat(valueBucket.isEmpty(), is(false));
        assertThat(valueBucket.isFull(), is(true));

        valueBucket.clear();

        assertThat(valueBucket.isEmpty(), is(true));
        assertThat(valueBucket.isFull(), is(false));
    }
}
