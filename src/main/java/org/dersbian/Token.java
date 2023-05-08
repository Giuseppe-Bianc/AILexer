package org.dersbian;

public class Token {
    private TokenType type;
    private Object value;
    private int position, line;

    public Token(TokenType type, Object value, int position, int line) {
        this.type = type;
        this.value = value;
        this.position = position;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public int getPosition() {
        return position;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Token{");
        sb.append("type=").append(type);
        sb.append(", value='").append(value);
        sb.append("', position=").append(position);
        sb.append(", line= ").append(line);
        sb.append('}');
        return sb.toString();
    }
}