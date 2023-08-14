package org.kasource.spring.console.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.search.Search;
import org.kasource.spring.console.exception.MeterNotFoundException;
import org.kasource.spring.console.exception.NoUniqueMeterException;
import org.kasource.spring.console.tag.TagPredicate;

@Service
public class MeterServiceImpl implements MeterService {

    @Autowired
    private MeterRegistry meterRegistry;


    @Override
    public List<Meter> findAll() {
        return meterRegistry.getMeters();
    }

    public Meter findMeter(String name) {
        return findMeter(name, null);
    }

    public Meter findMeter(String name, Set<String> tagExpressions) {

        Collection<Meter> meters = findMeters(name, tagExpressions);

        if (meters.size() != 1) {
            throw new NoUniqueMeterException("There's multiple meters named " + name + " with tags expressions"
                    + tagExpressions + " using different tags, please specify a tag(s)");
        }
        return meters.iterator().next();
    }

    @Override
    public Collection<Meter> findMeters(String name) {
        Search search = meterRegistry.find(name);
        Collection<Meter> meters = search.meters();
        if (meters.isEmpty()) {
            throw new MeterNotFoundException("Did not find any Meter named " + name);
        }
        return meters;
    }


    @Override
    public Collection<Meter> findMeters(String name, Set<String> tagExpressions) {
        Set<Tag> tags = parseTags(tagExpressions);
        List<TagPredicate> tagPredicates = TagPredicate.parsePredicates(tagExpressions);
        Search search = meterRegistry.find(name);
        if (!tags.isEmpty()) {
            search = search.tags(tags);
        }
        Collection<Meter> meters = search.meters();

        meters = meters.stream().filter(m -> tagPredicates.stream().allMatch(p -> p.test(m))).collect(Collectors.toList());


        if (meters.isEmpty()) {
            throw new MeterNotFoundException("Meter named " + name + " with tags expressions" + tagExpressions);
        }
        return meters;
    }

    private Set<Tag> parseTags(Collection<String> tags) {
        if (tags == null) {
            return Collections.EMPTY_SET;
        }
        return tags.stream().filter(tq -> tq.matches(TagPredicate.IS_TAG_REG_EXP))
                .map(t -> {
                    String[] parts = t.split("=");
                    return Tag.of(parts[0].trim(), parts[1].trim());
                })
                .collect(Collectors.toSet());
    }




}
