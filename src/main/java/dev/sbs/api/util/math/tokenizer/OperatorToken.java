package dev.sbs.api.util.math.tokenizer;

import dev.sbs.api.util.math.operator.Operator;
import lombok.Getter;

/**
 * Represents an operator used in expressions
 */
public class OperatorToken extends Token {

    /**
     * Get the operator for that token
     */
    @Getter private final Operator operator;

    /**
     * Create a new instance
     *
     * @param op the operator
     */
    public OperatorToken(Operator op) {
        super(Token.TOKEN_OPERATOR);
        if (op == null) {
            throw new IllegalArgumentException("Operator is unknown for token.");
        }
        this.operator = op;
    }

}
