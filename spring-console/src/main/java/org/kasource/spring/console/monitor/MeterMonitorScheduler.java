package org.kasource.spring.console.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import org.kasource.spring.console.config.properties.SpringConsoleConfig;


@Component
public class MeterMonitorScheduler implements SmartLifecycle {

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    private volatile boolean isRunning;

    private Map<MeterValueMonitor, Boolean> monitors = new ConcurrentHashMap<>();

    @Autowired
    private SpringConsoleConfig springConsoleConfig;

    @Override
    public void start() {
        taskScheduler.initialize();
        isRunning = true;
        taskScheduler.scheduleWithFixedDelay(this::evaluateMonitors, springConsoleConfig.getMonitor().getPollInterval());
    }

    @Override
    public void stop() {
        taskScheduler.shutdown();
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    private void evaluateMonitors() {
        monitors.keySet().forEach(m -> m.checkValues());
    }

    public void addMonitor(MeterValueMonitor monitor) {
        monitors.put(monitor, Boolean.TRUE);
    }

    public boolean hasMonitor(MeterValueMonitor monitor) {
        return monitors.containsKey(monitor);
    }

    public void removeMonitor(MeterValueMonitor monitor) {
        monitors.remove(monitor);
    }
}
