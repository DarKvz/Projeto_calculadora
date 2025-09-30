package com.example.calculadora;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class EngineTest {

    @Test
    void somaSimples() {
        Engine e = new Engine();
        assertEquals(new BigDecimal("7"), e.evaluate("3+4"));
    }

    @Test
    void precedenciaOperadores() {
        Engine e = new Engine();
        assertEquals(new BigDecimal("11"), e.evaluate("3+4*2"));
    }

    @Test
    void funcoesBasicas() {
        Engine e = new Engine();
        assertEquals(new BigDecimal("9"), e.evaluate("sqrt(81)").stripTrailingZeros());
    }

    @Test
    void potencia() {
        Engine e = new Engine();
        assertEquals(new BigDecimal("8"), e.evaluate("2^3").stripTrailingZeros());
    }

    @Test
    void percentualPostfixSimples() {
        Engine e = new Engine();
        assertEquals(new BigDecimal("0.5"), e.evaluate("50%"));
    }

    @Test
    void percentualComMultiplicacao() {
        Engine e = new Engine();
        assertEquals(new BigDecimal("20"), e.evaluate("200*10%"));
    }
}
