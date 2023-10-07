package org.kasource.spring.console.timeseries;

import org.kasource.spring.console.timeseries.bucket.Bucket;
import org.kasource.spring.console.timeseries.bucket.DoubleBucket;
import org.kasource.spring.console.timeseries.bucket.LongBucket;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DoubleTimeSeries implements TimeSeries<Double> {
    private final ConcurrentLinkedDeque<Bucket<Double>> valueBuckets =  new ConcurrentLinkedDeque<>();
    private final ConcurrentLinkedDeque<Bucket<Long>> timeBuckets = new ConcurrentLinkedDeque<>();

    private final int numberOfBuckets;

    private final int bucketSize;

    public DoubleTimeSeries(int numberOfBuckets, int bucketSize) {
        if (numberOfBuckets < 1) {
            throw new IllegalArgumentException("numberOfBuckets must be at least 1");
        }
        if (bucketSize < 1) {
            throw new IllegalArgumentException("bucketSize must be at least 1");
        }
        this.numberOfBuckets = numberOfBuckets;
        this.bucketSize = bucketSize;
        valueBuckets.add(new DoubleBucket(bucketSize));
        timeBuckets.add(new LongBucket(bucketSize));

    }

    @Override
    public synchronized void push(Double value) {
        Bucket<Double> firstValueBucket = valueBuckets.getFirst();
        if (!firstValueBucket.isFull()) {
            firstValueBucket.push(value);
            timeBuckets.getFirst().push(System.currentTimeMillis());
        } else if (valueBuckets.size() < numberOfBuckets) {
           valueBuckets.addFirst(new DoubleBucket(bucketSize, value));
           timeBuckets.addFirst(new LongBucket(bucketSize, System.currentTimeMillis()));
        } else { // No more buckets allowed, clear the oldest bucket and reuse it
            reuseLastBucket(value);
        }
    }

    private void reuseLastBucket(Double value) {
        Bucket<Double> lastValueBucket = valueBuckets.removeLast();
        lastValueBucket.clear();
        lastValueBucket.push(value);
        valueBuckets.addFirst(lastValueBucket);

        Bucket<Long> lastTimeBucket = timeBuckets.removeLast();
        lastTimeBucket.clear();
        lastTimeBucket.push(System.currentTimeMillis());
        timeBuckets.addFirst(lastTimeBucket);
    }

    @Override
    public Iterator<Double> iterator() {
        return new BucketsIterator<>(valueBuckets);
    }

    @Override
    public Stream<Double> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    @Override
    public Stream<Double> stream(Duration duration) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                new TimeConstraintBucketsIterator<>(valueBuckets, timeBuckets, duration),
                Spliterator.ORDERED), false);
    }

}
