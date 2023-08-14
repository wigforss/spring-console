package org.kasource.spring.console.monitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import org.kasource.spring.console.value.MeterValueChangedListener;

@Slf4j
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MeterValueMonitorImpl implements MeterValueMonitor {

    private final Collection<Meter> meters;

    private final int scale;

    private final MeterMonitorScheduler scheduler;


    private Queue<MeterValueChangedListener> valueChangedListenersQ = new ConcurrentLinkedQueue<>();

    private AtomicReference<Map<Meter, List<BigDecimal>>> previousValues = new AtomicReference<>();

    @EqualsAndHashCode.Include
    private List<String> meterIds;

    public MeterValueMonitorImpl(Collection<Meter> meters, int scale, MeterMonitorScheduler scheduler) {
        this.meters = meters;
        this.scale = scale;
        this.scheduler = scheduler;
        this.meterIds = meters.stream().map(m -> m.getId().toString()).collect(Collectors.toList());
    }

    @Override
    public void checkValues() {
        List<Meter> changedMeters = changedMeters();
        if (!changedMeters.isEmpty()) {
            publishValues(changedMeters);
        }
    }

    private List<Meter> changedMeters() {
        if (previousValues.get() == null) {
            storePreviousValues();
            return meters.stream().toList();
        }
        Map<Meter, List<BigDecimal>> previousValuesMap = previousValues.get();
        List<Meter> changedMeters = meters.stream()
                .filter(m -> !StreamSupport.stream(m.measure().spliterator(), false)
                        .map(this::toBigDecimal)
                        .collect(Collectors.toList()).equals(previousValuesMap.get(m))).collect(Collectors.toList());

        storePreviousValues();
        return changedMeters;
    }

    private void storePreviousValues() {
        Map<Meter, List<BigDecimal>> values = meters.stream().collect(Collectors.toMap(
                Function.identity(),
                m -> StreamSupport.stream(m.measure().spliterator(), false)
                        .map(this::toBigDecimal)
                        .collect(Collectors.toList())));


        previousValues.set(values);

    }

    private BigDecimal toBigDecimal(Measurement measurement) {
        try {
            return BigDecimal.valueOf(measurement.getValue()).setScale(scale, RoundingMode.HALF_DOWN);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void addValueListener(MeterValueChangedListener listener) {
        valueChangedListenersQ.add(listener);
        if (!scheduler.hasMonitor(this)) {
            start();
        }
    }

    public void removeValueListener(MeterValueChangedListener listener) {
        valueChangedListenersQ.remove(listener);
        if (valueChangedListenersQ.isEmpty()) {
            log.debug("Last listener removed for " + meterIds);
            stop();
        }
    }

    private void publishValues(List<Meter> meters) {
        valueChangedListenersQ.forEach(m -> m.onMetersValueChanged(this, meters));
    }



    private void start() {
        scheduler.addMonitor(this);
        log.debug("Started scheduling of values listener for " + meterIds);
    }

    private void stop() {
        scheduler.addMonitor(this);
        log.debug("Stopped  scheduling of values for " + meterIds);
    }


}
