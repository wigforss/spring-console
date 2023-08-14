package org.kasource.spring.console.monitor;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Meter;
import org.kasource.spring.console.config.properties.SpringConsoleConfig;

@Component
public class MeterValueMonitorManagerImpl implements MeterValueMonitorManager {

    @Autowired
    private SpringConsoleConfig springConsoleConfig;

    @Autowired
    private MeterMonitorScheduler scheduler;

    private Map<Set<Meter>, MeterValueMonitor> monitors = new ConcurrentHashMap<>();

    public MeterValueMonitor monitorFor(Collection<Meter> meters) {
        Set<Meter> meterSet = Set.copyOf(meters);
        Optional<MeterValueMonitor> monitor = Optional.ofNullable(monitors.get(meterSet));
        if (monitor.isEmpty()) {
            MeterValueMonitorImpl valueMonitor = new MeterValueMonitorImpl(
                    meters,
                    springConsoleConfig.getMonitor().getCompareScale(),
                    scheduler
            );

            monitors.put(meterSet, valueMonitor);
            monitor = Optional.of(valueMonitor);
        }
        return monitor.get();
    }




}
