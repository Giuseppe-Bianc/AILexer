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

        // Skip whitespace
        skipWhitespace();

        // Recognize tokens
        char currentChar = input.charAt(currentPosition);
        int tokenPosition = currentPosition + 1;

        if (Character.isDigit(currentChar)) {
            return recognizeNumber(tokenPosition);
        } else if (Character.isLetter(currentChar) || currentChar == '_') {
            return recognizeIdentifier(tokenPosition);
        } else if (isSymbol(currentChar)) {
            return recognizeSymbol(tokenPosition);
        }

        throw new InvalidTokenException(
                String.format("Invalid token '%c' at position %d at line %d", currentChar, currentPosition, currentLine));
    }

    Pattern getPattern(String regex) {
        return patternCache.get(regex, Pattern::compile);
    }

    private Token recognizeNumber(int tokenPosition) throws InvalidTokenException {
        int start = currentPosition;

        // Use a regular expression pattern to match the number format
        String numberPattern = "0[xX][0-9a-fA-F]+|0[oO][0-7]*|\\d+(\\.\\d+)?([eE][+-]?\\d+)?";
        Pattern pattern = getPattern(numberPattern);
        Matcher matcher = pattern.matcher(input.substring(currentPosition));

        if (matcher.find()) {
            currentPosition += matcher.end();
            String lexeme = matcher.group();

            if (lexeme.contains(".") || lexeme.toLowerCase().contains("e")) {
                double value = Double.parseDouble(lexeme);
                return new Token(TokenType.NUMBER, value, tokenPosition, currentLine);
            } else {
                long value;
                if (lexeme.startsWith("0x") || lexeme.startsWith("0X")) {
                    value = Long.parseLong(lexeme.substring(2), 16);
                } else if (lexeme.startsWith("0o") || lexeme.startsWith("0O")) {
                    value = Long.parseLong(lexeme.substring(2), 8);
                } else {
                    value = Long.parseLong(lexeme);
                }
                return new Token(TokenType.NUMBER, value, tokenPosition, currentLine);
            }
        }

        // Throw an exception if the number format is invalid
        throw new InvalidTokenException(
                String.format("Invalid number format at position %d at line %d", currentPosition, currentLine));
    }


    private Token recognizeIdentifier(int tokenPosition) {
        int start = currentPosition;
        while (currentPosition < input.length() && (Character.isLetterOrDigit(input.charAt(currentPosition)) || input.charAt(currentPosition) == '_')) {
            currentPosition++;
        }

        String lexeme = input.substring(start, currentPosition);
        return new Token(TokenType.IDENTIFIER, lexeme, tokenPosition, currentLine);
    }

    private Token recognizeSymbol(int tokenPosition) {
        char currentChar = input.charAt(currentPosition);
        currentPosition++;

        String lexeme = String.valueOf(currentChar);
        return new Token(TokenType.SYMBOL, lexeme, tokenPosition, currentLine);
    }

    private boolean isSymbol(char currentChar) {
        String symbols = "+-*/=<>();.{}%!&|#";
        return symbols.contains(String.valueOf(currentChar));
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
