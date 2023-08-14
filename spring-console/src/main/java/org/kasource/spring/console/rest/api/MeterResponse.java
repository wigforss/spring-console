package org.kasource.spring.console.rest.api;

import java.util.stream.Collectors;

import lombok.Data;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;

@Data
public class MeterResponse {

    private String id;
    private String name;

    private String description;
    private String baseUnit;

    private String type;

    private String tags;

    public static MeterResponse fromMeter(Meter meter) {
        MeterResponse response = new MeterResponse();
        response.id = meter.getId().toString();
        response.name = meter.getId().getName();
        response.description = meter.getId().getDescription();
        response.baseUnit = meter.getId().getBaseUnit();
        response.type = meter.getId().getType().name();
        response.tags = meter.getId().getTags().stream()
                .map(t -> t.getKey() + "=" + t.getValue())
                .collect(Collectors.joining(","));
        return response;
    }
}
