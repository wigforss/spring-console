package org.kasource.spring.console.rest.api;

import java.util.stream.Collectors;

import lombok.Data;

import io.micrometer.core.instrument.Meter;

@Data
public class LightMeterResponse {
    private String id;

    private String tags;

    public static LightMeterResponse fromMeter(Meter meter) {
        LightMeterResponse response = new LightMeterResponse();
        response.id = meter.getId().toString();
        response.tags = meter.getId().getTags().stream()
                .map(t -> t.getKey() + "=" + t.getValue())
                .collect(Collectors.joining(","));
        return response;
    }
}
