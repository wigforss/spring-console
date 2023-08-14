package org.kasource.spring.console.tag;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import lombok.Getter;

public enum TagOperator {
    /** Equals (ignore case) **/
    EQ("==", TagFunctions.EQUALS, false),

    /** NOT equals (ignore case) **/
    NOT_EQ("!=", TagFunctions.EQUALS, true),

    /** Regular Expression matches **/
    REGEXP_MATCH("=~", TagFunctions.REGEXP_MATCH, false),

    /** Regular Expression NOT matches **/
    NOT_REGEXP_MATCH("!~", TagFunctions.REGEXP_MATCH, true),

    /** Numerical greater than, falls back to lexical if value or query is not numerical **/
    GREATER (">", TagFunctions.GREATER, false),

    /** Numerical greater than, falls back to lexical if value or query is not numerical **/
    GREATER_OR_EQ (">=", TagFunctions.GREATER_OR_EQUAL, false),

    /** Numerical lesser than, falls back to lexical if value or query is not numerical **/
    LESSER ("<", TagFunctions.LESSER, false),

    /** Numerical lesser than, falls back to lexical if value or query is not numerical **/
    LESSER_OR_EQ ("<=", TagFunctions.LESSER_OR_EQUAL, false);

    private static final String REG_EXP = "^[\\w\\-\\_]+\\s*%s\\s*\\w+.*$";

    @Getter
    private String operator;

    private BiFunction<String, String, Boolean> operatorFunction;

    private boolean negate;


    TagOperator(String operator, BiFunction<String, String, Boolean> operatorFunction, boolean negate) {
        this.operator = operator;
        this.operatorFunction = operatorFunction;
        this.negate = negate;
    }

    public boolean evaluate(String value, String query) {
        if (negate) {
            return !operatorFunction.apply(value, query);
        } else {
            return operatorFunction.apply(value, query);
        }
    }

    public static Optional<TagOperator> parseOperator(String queryString) {
        return Arrays.stream(values()).filter(o -> queryString.matches(String.format(REG_EXP, o.operator))).findFirst();
    }
}
