package org.dersbian;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AILexer {
    private final String input;
    private int currentPosition;
    private int currentLine;
    private static final Cache<String, Pattern> patternCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .maximumSize(10)
            .weakKeys()
            .weakValues()
            .build();

    public AILexer(String input) {
        this.input = input;
        this.currentPosition = 0;
        this.currentLine = 1;
    }

    public List<Token> getAllTokens() throws InvalidTokenException {
        List<Token> tokens = new ArrayList<>();

        Token nextToken;
        while ((nextToken = getNextToken()) != null) {
            tokens.add(nextToken);
        }

        return tokens;
    }

    public Token getNextToken() throws InvalidTokenException {
        if (currentPosition >= input.length()) {
            return null;
        }

        // Rimuovi spazi bianchi
        skipWhitespace();

        // Riconosci i token
        Pattern pattern = getPattern("(AI|FOR|IF|ELSE|VAR|\\d+|\\w+|[+\\-*/=<>\\(\\);.{}])");
        Matcher matcher = pattern.matcher(input);
        matcher.region(currentPosition, input.length());

        if (matcher.lookingAt()) {
            String lexeme = matcher.group();
            int tokenLength = matcher.end() - matcher.start();
            currentPosition += tokenLength;
            int tokenPosition = currentPosition - tokenLength + 1;

            switch (lexeme) {
                case "\\d+" -> {
                    int value = Integer.parseInt(lexeme);
                    return new Token(TokenType.NUMBER, value, tokenPosition, currentLine);
                }
                case "\\w+" -> {
                    return new Token(TokenType.IDENTIFIER, lexeme, tokenPosition, currentLine);
                }
                default -> {
                    return new Token(TokenType.KEYWORD, lexeme, tokenPosition, currentLine);
                }
            }

        }

        throw new InvalidTokenException(
                String.format("Invalid token '%c' at position %d at line %d", input.charAt(currentPosition), currentPosition, currentLine));
    }

    private Pattern getPattern(String regex) {
        return patternCache.get(regex, Pattern::compile);
    }

    private void skipWhitespace() {
        while (currentPosition < input.length() && Character.isWhitespace(input.charAt(currentPosition))) {
            if (input.charAt(currentPosition) == '\n') {
                currentLine++;
            }
            currentPosition++;
        }
    }
}
