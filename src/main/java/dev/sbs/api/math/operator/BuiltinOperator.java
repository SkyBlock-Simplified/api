package dev.sbs.api.math.operator;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@Getter
public enum BuiltinOperator {

    ADDITION("+", 2, true, MathOperator.PRECEDENCE_ADDITION, args -> args[0] + args[1]),
    SUBTRACTION("-", 2, true, MathOperator.PRECEDENCE_ADDITION, args -> args[0] - args[1]),
    MULTIPLICATION("*", 2, true, MathOperator.PRECEDENCE_MULTIPLICATION, args -> args[0] * args[1]),
    DIVISION("/", 2, true, MathOperator.PRECEDENCE_DIVISION, args -> {
        if (args[1] == 0d)
            throw new ArithmeticException("Division by zero!");

        return args[0] / args[1];
    }),
    POWER("^", 2, false, MathOperator.PRECEDENCE_POWER, args -> Math.pow(args[0], args[1])),
    MODULO("%", 2, true, MathOperator.PRECEDENCE_MODULO, args -> {
        if (args[1] == 0d)
            throw new ArithmeticException("Division by zero!");

        return args[0] % args[1];
    }),
    UNARY_MINUS("-", 1, false, MathOperator.PRECEDENCE_UNARY_MINUS, args -> -args[0]),
    UNARY_PLUS("+", 1, false, MathOperator.PRECEDENCE_UNARY_PLUS, args -> args[0]);

    private static final @NotNull BuiltinOperator[] VALUES = values();
    private final @NotNull MathOperator actual;

    BuiltinOperator(@NotNull String symbol, int numOperands, boolean leftAssociative, int precedence, @NotNull Function<Double[], Double> function) {
        this.actual = new MathOperator(symbol, numOperands, leftAssociative, precedence) {
            @Override
            public Double apply(Double... args) {
                return function.apply(args);
            }
        };
    }

    public static MathOperator get(char symbol, int numArguments) {
        if (symbol == '÷')
            symbol = '/';

        for (BuiltinOperator operator : VALUES) {
            if (operator.getActual().getSymbol().charAt(0) == symbol) {
                if (numArguments == operator.getActual().getNumOperands())
                    return operator.getActual();
            }
        }

        return null;
    }

}
