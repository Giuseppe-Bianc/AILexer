package org.dersbian;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        AILexer lexer = new AILexer(Costanti.INPUTCODE);
        List<Token> tokens;
        try {
            tokens = lexer.getAllTokens();
        } catch (InvalidTokenException e) {
            throw new RuntimeException(e);
        }
        tokens.forEach(System.out::println);
    }
}
