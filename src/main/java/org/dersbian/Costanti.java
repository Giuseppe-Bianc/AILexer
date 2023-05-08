package org.dersbian;

public class Costanti {
    public static final String CODE =
            "AI ai = new AI();\nai.learn();\nIF ai.isSmart() {\n    ai.think();\n} ELSE {\n    ai.study();\n}";
    public static final String INPUT =
            "(()( % 3 + 4 * 2 - ( 1 - 5 ) / 2 == != >= % <= < > = & && || % -22 | \r\n 2 + 0x33 \n inter = 0o77\n 23.2e-3 + 2e3\n # commento prova #\n 2+2  3 +3";
    public static final String INPUTCODE = CODE + INPUT;
    public static final String INPUTSCODE = INPUTCODE + INPUTCODE + INPUTCODE + INPUTCODE;
    public static final String INPUTSSCODE = INPUTSCODE + INPUTSCODE;
    public static final String INPUTSSSCODE = INPUTSSCODE + INPUTCODE;
    public static final String INPUTSSSSCODE = INPUTSSCODE + INPUTCODE + INPUTCODE;
}