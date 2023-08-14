package org.kasource.spring.console.rest.api;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import lombok.Data;

import io.micrometer.core.instrument.Measurement;

@Data
public class MeasurementResponse {
    private Double value;



    public static MeasurementResponse from(Measurement measurement) {
        MeasurementResponse response = new MeasurementResponse();
        response.value = measurement.getValue();
        return response;
    }
}
