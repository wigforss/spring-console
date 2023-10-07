package org.kasource.spring.console.expression;

import io.micrometer.core.instrument.Tag;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.function.Function;

public class SpelTest {

    @Test
    void transform() {
        String input1 = "jvm.memory";
        String input2 = "jvm.memory{}";
        String input9 = "increase(sum(jvm.memory), 5m)";
        String input10 = "increase(sum(jvm.memory{method = 'GET', output = 'SUCCESS'}(count)))[5m]";

    }
    @Test
    void spel() throws ParseException, NoSuchMethodException {

        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("meters['jvm.memory'].tags([tag('dd', 'hh')]).value()");
        StandardEvaluationContext context = new StandardEvaluationContext(new Functions());
        context.setVariables(Map.of("jvm.memory", "data"));
        Object result = expression.getValue(context);
        System.out.println(result);
    }

    @Getter
    private static class Functions {

        public int length(String str) {
            return str.length();
        }

        public int increment(int i) {
            return i+1;
        }

        public Map<String, Meters> meters() {
           return Map.of("jvm.memory", new Meters());
        }
    }

    @Getter
    private static class Meters {
        private String name = "jvm.memory";
        private Map<String, Double> measurements = Map.of("count", 1.0D, "total", 2.0D);
        public Double count() {
            return measurements.get("count");
        }

        public Double value() {
            return measurements.get("total");
        }

        public Meters tags() {
            return this;
        }

        public Tag tag(String tagName, String expression) {
            return Tag.of(tagName, expression);
        }
    }
}
