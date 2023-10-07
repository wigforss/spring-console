package org.kasource.spring.console.timeseries;

public class BucketFullException extends RuntimeException {

    public BucketFullException(String message) {
        super(message);
    }

}
