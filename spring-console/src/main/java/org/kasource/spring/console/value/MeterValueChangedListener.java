package org.kasource.spring.console.value;

import java.util.List;

import io.micrometer.core.instrument.Meter;
import org.kasource.spring.console.monitor.MeterValueMonitor;

public interface MeterValueChangedListener {

    void onMetersValueChanged(MeterValueMonitor monitor, List<Meter> meters);
}
