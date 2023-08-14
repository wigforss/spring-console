package org.kasource.spring.console.rest.api;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

import io.micrometer.core.instrument.Meter;

@Data
public class MetersResponse {
    private String name;

    private String description;
    private String baseUnit;

    private String type;

    private List<LightMeterResponse> meters;


    public static MetersResponse fromMeters(Collection<Meter> meters) {
        Meter meter = meters.iterator().next();
        MetersResponse response = new MetersResponse();
        response.name = meter.getId().getName();
        response.description = meter.getId().getDescription();
        response.baseUnit = meter.getId().getBaseUnit();
        response.type = meter.getId().getType().name();
        response.meters = meters.stream().map(LightMeterResponse::fromMeter).collect(Collectors.toList());
        return response;
    }
}
