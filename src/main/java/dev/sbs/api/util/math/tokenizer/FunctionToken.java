package dev.sbs.api.util.math.tokenizer;

import dev.sbs.api.util.math.function.Function;
import lombok.Getter;

public class FunctionToken extends Token {

    @Getter private final Function function;
    @Getter int argumentCount;

    public FunctionToken(final Function function) {
        super(TOKEN_FUNCTION);
        this.function = function;
        this.argumentCount = 1;
    }

    public void incrementArgument() {
        this.argumentCount++;
    }

}