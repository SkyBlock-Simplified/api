package dev.sbs.api.util.math.tokenizer;

import dev.sbs.api.util.math.function.MathFunction;
import lombok.Getter;

public class FunctionToken extends Token {

    @Getter
    private final MathFunction function;
    @Getter
    int argumentCount;

    public FunctionToken(final MathFunction function) {
        super(TOKEN_FUNCTION);
        this.function = function;
        this.argumentCount = 1;
    }

    public void incrementArgument() {
        this.argumentCount++;
    }

}
