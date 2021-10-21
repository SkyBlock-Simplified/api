package dev.sbs.api.util.math.tokenizer;

import lombok.Getter;

/**
 * Represents a number in the expression
 */
public final class NumberToken extends Token {

    /**
     * Get the value of the number
     */
    @Getter private final double value;

    /**
     * Create a new instance
     *
     * @param value the value of the number
     */
    public NumberToken(double value) {
        super(TOKEN_NUMBER);
        this.value = value;
    }

    NumberToken(final char[] expression, final int offset, final int len) {
        this(Double.parseDouble(String.valueOf(expression, offset, len)));
    }

}