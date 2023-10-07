package org.kasource.spring.console.timeseries.bucket;

import java.util.stream.Stream;

public interface Bucket<T> extends Iterable<T> {
    void push(T value);

    T get(int index);

    T getCurrent();

    boolean isEmpty();

    boolean isFull();

    int getIndex();

    int findIndex(T value, boolean inclusive);

    void clear();

    Stream<T> stream();

    Stream<T> stream(int index);
}
