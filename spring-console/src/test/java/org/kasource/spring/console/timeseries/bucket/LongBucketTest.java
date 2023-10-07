package org.kasource.spring.console.timeseries.bucket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kasource.spring.console.timeseries.BucketFullException;

import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class LongBucketTest {

    @Test
    void createAndPush() {
        LongBucket valueBucket = new LongBucket(4);

        valueBucket.push(1L);
        valueBucket.push(2L);
        valueBucket.push(3L);
        valueBucket.push(4L);

        assertThat(valueBucket.isFull(), is(true));
        assertThat(valueBucket.getIndex(), is(3));
        // Latest value should be returned first
        assertThat(valueBucket.stream().collect(Collectors.toList()), contains(4L, 3L, 2L, 1L));

    }

    @Test
    void createAndPushNotFull() {
        LongBucket valueBucket = new LongBucket(8);

        valueBucket.push(1L);
        valueBucket.push(2L);
        valueBucket.push(3L);
        valueBucket.push(4L);

        assertThat(valueBucket.isFull(), is(false));
        assertThat(valueBucket.getIndex(), is(3));
        // Latest value should be returned first
        assertThat(valueBucket.stream().collect(Collectors.toList()), contains(4L, 3L, 2L, 1L));

    }

    @Test
    void bucketOverflow() {
        LongBucket valueBucket = new LongBucket(2);

        valueBucket.push(1L);
        valueBucket.push(2L);

        assertThat(valueBucket.isFull(), is(true));
        assertThat(valueBucket.getIndex(), is(1));

        BucketFullException throwable = Assertions.assertThrows(BucketFullException.class, () ->
                valueBucket.push(3L)
        );
        assertThat(throwable, is(notNullValue()));
    }

    @Test
    void clear() {
        LongBucket valueBucket = new LongBucket(2);

        valueBucket.push(1L);
        valueBucket.push(2L);

        assertThat(valueBucket.isEmpty(), is(false));
        assertThat(valueBucket.isFull(), is(true));

        valueBucket.clear();

        assertThat(valueBucket.isEmpty(), is(true));
        assertThat(valueBucket.isFull(), is(false));
    }


    @Test
    void findIndex() {
        LongBucket valueBucket = new LongBucket(8);

        valueBucket.push(10L);
        valueBucket.push(19L);
        valueBucket.push(28L);
        valueBucket.push(36L);
        valueBucket.push(45L);
        valueBucket.push(54L);
        valueBucket.push(62L);
        valueBucket.push(71L);

        valueBucket.clear();
        valueBucket.push(10L);
        valueBucket.push(19L);
        valueBucket.push(28L);


        System.out.println(valueBucket.findIndex(60L, true));

    }
}
