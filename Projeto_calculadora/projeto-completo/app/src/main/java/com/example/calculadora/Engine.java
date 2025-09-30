package com.example.calculadora;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class Engine {
    private final MathContext mc = new MathContext(20, RoundingMode.HALF_UP);

    public BigDecimal evaluate(String expr) {
        if (expr == null || expr.isBlank()) throw new IllegalArgumentException("Expressão vazia");
        List<String> tokens = tokenize(expr);
        List<String> rpn = toRPN(tokens);
        return evalRPN(rpn);
    }

    private static boolean isNumber(String s) {
        try {
            new BigDecimal(s);
            return true;
        } catch (Exception e) { return false; }
    }

    // Binary operators (no percent here)
    private static final Map<String, Integer> PREC = Map.of(
            "+", 1, "-", 1, "*", 2, "/", 2, "^", 3
    );

    private static boolean isFunction(String t) {
        return Set.of("sin","cos","tan","log","ln","sqrt","abs","fact").contains(t);
    }

    private static boolean isOperator(String t) {
        return PREC.containsKey(t);
    }

    private List<String> tokenize(String expr) {
        ArrayList<String> out = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) continue;
            if (Character.isDigit(c) || c=='.') {
                buf.setLength(0);
                buf.append(c);
                while (i+1 < expr.length() && (Character.isDigit(expr.charAt(i+1)) || expr.charAt(i+1)=='.')) {
                    i++; buf.append(expr.charAt(i));
                }
                out.add(buf.toString());
            } else if (Character.isLetter(c)) {
                buf.setLength(0);
                buf.append(c);
                while (i+1 < expr.length() && Character.isLetter(expr.charAt(i+1))) { i++; buf.append(expr.charAt(i)); }
                out.add(buf.toString().toLowerCase());
            } else {
                out.add(String.valueOf(c));
            }
        }
        return out;
    }

    private List<String> toRPN(List<String> tokens) {
        ArrayList<String> output = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        for (int i = 0; i < tokens.size(); i++) {
            String t = tokens.get(i);
            if (isNumber(t)) {
                output.add(t);
            } else if (isFunction(t)) {
                stack.push(t);
            } else if (t.equals("%")) {
                // Postfix percent -> convert last value to value/100
                output.add("percent");
            } else if (isOperator(t)) {
                while (!stack.isEmpty() && (isOperator(stack.peek()) &&
                      ((PREC.get(t) <= PREC.get(stack.peek()))))) {
                    output.add(stack.pop());
                }
                stack.push(t);
            } else if (t.equals("(")) {
                stack.push(t);
            } else if (t.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) output.add(stack.pop());
                if (stack.isEmpty()) throw new IllegalArgumentException("Parênteses desbalanceados");
                stack.pop(); // remove "("
                if (!stack.isEmpty() && isFunction(stack.peek())) output.add(stack.pop());
            } else if (t.equals(",")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) output.add(stack.pop());
            } else {
                throw new IllegalArgumentException("Token inválido: " + t);
            }
        }
        while (!stack.isEmpty()) {
            var top = stack.pop();
            if (top.equals("(") || top.equals(")")) throw new IllegalArgumentException("Parênteses desbalanceados");
            output.add(top);
        }
        return output;
    }

    private BigDecimal evalRPN(List<String> rpn) {
        Deque<BigDecimal> stack = new ArrayDeque<>();
        for (String t : rpn) {
            if (isNumber(t)) {
                stack.push(new BigDecimal(t, mc));
            } else if (isOperator(t)) {
                if (stack.size() < 2) throw new IllegalStateException("Operação inválida");
                BigDecimal b = stack.pop();
                BigDecimal a = stack.pop();
                switch (t) {
                    case "+": stack.push(a.add(b, mc)); break;
                    case "-": stack.push(a.subtract(b, mc)); break;
                    case "*": stack.push(a.multiply(b, mc)); break;
                    case "/": stack.push(a.divide(b, mc)); break;
                    case "^": stack.push(pow(a, b)); break;
                    default: throw new IllegalArgumentException("Operador desconhecido: " + t);
                }
            } else if ("percent".equals(t)) {
                if (stack.isEmpty()) throw new IllegalStateException("Percentual sem operando");
                BigDecimal x = stack.pop();
                stack.push(x.divide(new BigDecimal("100"), mc));
            } else if (isFunction(t)) {
                if (t.equals("fact")) {
                    BigDecimal n = stack.pop();
                    stack.push(fact(n));
                } else {
                    BigDecimal x = stack.pop();
                    stack.push(applyFunc(t, x));
                }
            } else {
                throw new IllegalArgumentException("Token RPN inválido: " + t);
            }
        }
        if (stack.size() != 1) throw new IllegalStateException("Expressão inválida");
        return stack.pop().stripTrailingZeros();
    }

    private BigDecimal applyFunc(String f, BigDecimal x) {
        double d = x.doubleValue();
        return switch (f) {
            case "sin" -> new BigDecimal(Math.sin(d), mc);
            case "cos" -> new BigDecimal(Math.cos(d), mc);
            case "tan" -> new BigDecimal(Math.tan(d), mc);
            case "log" -> new BigDecimal(Math.log10(d), mc);
            case "ln" -> new BigDecimal(Math.log(d), mc);
            case "sqrt" -> new BigDecimal(Math.sqrt(d), mc);
            case "abs" -> x.abs(mc);
            default -> throw new IllegalArgumentException("Função desconhecida: " + f);
        };
    }

    private BigDecimal fact(BigDecimal n) {
        if (n.scale() > 0) throw new IllegalArgumentException("Fatorial apenas para inteiros");
        if (n.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Fatorial negativo indefinido");
        BigDecimal acc = BigDecimal.ONE;
        for (int i=1; i<= n.intValueExact(); i++) {
            acc = acc.multiply(new BigDecimal(i), mc);
        }
        return acc;
    }

    private BigDecimal pow(BigDecimal a, BigDecimal b) {
        double res = Math.pow(a.doubleValue(), b.doubleValue());
        return new BigDecimal(res, mc);
    }
}
