package org.kasource.spring.console.rest.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.Data;

import org.springframework.util.StreamUtils;

import io.micrometer.core.instrument.Meter;

@Data
public class MeasurementsResponse {
    private Map<String, MeasurementResponse> measurements;

    public static MeasurementsResponse fromMeter(Meter meter) {
        MeasurementsResponse measurements = new MeasurementsResponse();
        measurements.measurements = StreamSupport
                .stream(meter.measure().spliterator(), false)
                .collect(Collectors.toMap(
                        m -> m.getStatistic().getTagValueRepresentation(),
                        MeasurementResponse::from
                ));
        return measurements;
    }
}
