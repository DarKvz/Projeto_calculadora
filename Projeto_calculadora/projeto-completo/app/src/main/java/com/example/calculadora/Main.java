package com.example.calculadora;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

public class Main {
    private final Engine engine = new Engine();
    private JTextField display;
    private JTextArea history;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().buildUI());
    }

    private void buildUI() {
        JFrame f = new JFrame("Projeto calculadora");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(480, 640);
        f.setLayout(new BorderLayout());

        display = new JTextField();
        display.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
        f.add(display, BorderLayout.NORTH);

        history = new JTextArea();
        history.setEditable(false);
        history.setLineWrap(true);
        history.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(history);
        sp.setPreferredSize(new Dimension(480, 150));
        f.add(sp, BorderLayout.SOUTH);

        JPanel grid = new JPanel(new GridLayout(6, 5, 5, 5));
        String[] buttons = {
                "7","8","9","/","sqrt",
                "4","5","6","*","^",
                "1","2","3","-","log",
                "0",".","(",")","+",
                "sin","cos","tan","ln","abs",
                "C","<-","%","fact","="
        };
        for (String b : buttons) {
            JButton btn = new JButton(b);
            btn.addActionListener(this::onClick);
            grid.add(btn);
        }
        f.add(grid, BorderLayout.CENTER);

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void onClick(ActionEvent e) {
        String t = ((JButton)e.getSource()).getText();
        switch (t) {
            case "C" -> display.setText("");
            case "<-" -> {
                String s = display.getText();
                if (!s.isEmpty()) display.setText(s.substring(0, s.length()-1));
            }
            case "=" -> {
                try {
                    BigDecimal r = engine.evaluate(display.getText());
                    history.append(display.getText() + " = " + r.toPlainString() + "\n");
                    display.setText(r.toPlainString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            default -> display.setText(display.getText() + t);
        }
    }
}
