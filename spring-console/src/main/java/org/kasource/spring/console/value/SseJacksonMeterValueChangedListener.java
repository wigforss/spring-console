package org.kasource.spring.console.value;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.micrometer.core.instrument.Meter;
import org.kasource.spring.console.monitor.MeterValueMonitor;
import org.kasource.spring.console.rest.api.MeasurementsResponse;
import org.kasource.spring.console.rest.transform.ValueResponseTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SseJacksonMeterValueChangedListener implements MeterValueChangedListener {

    @EqualsAndHashCode.Include
    @NonNull
    private final SseEmitter emitter;

    @NonNull
    private final ObjectMapper objectMapper;

    @NonNull
    private final ValueResponseTransformer valueResponseTransformer;


    @Override
    public void onMetersValueChanged(MeterValueMonitor monitor, List<Meter> meters) {

        Map<String, MeasurementsResponse> response = valueResponseTransformer.toValueResponse(meters);
        try {
            log.debug("Send data to " + meters.iterator().next().getId() + " listeners ");
            emitter.send(objectMapper.writeValueAsString(response), MediaType.APPLICATION_JSON);
        } catch (IOException | IllegalStateException e) {
            log.debug("Could not publish message", e);
            stop(monitor);
        }

    }

    private void stop(MeterValueMonitor monitor) {
        monitor.removeValueListener(this);
    }
}
