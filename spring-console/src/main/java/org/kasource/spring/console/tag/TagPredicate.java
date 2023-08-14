package org.kasource.spring.console.tag;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import io.micrometer.core.instrument.Meter;
import org.kasource.spring.console.exception.IllegalTagOperatorException;
import org.kasource.spring.console.exception.IllegalTagQueryException;

@RequiredArgsConstructor
public class TagPredicate implements Predicate<Meter> {
    public static final String IS_TAG_REG_EXP = "^[\\w\\-\\_\\.]+\\s*=\\s*\\w+.*$";
    private final String tagName;
    private final TagOperator operator;

    private final String query;

    public static List<TagPredicate> parsePredicates(Collection<String> tagQueries) {
       return tagQueries.stream()
               .filter(tq -> !tq.matches(IS_TAG_REG_EXP))
               .map(TagPredicate::parsePredicate).collect(Collectors.toList());
    }

    public static TagPredicate parsePredicate(String tagQuery) {
        Optional<TagOperator> operatorFound = TagOperator.parseOperator(tagQuery);
        if (operatorFound.isEmpty()) {
            throw new IllegalTagOperatorException("Could not find any valid tag operator. Supported operators are: "
                    + Arrays.stream(TagOperator.values()).map(TagOperator::getOperator).collect(Collectors.joining(",")));
        } else {
            TagOperator tagOperator = operatorFound.get();

            String[] parts = tagQuery.split(Pattern.quote(tagOperator.getOperator()));

            if (parts.length == 0) {
                throw new IllegalTagQueryException("Could not find value on the left side of " + operatorFound.get().getOperator());
            }
            if (parts.length == 1) {
                throw new IllegalTagQueryException("Could not find value on right the side of " + operatorFound.get().getOperator());
            }
            if (parts.length > 2) {
                throw new IllegalTagQueryException("Operator " + operatorFound.get().getOperator() + " found more than once in expression");
            }

            return new TagPredicate(parts[0].trim(), operatorFound.get(), parts[1].trim());
        }
    }

    @Override
    public boolean test(Meter meter) {
        String tagValue = meter.getId().getTag(tagName);
        if (tagValue == null) {
            return false;
        }
        return operator.evaluate(tagValue, query);
    }
}
