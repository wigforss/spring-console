package org.kasource.spring.console.timeseries;

import org.kasource.spring.console.timeseries.bucket.Bucket;
import org.kasource.spring.console.timeseries.bucket.LongBucket;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TimeConstraintBucketsIterator<T> implements Iterator<T> {

    private Iterator<Bucket<T>> bucketIterator;
    private Iterator<T> currentIterator;
    private List<Bucket<Long>> timeBuckets;

    private int currentIndex;

    //private Bucket<T> nextBucket;

    private long cutoffTime;

    private boolean timeout;
    public TimeConstraintBucketsIterator(Deque<Bucket<T>> valueBuckets,
                                         Deque<Bucket<Long>> timeBuckets,
                                         Duration duration) {

        initialize(valueBuckets, timeBuckets, duration);

    }

    private void initialize(Deque<Bucket<T>> valueBuckets, Deque<Bucket<Long>> timeBuckets, Duration duration) {
        this.timeBuckets = new ArrayList<>(timeBuckets);
        this.cutoffTime = System.currentTimeMillis() - duration.toMillis();
        this.bucketIterator = valueBuckets.iterator();
        // Check bucket
        Bucket<T> nextBucket = bucketIterator.next();

        setNextBucketIterator(nextBucket);
    }

    private void setNextBucketIterator(Bucket<T> valueBucket) {
        Bucket<Long> timeBucket = this.timeBuckets.get(currentIndex);
        if (bucketTimeout(valueBucket, timeBucket)) {
            timeout = true;
            int cutoffIndex = timeBucket.findIndex(cutoffTime, true);
            currentIterator = valueBucket.stream(cutoffIndex).iterator();
        } else {
            currentIterator = valueBucket.iterator();
        }
    }

    private boolean bucketTimeout(Bucket<T> valueBucket, Bucket<Long> timeBucket) {
        long firstTimestamp = timeBucket.get(0);
        return firstTimestamp < cutoffTime;
    }


    @Override
    public boolean hasNext() {

        if (currentIterator.hasNext()) {
            return true;
        } else {
            if (timeout) {
                return false;
            }
            if (bucketIterator.hasNext()) {
                Bucket<T> nextBucket = bucketIterator.next();
                currentIndex++;
                setNextBucketIterator(nextBucket);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public T next() {
        return currentIterator.next();
    }
}
