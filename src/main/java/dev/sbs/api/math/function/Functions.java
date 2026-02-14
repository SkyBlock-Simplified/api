package dev.sbs.api.math.function;

import dev.sbs.api.util.ArrayUtil;
import dev.sbs.api.util.NumberUtil;

/**
 * Class representing the builtin functions available for use in expressions
 */
public class Functions {

    private static final int INDEX_SIN = 0;
    private static final int INDEX_COS = 1;
    private static final int INDEX_TAN = 2;
    private static final int INDEX_CSC = 3;
    private static final int INDEX_SEC = 4;
    private static final int INDEX_COT = 5;
    private static final int INDEX_SINH = 6;
    private static final int INDEX_COSH = 7;
    private static final int INDEX_TANH = 8;
    private static final int INDEX_CSCH = 9;
    private static final int INDEX_SECH = 10;
    private static final int INDEX_COTH = 11;
    private static final int INDEX_ASIN = 12;
    private static final int INDEX_ACOS = 13;
    private static final int INDEX_ATAN = 14;
    private static final int INDEX_SQRT = 15;
    private static final int INDEX_CBRT = 16;
    private static final int INDEX_ABS = 17;
    private static final int INDEX_CEIL = 18;
    private static final int INDEX_FLOOR = 19;
    private static final int INDEX_POW = 20;
    private static final int INDEX_EXP = 21;
    private static final int INDEX_EXPM1 = 22;
    private static final int INDEX_LOG10 = 23;
    private static final int INDEX_LOG2 = 24;
    private static final int INDEX_LOG = 25;
    private static final int INDEX_LOG1P = 26;
    private static final int INDEX_LOGB = 27;
    private static final int INDEX_SGN = 28;
    private static final int INDEX_TO_RADIAN = 29;
    private static final int INDEX_TO_DEGREE = 30;
    private static final int INDEX_LENGTH = 31;
    private static final int INDEX_MAX = 32;
    private static final int INDEX_MIN = 33;
    private static final int INDEX_ROUND = 34;
    private static final int INDEX_ROUNDUP = 35;
    private static final int INDEX_ROUNDDOWN = 36;

    private static final MathFunction[] BUILT_IN_FUNCTIONS = new MathFunction[37];

    static {
        BUILT_IN_FUNCTIONS[INDEX_SIN] = new MathFunction("sin") {
            @Override
            public double apply(double... args) {
                return Math.sin(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COS] = new MathFunction("cos") {
            @Override
            public double apply(double... args) {
                return Math.cos(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TAN] = new MathFunction("tan") {
            @Override
            public double apply(double... args) {
                return Math.tan(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COT] = new MathFunction("cot") {
            @Override
            public double apply(double... args) {
                double tan = Math.tan(args[0]);
                if (tan == 0d) {
                    throw new ArithmeticException("Division by zero in cotangent!");
                }
                return 1d / tan;
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG] = new MathFunction("log") {
            @Override
            public double apply(double... args) {
                return Math.log(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG2] = new MathFunction("log2") {
            @Override
            public double apply(double... args) {
                return Math.log(args[0]) / Math.log(2d);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG10] = new MathFunction("log10") {
            @Override
            public double apply(double... args) {
                return Math.log10(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG1P] = new MathFunction("log1p") {
            @Override
            public double apply(double... args) {
                return Math.log1p(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ABS] = new MathFunction("abs") {
            @Override
            public double apply(double... args) {
                return Math.abs(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ACOS] = new MathFunction("acos") {
            @Override
            public double apply(double... args) {
                return Math.acos(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ASIN] = new MathFunction("asin") {
            @Override
            public double apply(double... args) {
                return Math.asin(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ATAN] = new MathFunction("atan") {
            @Override
            public double apply(double... args) {
                return Math.atan(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CBRT] = new MathFunction("cbrt") {
            @Override
            public double apply(double... args) {
                return Math.cbrt(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_FLOOR] = new MathFunction("floor") {
            @Override
            public double apply(double... args) {
                return Math.floor(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SINH] = new MathFunction("sinh") {
            @Override
            public double apply(double... args) {
                return Math.sinh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SQRT] = new MathFunction("sqrt") {
            @Override
            public double apply(double... args) {
                return Math.sqrt(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TANH] = new MathFunction("tanh") {
            @Override
            public double apply(double... args) {
                return Math.tanh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COSH] = new MathFunction("cosh") {
            @Override
            public double apply(double... args) {
                return Math.cosh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CEIL] = new MathFunction("ceil") {
            @Override
            public double apply(double... args) {
                return Math.ceil(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_POW] = new MathFunction("pow", 2) {
            @Override
            public double apply(double... args) {
                return Math.pow(args[0], args[1]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_EXP] = new MathFunction("exp", 1) {
            @Override
            public double apply(double... args) {
                return Math.exp(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_EXPM1] = new MathFunction("expm1", 1) {
            @Override
            public double apply(double... args) {
                return Math.expm1(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SGN] = new MathFunction("signum", 1) {
            @Override
            public double apply(double... args) {
                if (args[0] > 0) {
                    return 1;
                } else if (args[0] < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CSC] = new MathFunction("csc") {
            @Override
            public double apply(double... args) {
                double sin = Math.sin(args[0]);
                if (sin == 0d) {
                    throw new ArithmeticException("Division by zero in cosecant!");
                }
                return 1d / sin;
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SEC] = new MathFunction("sec") {
            @Override
            public double apply(double... args) {
                double cos = Math.cos(args[0]);
                if (cos == 0d) {
                    throw new ArithmeticException("Division by zero in secant!");
                }
                return 1d / cos;
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CSCH] = new MathFunction("csch") {
            @Override
            public double apply(double... args) {
                //this would throw an ArithmeticException later as sinh(0) = 0
                if (args[0] == 0d) {
                    return 0;
                }

                return 1d / Math.sinh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SECH] = new MathFunction("sech") {
            @Override
            public double apply(double... args) {
                return 1d / Math.cosh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COTH] = new MathFunction("coth") {
            @Override
            public double apply(double... args) {
                return Math.cosh(args[0]) / Math.sinh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOGB] = new MathFunction("logb", 2) {
            @Override
            public double apply(double... args) {
                return Math.log(args[1]) / Math.log(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TO_RADIAN] = new MathFunction("toradian") {
            @Override
            public double apply(double... args) {
                return Math.toRadians(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TO_DEGREE] = new MathFunction("todegree") {
            @Override
            public double apply(double... args) {
                return Math.toDegrees(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LENGTH] = new MathFunction("length") {
            @Override
            public double apply(double... args) {
                return ArrayUtil.isNotEmpty(args) ? (int) (Math.log10(args[0]) + 1) : 0;
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_MAX] = new MathFunction("max", 2) {
            @Override
            public double apply(double... args) {
                return Math.max(args[0], args[1]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_MIN] = new MathFunction("min", 2) {
            @Override
            public double apply(double... args) {
                return Math.min(args[0], args[1]);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ROUND] = new MathFunction("round", 1, 2) {
            @Override
            public double apply(double... args) {
                if (args.length == 1)
                    return Math.round(args[0]);
                else
                    return NumberUtil.round(args[0], Double.valueOf(args[1]).intValue());
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ROUNDUP] = new MathFunction("roundup", 2) {
            @Override
            public double apply(double... args) {
                return NumberUtil.roundUp(args[0], Double.valueOf(args[1]).intValue());
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ROUNDDOWN] = new MathFunction("rounddown", 2) {
            @Override
            public double apply(double... args) {
                return NumberUtil.roundDown(args[0], Double.valueOf(args[1]).intValue());
            }
        };

    }

    /**
     * Get the builtin function for a given name
     *
     * @param name te name of the function
     * @return a Function instance
     */
    public static MathFunction getBuiltinFunction(final String name) {
        return switch (name) {
            case "sin" -> BUILT_IN_FUNCTIONS[INDEX_SIN];
            case "cos" -> BUILT_IN_FUNCTIONS[INDEX_COS];
            case "tan" -> BUILT_IN_FUNCTIONS[INDEX_TAN];
            case "cot" -> BUILT_IN_FUNCTIONS[INDEX_COT];
            case "asin" -> BUILT_IN_FUNCTIONS[INDEX_ASIN];
            case "acos" -> BUILT_IN_FUNCTIONS[INDEX_ACOS];
            case "atan" -> BUILT_IN_FUNCTIONS[INDEX_ATAN];
            case "sinh" -> BUILT_IN_FUNCTIONS[INDEX_SINH];
            case "cosh" -> BUILT_IN_FUNCTIONS[INDEX_COSH];
            case "tanh" -> BUILT_IN_FUNCTIONS[INDEX_TANH];
            case "abs" -> BUILT_IN_FUNCTIONS[INDEX_ABS];
            case "log" -> BUILT_IN_FUNCTIONS[INDEX_LOG];
            case "log10" -> BUILT_IN_FUNCTIONS[INDEX_LOG10];
            case "log2" -> BUILT_IN_FUNCTIONS[INDEX_LOG2];
            case "log1p" -> BUILT_IN_FUNCTIONS[INDEX_LOG1P];
            case "ceil" -> BUILT_IN_FUNCTIONS[INDEX_CEIL];
            case "floor" -> BUILT_IN_FUNCTIONS[INDEX_FLOOR];
            case "sqrt" -> BUILT_IN_FUNCTIONS[INDEX_SQRT];
            case "cbrt" -> BUILT_IN_FUNCTIONS[INDEX_CBRT];
            case "pow" -> BUILT_IN_FUNCTIONS[INDEX_POW];
            case "exp" -> BUILT_IN_FUNCTIONS[INDEX_EXP];
            case "expm1" -> BUILT_IN_FUNCTIONS[INDEX_EXPM1];
            case "signum" -> BUILT_IN_FUNCTIONS[INDEX_SGN];
            case "csc" -> BUILT_IN_FUNCTIONS[INDEX_CSC];
            case "sec" -> BUILT_IN_FUNCTIONS[INDEX_SEC];
            case "csch" -> BUILT_IN_FUNCTIONS[INDEX_CSCH];
            case "sech" -> BUILT_IN_FUNCTIONS[INDEX_SECH];
            case "coth" -> BUILT_IN_FUNCTIONS[INDEX_COTH];
            case "toradian" -> BUILT_IN_FUNCTIONS[INDEX_TO_RADIAN];
            case "todegree" -> BUILT_IN_FUNCTIONS[INDEX_TO_DEGREE];
            case "length" -> BUILT_IN_FUNCTIONS[INDEX_LENGTH];
            case "max" -> BUILT_IN_FUNCTIONS[INDEX_MAX];
            case "min" -> BUILT_IN_FUNCTIONS[INDEX_MIN];
            case "round" -> BUILT_IN_FUNCTIONS[INDEX_ROUND];
            case "roundup" -> BUILT_IN_FUNCTIONS[INDEX_ROUNDUP];
            case "rounddown" -> BUILT_IN_FUNCTIONS[INDEX_ROUNDDOWN];
            default -> null;
        };
    }

}
