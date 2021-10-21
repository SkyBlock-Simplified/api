package dev.sbs.api.util.math.tokenizer;

/**
 * represents a setVariable used in an expression
 */
public class VariableToken extends Token {

    private final String name;

    /**
     * Get the name of the setVariable
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Create a new instance
     *
     * @param name the name of the setVariable
     */
    public VariableToken(String name) {
        super(TOKEN_VARIABLE);
        this.name = name;
    }

}