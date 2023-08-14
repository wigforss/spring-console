package org.kasource.spring.console.rest.controller;

import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import org.kasource.spring.console.config.properties.SpringConsoleConfig;
import org.kasource.spring.console.monitor.MeterValueMonitor;
import org.kasource.spring.console.rest.api.MeasurementsResponse;
import org.kasource.spring.console.rest.api.MeterResponse;
import org.kasource.spring.console.rest.api.MetersResponse;
import org.kasource.spring.console.service.MeterService;
import org.kasource.spring.console.monitor.MeterValueMonitorManager;
import org.kasource.spring.console.value.SseJacksonMeterValueChangedListener;
import org.kasource.spring.console.rest.transform.ValueResponseTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin
@Controller
@RequestMapping(path = "/meters")
public class MeterController {

    @Autowired
    private MeterService meterService;

    @Autowired
    private MeterValueMonitorManager monitorManager;

    @Autowired
    private SpringConsoleConfig config;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ValueResponseTransformer valueResponseTransformer;

    private long sseTimeout = -1L;

    @PostConstruct
    void readConfig() {
        sseTimeout = config.getEventPublishing().getTimeoutMillis();
    }

    @Timed
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, MetersResponse> allMeters() {
        Map<String, List<Meter>> groupedMeters = meterService.findAll().stream().collect(groupingBy(m -> (m.getId().getName())));
        return groupedMeters.entrySet().stream().collect(toMap(
                Map.Entry::getKey,
                e -> MetersResponse.fromMeters(e.getValue())
        ));
    }

    @Timed
    @GetMapping(path = "/names", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> allMeterNames() {
        return meterService.findAll().stream().map(m -> m.getId().getName()).sorted().distinct().collect(toList());
    }

    @Timed
    @GetMapping(path = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<MeterResponse> meters(@PathVariable String name) {
        Collection<Meter> meters = meterService.findMeters(name);
        return meters.stream().map(MeterResponse::fromMeter).collect(toList());
    }

    @Timed
    @GetMapping(path = "/{name}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Set<String> metersTags(@PathVariable String name) {
        Collection<Meter> meters = meterService.findMeters(name);
        return meters.stream().flatMap(m -> m.getId().getTags().stream().map(Tag::getKey)).collect(toSet());
    }

    @Timed
    @GetMapping(path = "/{name}/tags/{tagName}/values", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<String> metersTagValues(@PathVariable String name, @PathVariable String tagName) {
        Collection<Meter> meters = meterService.findMeters(name);
        return meters.stream().map(m -> m.getId().getTag(tagName)).sorted().distinct().collect(toList());
    }

    @Timed
    @GetMapping(path = "/{name}/{tagExpressions}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<MeterResponse> meters(@PathVariable String name, @PathVariable Set<String> tagExpressions) {

        Collection<Meter> meters = meterService.findMeters(name, tagExpressions);

        return meters.stream().map(MeterResponse::fromMeter).collect(toList());
    }

    @Timed
    @GetMapping(path = "/{name}/value", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, MeasurementsResponse> metersValue(@PathVariable String name) {
        Collection<Meter> meters = meterService.findMeters(name);

        return valueResponseTransformer.toValueResponse(meters);
    }

    @Timed
    @GetMapping(path = "/{name}/{tagExpressions}/value", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, MeasurementsResponse> meterValue(@PathVariable String name, @PathVariable Set<String> tagExpressions) {

        Collection<Meter> meters = meterService.findMeters(name , tagExpressions);

        return valueResponseTransformer.toValueResponse(meters);
    }

    @GetMapping(path = "/{name}/value/events")
    public SseEmitter meterValuesEvents(@PathVariable String name) {
        Collection<Meter> meters = meterService.findMeters(name);
        MeterValueMonitor monitor = monitorManager.monitorFor(meters);

        SseEmitter sseEmitter = new SseEmitter(sseTimeout);
        monitor.addValueListener(new SseJacksonMeterValueChangedListener(sseEmitter, objectMapper, valueResponseTransformer));
        return sseEmitter;
    }

    @GetMapping(path = "/{name}/{tagExpressions}/value/events")
    public SseEmitter meterValuesEvents(@PathVariable String name, @PathVariable Set<String> tagExpressions) {


        Collection<Meter> meters = meterService.findMeters(name , tagExpressions);
        MeterValueMonitor monitor = monitorManager.monitorFor(meters);

        SseEmitter sseEmitter = new SseEmitter(sseTimeout);
        monitor.addValueListener(new SseJacksonMeterValueChangedListener(sseEmitter, objectMapper, valueResponseTransformer));
        return sseEmitter;
    }





}
