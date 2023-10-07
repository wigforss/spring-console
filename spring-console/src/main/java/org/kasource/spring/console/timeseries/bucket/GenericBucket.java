package org.kasource.spring.console.timeseries.bucket;

import org.kasource.spring.console.timeseries.BucketFullException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GenericBucket<T> implements Bucket<T> {

    private final T[] data;

    private final AtomicInteger currentIndex = new AtomicInteger(-1);
    public GenericBucket(T[] data) {
        this.data = data;
    }

    public GenericBucket(T[] data, T... initialData) {
        this.data = data;
        for (T value : initialData) {
            push(value);
        }
    }

    @Override
    public void push(T value) {
        if (!isFull()) {
            data[currentIndex.incrementAndGet()] = value;
        } else {
            throw new BucketFullException("Can't push more data into bucket");
        }
    }

    @Override
    public T get(int index) {
        return data[index];
    }

    @Override
    public T getCurrent() {
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
     * Note: T must implements Comparable, this method will only work correctly on ordered and non-duplicate data, like timestamps.
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
    public int findIndex(T value, boolean inclusive) {
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
    public Iterator<T> iterator() {
        return new GenericValueBucketIterator<>(data, currentIndex.get());
    }

    @Override
    public Stream<T> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    @Override
    public Stream<T> stream(int index) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                new GenericValueBucketIterator(data, index),
                Spliterator.ORDERED), false);
    }

    private static class GenericValueBucketIterator<T> implements Iterator<T> {
        private final T[] data;
        private int currentIndex;

        private int stopIndex;

        private GenericValueBucketIterator(final T[] data, int currentIndex) {
            this.data = data;
            this.currentIndex =currentIndex;
        }

        private GenericValueBucketIterator(final T[] data, int currentIndex, int stopIndex) {
            this(data, currentIndex);
            this.stopIndex = stopIndex;
        }

        @Override
        public boolean hasNext() {
            return currentIndex >= stopIndex;
        }

        @Override
        public T next() {
            T value = data[currentIndex];
            currentIndex--;
            return value;
        }
    }
}
