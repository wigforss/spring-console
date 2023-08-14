package org.kasource.spring.console.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;

public interface MeterService {

    List<Meter> findAll();

    Meter findMeter(String name);

    Meter findMeter(String name, Set<String> tagExpressions);

    Collection<Meter> findMeters(String name);

    Collection<Meter> findMeters(String name, Set<String> tagExpressions);



}
