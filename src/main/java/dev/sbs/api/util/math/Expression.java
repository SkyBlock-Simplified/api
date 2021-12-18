package dev.sbs.api.util.math;

import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.math.function.Function;
import dev.sbs.api.util.math.function.Functions;
import dev.sbs.api.util.math.operator.Operator;
import dev.sbs.api.util.math.operator.Operators;
import dev.sbs.api.util.math.tokenizer.FunctionToken;
import dev.sbs.api.util.math.tokenizer.NumberToken;
import dev.sbs.api.util.math.tokenizer.OperatorToken;
import dev.sbs.api.util.math.tokenizer.Token;
import dev.sbs.api.util.math.tokenizer.VariableToken;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@SuppressWarnings("all")
public class Expression {

    private final Token[] tokens;
    @Getter
    private final Map<String, Double> variables;
    private final Set<String> userFunctionNames;
    @Getter
    @Setter
    private VariableProvider variableProvider;

    /**
     * Creates a new expression that is a copy of the existing one.
     *
     * @param existing the expression to copy
     */
    public Expression(final Expression existing) {
        this.tokens = Arrays.copyOf(existing.tokens, existing.tokens.length);
        this.variables = new HashMap<>();
        this.variables.putAll(existing.variables);
        this.variableProvider = existing.variableProvider;
        this.userFunctionNames = new HashSet<>(existing.userFunctionNames);
    }

    /*Expression(final Token[] tokens) {
        this.tokens = tokens;
        this.variables = createDefaultVariables();
        this.userFunctionNames = Collections.emptySet();
    }*/

    Expression(final Token[] tokens, Set<String> userFunctionNames) {
        this.tokens = tokens;
        this.variables = createDefaultVariables();
        this.userFunctionNames = userFunctionNames;
    }

    private static Map<String, Double> createDefaultVariables() {
        final Map<String, Double> vars = new HashMap<>(4);
        vars.put("pi", Math.PI);
        vars.put("π", Math.PI);
        vars.put("φ", 1.61803398874d);
        vars.put("e", Math.E);
        return vars;
    }

    public Expression setVariable(final String name, final double value) {
        this.checkVariableName(name);
        this.variables.put(name, value);
        return this;
    }

    private void checkVariableName(String name) {
        if (this.userFunctionNames.contains(name) || Functions.getBuiltinFunction(name) != null)
            throw new IllegalArgumentException(FormatUtil.format("The variable name ''{0}'' is invalid. Since there exists a function with the same name", name));

    }

    public Expression setVariables(Map<String, Double> variables) {
        for (Map.Entry<String, Double> v : variables.entrySet())
            this.setVariable(v.getKey(), v.getValue());

        return this;
    }

    public Expression clearVariables() {
        this.variables.clear();
        return this;
    }

    public Set<String> getVariableNames() {
        final Set<String> variables = new HashSet<>();

        for (final Token t : tokens) {
            if (t.getType() == Token.TOKEN_VARIABLE)
                variables.add(((VariableToken) t).getName());
        }

        return variables;
    }

    public ValidationResult validate(boolean checkVariablesSet) {
        final List<String> errors = new ArrayList<>(0);

        if (checkVariablesSet) {
            /* check that all vars have a value set */
            for (final Token t : this.tokens) {
                if (t.getType() == Token.TOKEN_VARIABLE) {
                    final String var = ((VariableToken) t).getName();

                    if (!variables.containsKey(var))
                        errors.add(FormatUtil.format("The setVariable ''{0}'' has not been set", var));

                }
            }
        }

        /* Check if the number of operands, functions and operators match.
           The idea is to increment a counter for operands and decrease it for operators.
           When a function occurs the number of available arguments has to be greater
           than or equals to the function's expected number of arguments.
           The count has to be larger than 1 at all times and exactly 1 after all tokens
           have been processed */
        int count = 0;
        for (Token token : this.tokens) {
            switch (token.getType()) {
                case Token.TOKEN_NUMBER:
                case Token.TOKEN_VARIABLE:
                    count++;
                    break;
                case Token.TOKEN_FUNCTION:
                    final Function func = ((FunctionToken) token).getFunction();
                    final int argsNum = ((FunctionToken) token).getArgumentCount();

                    if (func.getMinArguments() > argsNum || func.getMaxArguments() < argsNum)
                        errors.add(FormatUtil.format("Not enough arguments for ''{0}''", func.getName()));

                    if (argsNum > 1)
                        count -= argsNum - 1;
                    else if (argsNum == 0) {
                        // see https://github.com/fasseg/exp4j/issues/59
                        count++;
                    }
                    break;
                case Token.TOKEN_OPERATOR:
                    Operator op = ((OperatorToken) token).getOperator();
                    if (op.getNumOperands() == 2)
                        count--;

                    break;
            }

            if (count < 1) {
                errors.add("Too many operators");
                return new ValidationResult(false, errors);
            }
        }

        if (count > 1)
            errors.add("Too many operands");

        return errors.size() == 0 ? ValidationResult.SUCCESS : new ValidationResult(false, errors);

    }

    public ValidationResult validate() {
        return validate(true);
    }

    public Future<Double> evaluateAsync(ExecutorService executor) {
        return executor.submit(this::evaluate);
    }

    public double evaluate() {
        final ArrayStack output = new ArrayStack();

        for (Token token : tokens) {
            if (token.getType() == Token.TOKEN_NUMBER)
                output.push(((NumberToken) token).getValue());
            else if (token.getType() == Token.TOKEN_VARIABLE) {
                final String name = ((VariableToken) token).getName();
                Double value = this.variables.get(name);

                if (value == null) {
                    if (this.variableProvider != null)
                        value = variableProvider.getVariable(name);
                    else
                        throw new IllegalArgumentException(FormatUtil.format("No value has been set for the setVariable ''{0}''", name));
                }

                output.push(value);
            } else if (token.getType() == Token.TOKEN_OPERATOR) {
                OperatorToken op = (OperatorToken) token;
                if (output.size() < op.getOperator().getNumOperands())
                    throw new IllegalArgumentException(FormatUtil.format("Invalid number of operands available for ''{0}'' operator", op.getOperator().getSymbol()));

                if (op.getOperator().getNumOperands() == 2) {
                    /* pop the operands and push the result of the operation */
                    double rightArg = output.pop();
                    double leftArg = output.pop();
                    output.push(op.getOperator().apply(leftArg, rightArg));
                } else if (op.getOperator().getNumOperands() == 1) {
                    /* pop the operand and push the result of the operation */
                    double arg = output.pop();
                    output.push(op.getOperator().apply(arg));
                }
            } else if (token.getType() == Token.TOKEN_FUNCTION) {
                FunctionToken func = (FunctionToken) token;
                final int numArguments = func.getArgumentCount();

                if (numArguments < func.getFunction().getMinArguments() || numArguments > func.getFunction().getMaxArguments() || output.isEmpty())
                    throw new IllegalArgumentException(FormatUtil.format("Invalid number of arguments available for ''{0}'' function", func.getFunction().getName()));

                /* collect the arguments from the stack */
                double[] args = new double[numArguments];
                for (int j = numArguments - 1; j >= 0; j--)
                    args[j] = output.pop();

                output.push(func.getFunction().apply(args));
            }
        }

        if (output.size() > 1)
            throw new IllegalArgumentException("Invalid number of items on the output queue. Might be caused by an invalid number of arguments for a function.");

        return output.pop();
    }

    /**
     * Convert the <code>Expression</code> back to string expression.
     * <p>Calling this function returns exactly the same result as calling <tt>{@link #toString(boolean) toString}(false)</tt>,
     * so it uses the '*' sign in multiplications</p>
     *
     * @return a string representation of the <code>Expression</code>.
     */
    @Override
    public String toString() {
        return toString(false);
    }

    /**
     * Convert the <code>Expression</code> back to string expression.
     * <p>The argument <code>implicitMultiplication</code> determines whether to use the '*' sign
     * in the returned string expression.
     * Every occurrence of the '*' sign will be removed, except when there is a number to the right of it.</p>
     *
     * @param implicitMultiplication if <code>true</code>, removes the '*' sign in multiplications (only when it's logical)
     * @return a string representation of the <code>Expression</code>.
     */
    public String toString(boolean implicitMultiplication) {
        String expression = toString(Arrays.asList(tokens), implicitMultiplication);

        if (implicitMultiplication) {
            expression = expression.replaceAll("\\*(\\D)", "$1");
        }

        return expression;
    }

    private String toString(List<Token> tokens, boolean impMult) {
        if (tokens.size() == 0)
            return "";
        Token token = tokens.get(tokens.size() - 1);

        switch (token.getType()) {
            case Token.TOKEN_OPERATOR:
                Operator operator = ((OperatorToken) token).getOperator();
                List<List<Token>> operands = getTokensArguments(tokens.subList(0, tokens.size() - 1), operator.getNumOperands());
                List<Token> leftTokens;
                List<Token> rightTokens;

                if (operator.getNumOperands() == 1) {
                    if (operator.isLeftAssociative()) {
                        leftTokens = operands.get(0);
                        rightTokens = new ArrayList<>();
                    } else {
                        leftTokens = new ArrayList<>();
                        rightTokens = operands.get(0);
                    }
                } else {
                    if (operator.getSymbol().equals("*") && operands.get(1).size() == 1 && operands.get(0).get(operands.get(0).size() - 1).getType() != Token.TOKEN_NUMBER) {
                        leftTokens = operands.get(1);
                        rightTokens = operands.get(0);
                    } else {
                        leftTokens = operands.get(0);
                        rightTokens = operands.get(1);
                    }
                }

                boolean parentheses_left = leftTokens.size() > 1 && leftTokens.get(leftTokens.size() - 1).getType() != Token.TOKEN_FUNCTION;
                boolean parentheses_right = rightTokens.size() > 1 && rightTokens.get(rightTokens.size() - 1).getType() != Token.TOKEN_FUNCTION;

                if (parentheses_left && leftTokens.get(leftTokens.size() - 1).getType() == Token.TOKEN_OPERATOR) {
                    Operator leftOperator = ((OperatorToken) leftTokens.get(leftTokens.size() - 1)).getOperator();

                    if (leftOperator.getNumOperands() == 1 && leftOperator.getSymbol().matches("\\+|-") && !operator.getSymbol().matches("\\+|-"))
                        parentheses_left = true;
                    else {
                        if (leftOperator.getSymbol().matches("\\+|-|\\*"))
                            parentheses_left = operator.getPrecedence() > leftOperator.getPrecedence();
                        else
                            parentheses_left = operator.getPrecedence() >= leftOperator.getPrecedence();
                    }
                }
                if (parentheses_right && rightTokens.get(rightTokens.size() - 1).getType() == Token.TOKEN_OPERATOR) {
                    Operator rightOperator = ((OperatorToken) rightTokens.get(rightTokens.size() - 1)).getOperator();

                    if (rightOperator.getNumOperands() == 1 && rightOperator.getSymbol().matches("\\+|-"))
                        parentheses_right = true;
                    else {
                        if (operator.getSymbol().matches("\\+|\\*") && rightOperator.getSymbol().matches("\\+|\\*"))
                            parentheses_right = operator.getPrecedence() > rightOperator.getPrecedence();
                        else
                            parentheses_right = operator.getPrecedence() >= rightOperator.getPrecedence();
                    }
                }

                if (!parentheses_left && leftTokens.size() > 0 && leftTokens.get(leftTokens.size() - 1).getType() == Token.TOKEN_NUMBER)
                    parentheses_left = ((NumberToken) leftTokens.get(0)).getValue() < 0;

                if (!parentheses_right && rightTokens.size() > 0 && rightTokens.get(rightTokens.size() - 1).getType() == Token.TOKEN_NUMBER)
                    parentheses_right = ((NumberToken) rightTokens.get(0)).getValue() < 0;

                String leftOperand = toString(leftTokens, impMult),
                    rightOperand = toString(rightTokens, impMult),
                    symbol = operator.getSymbol();

                if (parentheses_left)
                    leftOperand = "(" + leftOperand + ")";

                if (parentheses_right)
                    rightOperand = "(" + rightOperand + ")";

                return leftOperand + symbol + rightOperand;
            case Token.TOKEN_FUNCTION:
                Function function = ((FunctionToken) token).getFunction();

                if (function.getName().equals("pow")) {
                    tokens.set(tokens.size() - 1, new OperatorToken(Operators.getBuiltinOperator('^', 2)));
                    return toString(tokens, impMult);
                }

                String stringArgs = "";
                List<List<Token>> args = getTokensArguments(tokens.subList(0, tokens.size() - 1), function.getNumArguments());
                for (List<Token> argument : args) {
                    stringArgs += ", " + toString(argument, impMult);
                }
                stringArgs = stringArgs.substring(2);

                return function.getName() + "(" + stringArgs + ")";
            case Token.TOKEN_VARIABLE:
                return ((VariableToken) token).getName();
            case Token.TOKEN_NUMBER:
                double num = ((NumberToken) token).getValue();
                if (num != (long) num)
                    return String.valueOf(num);
                else
                    return String.valueOf((long) num);
            default:
                throw new UnsupportedOperationException("The token type '" + token.getClass().getName() + "' is not supported in this function yet");
        }
    }

    private List<List<Token>> getTokensArguments(List<Token> tokens, int numOperands) {
        List<List<Token>> tArgs = new ArrayList<>(2);
        if (numOperands == 1) {
            tArgs.add(tokens);
        } else {
            int size = 0;
            int[] pos = new int[numOperands - 1];
            for (int i = 0; i < tokens.size() - 1; i++) {
                Token t = tokens.get(i);
                switch (t.getType()) {
                    case Token.TOKEN_NUMBER:
                        size++;
                        break;

                    case Token.TOKEN_VARIABLE:
                        size++;
                        break;

                    case Token.TOKEN_OPERATOR:
                        Operator operator = ((OperatorToken) t).getOperator();
                        if (operator.getNumOperands() == 2)
                            size--;
                        break;

                    case Token.TOKEN_FUNCTION:
                        FunctionToken func = (FunctionToken) t;
                        for (int j = 0; j < func.getFunction().getNumArguments(); j++) {
                            size--;
                        }
                        size++;
                        break;
                }
                for (int j = 0; j < pos.length; j++) {
                    if (size == j + 1) {
                        pos[j] = i;
                    }
                }
            }

            tArgs.add(tokens.subList(0, pos[0] + 1));
            for (int i = 1; i < pos.length; i++) {
                tArgs.add(tokens.subList(pos[i - 1] + 1, pos[i] + 1));
            }
            tArgs.add(tokens.subList(pos[pos.length - 1] + 1, tokens.size()));
        }

        return tArgs;
    }

}
