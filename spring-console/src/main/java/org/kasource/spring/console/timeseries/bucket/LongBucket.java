package org.kasource.spring.console.timeseries.bucket;

import org.kasource.spring.console.timeseries.BucketFullException;
import org.kasource.spring.console.timeseries.bucket.Bucket;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LongBucket implements Bucket<Long> {

    private final long[] data;

    private final AtomicInteger currentIndex = new AtomicInteger(-1);
    public LongBucket(int capacity) {
        data = new long[capacity];
    }

    public LongBucket(int capacity, long... initialData) {
        data = new long[capacity];
        for (long value : initialData) {
            push(value);
        }
    }

    @Override
    public void push(Long value) {
        if (!isFull()) {
            data[currentIndex.incrementAndGet()] = value;
        } else {
            throw new BucketFullException("Can't push more data into bucket");
        }
    }

    @Override
    public Long get(int index) {
        return data[index];
    }

    @Override
    public Long getCurrent() {
        return data[currentIndex.get()];
    }

    @Override
    public boolean isEmpty() {
        return currentIndex.get() == -1;
    }

    @Override
    public boolean isFull() {
        return (currentIndex.get() + 1) >= data.length;
    }

    @Override
    public int getIndex() {
        return currentIndex.get();
    }

    /**
     * Find index of value or closest index.
     * Note: This method will only work correctly on ordered and non-duplicate data, like timestamps.
     *
     * If no exact match found the closest index will be returned, inclusive will return the closest index higher than value and
     * non-exclusive will return the closest index lower than the value.
     *
     * @param value         Value to find
     * @param inclusive     If no match include the index higher than the value else the index lower than the value
     *
     * @return              Index of the value or the closest index
     **/
    @Override
    public int findIndex(Long value, boolean inclusive) {
        int index = Arrays.binarySearch(data, 0, currentIndex.get(), value);
        if (index >= 0) {
            return index;
        } else {
            if (inclusive) {
                return -index - 1;
            } else {
                return -index - 2;
            }
        }
    }

    @Override
    public void clear() {
        currentIndex.set(-1);
    }

    @Override
    public Stream<Long> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    @Override
    public Stream<Long> stream(int index) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                new LongBucketIterator(data, index),
                Spliterator.ORDERED), false);
    }

    @Override
    public Iterator<Long> iterator() {
        return new LongBucketIterator(data, currentIndex.get());
    }

    private static class LongBucketIterator implements Iterator<Long> {
        private final long[] data;
        private int currentIndex;

        private int stopIndex;

        private LongBucketIterator(final long[] data, int currentIndex) {
            this.data = data;
            this.currentIndex = currentIndex;

        }
         private LongBucketIterator(final long[] data, int currentIndex, int stopIndex) {
            this(data, currentIndex);
            this.stopIndex = stopIndex;
         }

        @Override
        public boolean hasNext() {
            return currentIndex >= stopIndex;
        }

        @Override
        public Long next() {
            long value = data[currentIndex];
            currentIndex--;
            return value;
        }
    }
}
