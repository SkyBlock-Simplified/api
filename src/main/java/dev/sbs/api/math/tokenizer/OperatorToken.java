package dev.sbs.api.math.tokenizer;

import dev.sbs.api.math.operator.MathOperator;
import lombok.Getter;

/**
 * Represents an operator used in expressions
 */
public class OperatorToken extends Token {

    /**
     * Get the operator for that token
     */
    @Getter
    private final MathOperator operator;

    /**
     * Create a new instance
     *
     * @param op the operator
     */
    public OperatorToken(MathOperator op) {
        super(Token.TOKEN_OPERATOR);
        if (op == null) {
            throw new IllegalArgumentException("Operator is unknown for token.");
        }
        this.operator = op;
    }

}
