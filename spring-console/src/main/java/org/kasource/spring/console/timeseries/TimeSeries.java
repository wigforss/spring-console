package org.kasource.spring.console.timeseries;

import java.time.Duration;
import java.util.stream.Stream;

public interface TimeSeries<T> extends Iterable<T> {
    void push(T value);

    Stream<T> stream();

    Stream<T> stream(Duration duration);

}
