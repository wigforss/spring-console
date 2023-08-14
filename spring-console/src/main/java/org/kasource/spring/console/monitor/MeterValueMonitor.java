package org.kasource.spring.console.monitor;

import org.kasource.spring.console.value.MeterValueChangedListener;

public interface MeterValueMonitor {


    void checkValues();
    void addValueListener(MeterValueChangedListener listener);

    void removeValueListener(MeterValueChangedListener listener);

}
