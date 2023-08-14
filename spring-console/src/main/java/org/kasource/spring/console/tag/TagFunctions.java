package org.kasource.spring.console.tag;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import io.micrometer.core.instrument.Meter;

public class TagFunctions {

    public static final BiFunction<String, String, Boolean> EQUALS = (v, q) -> v.equalsIgnoreCase(q);

    public static final BiFunction<String, String, Boolean> REGEXP_MATCH = (v, q) -> v.matches(q);

    public static final BiFunction<String, String, Boolean> GREATER = (v, q) -> {
        try {
            BigDecimal numericalValue = BigDecimal.valueOf(Double.valueOf(v));
            BigDecimal numericalQuery = BigDecimal.valueOf(Double.valueOf(q));
            return numericalValue.compareTo(numericalQuery) > 0;
        } catch (NumberFormatException e) {
            return q.compareTo(v) > 0;
        }
    };

    public static final BiFunction<String, String, Boolean> GREATER_OR_EQUAL = (v, q) -> {
        try {
            BigDecimal numericalValue = BigDecimal.valueOf(Double.valueOf(v));
            BigDecimal numericalQuery = BigDecimal.valueOf(Double.valueOf(q));
            return numericalValue.compareTo(numericalQuery) >= 0;
        } catch (NumberFormatException e) {
            return v.compareTo(q) >= 0;
        }
    };

    public static final BiFunction<String, String, Boolean> LESSER = (v, q) -> {
        try {
            BigDecimal numericalValue = BigDecimal.valueOf(Double.valueOf(v));
            BigDecimal numericalQuery = BigDecimal.valueOf(Double.valueOf(q));
            return numericalValue.compareTo(numericalQuery) < 0;
        } catch (NumberFormatException e) {
            return v.compareTo(q) > 0;
        }
    };

    public static final BiFunction<String, String, Boolean> LESSER_OR_EQUAL = (v, q) -> {
        try {
            BigDecimal numericalValue = BigDecimal.valueOf(Double.valueOf(v));
            BigDecimal numericalQuery = BigDecimal.valueOf(Double.valueOf(q));
            return numericalValue.compareTo(numericalQuery) <= 0;
        } catch (NumberFormatException e) {
            return v.compareTo(q) >= 0;
        }
    };




}
