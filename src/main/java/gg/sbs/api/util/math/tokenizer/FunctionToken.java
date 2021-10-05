package gg.sbs.api.util.math.tokenizer;

import gg.sbs.api.util.math.function.Function;

public class FunctionToken extends Token {

    private final Function function;

    public FunctionToken(final Function function) {
        super(Token.TOKEN_FUNCTION);
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

}