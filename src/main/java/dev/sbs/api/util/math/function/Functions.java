package dev.sbs.api.util.math.function;

import dev.sbs.api.util.helper.ArrayUtil;

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

    private static final MathFunction[] BUILT_IN_FUNCTIONS = new MathFunction[34];

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

    }

    /**
     * Get the builtin function for a given name
     *
     * @param name te name of the function
     * @return a Function instance
     */
    public static MathFunction getBuiltinFunction(final String name) {

        switch (name) {
            case "sin":
                return BUILT_IN_FUNCTIONS[INDEX_SIN];
            case "cos":
                return BUILT_IN_FUNCTIONS[INDEX_COS];
            case "tan":
                return BUILT_IN_FUNCTIONS[INDEX_TAN];
            case "cot":
                return BUILT_IN_FUNCTIONS[INDEX_COT];
            case "asin":
                return BUILT_IN_FUNCTIONS[INDEX_ASIN];
            case "acos":
                return BUILT_IN_FUNCTIONS[INDEX_ACOS];
            case "atan":
                return BUILT_IN_FUNCTIONS[INDEX_ATAN];
            case "sinh":
                return BUILT_IN_FUNCTIONS[INDEX_SINH];
            case "cosh":
                return BUILT_IN_FUNCTIONS[INDEX_COSH];
            case "tanh":
                return BUILT_IN_FUNCTIONS[INDEX_TANH];
            case "abs":
                return BUILT_IN_FUNCTIONS[INDEX_ABS];
            case "log":
                return BUILT_IN_FUNCTIONS[INDEX_LOG];
            case "log10":
                return BUILT_IN_FUNCTIONS[INDEX_LOG10];
            case "log2":
                return BUILT_IN_FUNCTIONS[INDEX_LOG2];
            case "log1p":
                return BUILT_IN_FUNCTIONS[INDEX_LOG1P];
            case "ceil":
                return BUILT_IN_FUNCTIONS[INDEX_CEIL];
            case "floor":
                return BUILT_IN_FUNCTIONS[INDEX_FLOOR];
            case "sqrt":
                return BUILT_IN_FUNCTIONS[INDEX_SQRT];
            case "cbrt":
                return BUILT_IN_FUNCTIONS[INDEX_CBRT];
            case "pow":
                return BUILT_IN_FUNCTIONS[INDEX_POW];
            case "exp":
                return BUILT_IN_FUNCTIONS[INDEX_EXP];
            case "expm1":
                return BUILT_IN_FUNCTIONS[INDEX_EXPM1];
            case "signum":
                return BUILT_IN_FUNCTIONS[INDEX_SGN];
            case "csc":
                return BUILT_IN_FUNCTIONS[INDEX_CSC];
            case "sec":
                return BUILT_IN_FUNCTIONS[INDEX_SEC];
            case "csch":
                return BUILT_IN_FUNCTIONS[INDEX_CSCH];
            case "sech":
                return BUILT_IN_FUNCTIONS[INDEX_SECH];
            case "coth":
                return BUILT_IN_FUNCTIONS[INDEX_COTH];
            case "toradian":
                return BUILT_IN_FUNCTIONS[INDEX_TO_RADIAN];
            case "todegree":
                return BUILT_IN_FUNCTIONS[INDEX_TO_DEGREE];
            case "length":
                return BUILT_IN_FUNCTIONS[INDEX_LENGTH];
            case "max":
                return BUILT_IN_FUNCTIONS[INDEX_MAX];
            case "min":
                return BUILT_IN_FUNCTIONS[INDEX_MIN];
            default:
                return null;
        }
    }

}
