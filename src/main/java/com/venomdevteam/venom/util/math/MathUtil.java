package com.venomdevteam.venom.util.math;

import java.math.BigInteger;

public class MathUtil {

    public static BigInteger fibonacci(int place) {

        BigInteger[]  f = new BigInteger[place + 1];
        int i;

        f[0] = BigInteger.ZERO;
        f[1] = BigInteger.ONE;

        for (i = 2; i <= place; i++) {
            f[i] = f[i - 1].add(f[i - 2]);
        }

        return f[place];

    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equalsIgnoreCase("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equalsIgnoreCase("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equalsIgnoreCase("tan")) x = Math.tan(Math.toRadians(x));
                    else if (func.equalsIgnoreCase("fib")) x = fibonacci((int) x).doubleValue();
                    else if (func.equalsIgnoreCase("abs")) x = Math.abs(x);
                    else if (func.equals("test")) x = 777;
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());

                return x;
            }
        }.parse();
    }

}
