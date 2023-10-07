package org.kasource.spring.console.timeseries;

import org.kasource.spring.console.timeseries.bucket.Bucket;

import java.time.Duration;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BucketsIterator<T> implements Iterator<T> {

    private final Iterator<Bucket<T>> bucketIterator;
    private Iterator<T> currentIterator;
    public BucketsIterator(Deque<Bucket<T>> buckets) {
        // Given we are guaranteed at least one bucket.
        this.bucketIterator = buckets.iterator();
        this.currentIterator = bucketIterator.next().iterator();
    }


    @Override
    public boolean hasNext() {
        if (currentIterator.hasNext()) {
            return true;
        } else {
            if (bucketIterator.hasNext()) {
                currentIterator = bucketIterator.next().iterator();
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
