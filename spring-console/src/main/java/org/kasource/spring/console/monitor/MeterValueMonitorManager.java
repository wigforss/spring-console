package org.kasource.spring.console.monitor;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import io.micrometer.core.instrument.Meter;

public interface MeterValueMonitorManager {

    MeterValueMonitor monitorFor(Collection<Meter> meters);

}
