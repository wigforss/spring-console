package org.kasource.spring.console.rest.transform;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Meter;
import org.kasource.spring.console.rest.api.MeasurementsResponse;

@Component
public class ValueResponseTransformer {

    public Map<String, MeasurementsResponse> toValueResponse(Collection<Meter> meters) {
        return meters
                .stream()
                .collect(Collectors.toMap(
                        m -> {
                            if (m.getId().getTags().isEmpty()) {
                                return "default";
                            } else {
                                return m.getId().getTags().stream()
                                        .map(t -> t.getKey() + "=" + t.getValue()).collect(Collectors.joining(","));
                            }
                        },
                        m -> MeasurementsResponse.fromMeter(m)));
    }
}
