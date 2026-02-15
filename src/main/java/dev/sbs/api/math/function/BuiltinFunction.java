package dev.sbs.api.math.function;

import dev.sbs.api.util.ArrayUtil;
import dev.sbs.api.util.NumberUtil;
import dev.sbs.api.util.VarargFunction;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Class representing the builtin functions available for use in expressions
 */
@Getter
public enum BuiltinFunction {

    INDEX_SIN("sin", 1, args -> Math.sin(args[0])),
    INDEX_COS("cos", 1, args -> Math.cos(args[0])),
    INDEX_TAN("tan", 1, args -> Math.tan(args[0])),
    INDEX_CSC("csc", 1, args -> {
        double sin = Math.sin(args[0]);
        if (sin == 0d)
            throw new ArithmeticException("Division by zero in cosecant!");
        return 1d / sin;
    }),
    INDEX_SEC("sec", 1, args -> {
        double cos = Math.cos(args[0]);
        if (cos == 0d)
            throw new ArithmeticException("Division by zero in secant!");
        return 1d / cos;
    }),
    INDEX_COT("cot", 1, args -> {
        double tan = Math.tan(args[0]);
        if (tan == 0d)
            throw new ArithmeticException("Division by zero in cotangent!");
        return 1d / tan;
    }),
    INDEX_SINH( "sinh", 1, args -> Math.sinh(args[0])),
    INDEX_COSH( "cosh", 1, args -> Math.cosh(args[0])),
    INDEX_TANH( "tanh", 1, args -> Math.tanh(args[0])),
    INDEX_CSCH("csch", 1, args -> {
        // This would throw an ArithmeticException later as sinh(0) = 0
        if (args[0] == 0d)
            return 0d;

        return 1d / Math.sinh(args[0]);
    }),
    INDEX_SECH("sech", 1, args -> 1d / Math.cosh(args[0])),
    INDEX_COTH("coth", 1, args -> Math.cosh(args[0]) / Math.sinh(args[0])),
    INDEX_ASIN( "asin", 1, args -> Math.asin(args[0])),
    INDEX_ACOS( "acos", 1, args -> Math.acos(args[0])),
    INDEX_ATAN( "atan", 1, args -> Math.atan(args[0])),
    INDEX_SQRT( "sqrt", 1, args -> Math.sqrt(args[0])),
    INDEX_CBRT( "cbrt", 1, args -> Math.cbrt(args[0])),
    INDEX_ABS( "abs", 1, args -> Math.abs(args[0])),
    INDEX_CEIL( "ceil", 1, args -> Math.ceil(args[0])),
    INDEX_FLOOR( "floor", 1, args -> Math.floor(args[0])),
    INDEX_POW( "pow", 2, args -> Math.pow(args[0], args[1])),
    INDEX_EXP( "exp", 1, args -> Math.exp(args[0])),
    INDEX_EXPM1( "expm1", 1, args -> Math.expm1(args[0])),
    INDEX_LOG10( "log10", 1, args -> Math.log10(args[0])),
    INDEX_LOG2( "log2", 1, args -> Math.log(args[0]) / Math.log(2d)),
    INDEX_LOG( "log", 1, args -> Math.log(args[0])),
    INDEX_LOG1P( "log1p", 1, args -> Math.log1p(args[0])),
    INDEX_LOGB("logb", 2, args -> Math.log(args[1]) / Math.log(args[0])),
    INDEX_SGN("signum", 1, args -> {
        if (args[0] > 0)
            return 1d;
        else if (args[0] < 0)
            return -1d;
        else
            return 0d;
    }),
    INDEX_TO_RADIAN("toradian", 1, args -> Math.toRadians(args[0])),
    INDEX_TO_DEGREE("todegree", 1, args -> Math.toDegrees(args[0])),
    INDEX_LENGTH("length", 1, args -> ArrayUtil.isNotEmpty(args) ? (int) (Math.log10(args[0]) + 1) : 0d),
    INDEX_MAX("max", 1, 100, args -> {
        double value = args[0];

        for (int i = 1; i < args.length; i++)
            value = Math.max(value, args[i]);

        return value;
    }),
    INDEX_MIN("min", 1, 100, args -> {
        double value = args[0];

        for (int i = 1; i < args.length; i++)
            value = Math.min(value, args[i]);

        return value;
    }),
    INDEX_SUM("sum", 1, 100, args -> {
        double value = args[0];

        for (int i = 1; i < args.length; i++)
            value += args[i];

        return value;
    }),
    INDEX_AVG("avg", 1, 100, args -> {
        double value = args[0];

        for (int i = 1; i < args.length; i++)
            value += args[i];

        return value / args.length;
    }),
    INDEX_ROUND("round", 1, 2, args -> {
        if (args.length == 1)
            return (double) Math.round(args[0]);
        else
            return NumberUtil.round(args[0], args[1].intValue());
    }),
    INDEX_ROUNDUP("roundup", 1, args -> (double) NumberUtil.roundUp(args[0], args[1].intValue())),
    INDEX_ROUNDDOWN("rounddown", 1, args -> (double) NumberUtil.roundDown(args[0], args[1].intValue()));

    private static final BuiltinFunction[] VALUES = values();
    private final @NotNull MathFunction actual;

    BuiltinFunction(@NotNull String name, int numArguments, @NotNull VarargFunction<Double, Double> function) {
        this(name, numArguments, numArguments, function);
    }

    BuiltinFunction(@NotNull String name, int minArguments, int maxArguments, @NotNull VarargFunction<Double, Double> function) {
        this.actual = new MathFunction(name, minArguments, maxArguments) {
            @Override
            public Double apply(Double... args) {
                return function.apply(args);
            }
        };
    }

    /**
     * Get the builtin function for a given name
     *
     * @param name the name of the function
     * @return a Function instance
     */
    public static MathFunction get(@NotNull String name) {
        for (BuiltinFunction function : VALUES) {
            if (function.getActual().getName().equalsIgnoreCase(name))
                return function.getActual();
        }

        return null;
    }

}
