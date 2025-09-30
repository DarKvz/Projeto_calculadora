# Projeto calculadora

Calculadora científica desktop escrita em **Java 17 (Swing)** com motor de avaliação baseado em **Shunting-yard → RPN**, **precisão com `BigDecimal`**, operador **`%` pós-fixo correto**, histórico de cálculos, **testes JUnit 5** e **CI com GitHub Actions**. Ideal para estudos de Qualidade de Software (ISO/IEC 25010) e boas práticas de versionamento.

## ✨ Funcionalidades

* Operações: `+`, `-`, `*`, `/`, `^`, parênteses.
* **Percentual pós-fixo**: `50% → 0.5`, `200*10% → 20`, `(5+5)% → 0.1`.
* Funções: `sin`, `cos`, `tan`, `log` (base 10), `ln` (natural), `sqrt`, `abs`, `fact` (fatorial).
* Histórico de cálculos (área dedicada na UI).
* Tratamento de erros com mensagens claras (parênteses desbalanceados, token inválido, etc.).
* Testes unitários (JUnit 5) e pipeline de CI (GitHub Actions).

## 🧭 Como executar

**Pré-requisitos**: Java 17+ e Maven 3.9+ instalados e no `PATH`.

```bash
cd app
mvn clean package
java -jar target/projeto-calculadora-0.1.1.jar
```

> Se estiver usando um IDE (IntelliJ/Eclipse/VS Code), basta importar o diretório `app` como projeto Maven e executar a classe `com.example.calculadora.Main`.

## 📝 Exemplos de uso

```text
3+4*2            -> 11
sqrt(81)         -> 9
2^3              -> 8
50%              -> 0.5
200*10%          -> 20
(5+5)%           -> 0.1
sin(3.14159/2)   -> ~1
abs(-3.5)        -> 3.5
fact(5)          -> 120
```

> Observações: funções trigonométricas usam **radianos**. `fact(n)` aceita **inteiros não negativos**.

## 🧱 Estrutura do projeto

```
app/
 ├─ pom.xml
 └─ src/
    ├─ main/java/com/example/calculadora/
    │  ├─ Engine.java   # Tokenização, Shunting-yard, RPN e avaliação
    │  └─ Main.java     # UI Swing (display, grid de botões, histórico)
    └─ test/java/com/example/calculadora/
       └─ EngineTest.java

```

## 🧪 Testes

Execute:

```bash
cd app
mvn -q -DskipTests=false test
```

Testes cobrem operações básicas, funções e **comportamento do `%` pós-fixo** (`50%` e `200*10%`).

## 🔧 Decisões técnicas

* **Precisão**: `BigDecimal` com `MathContext(20, HALF_UP)` no motor.
* **Parsing**: algoritmo **Shunting-yard** para gerar RPN; avaliador de pilha.
* **`%`**: interpretado como **operador unário pós-fixo** (divide o operando por 100).

  > *Opcional*: é possível habilitar o comportamento “calculadora de mesa” (`a + b%` ⇒ `a + a*(b/100)`) caso desejado.
* **UI**: Java Swing com `GridLayout` e histórico via `JTextArea`.

## ✅ Qualidade (ISO/IEC 25010 — resumo)

* **Funcionalidade**: operações e funções cobertas por testes.
* **Confiabilidade**: tratamento de erros e uso de `BigDecimal`.
* **Usabilidade**: interface simples, botões agrupados e feedback de erro.
* **Eficiência**: avaliação linear após tokenização.
* **Manutenibilidade**: separação Engine/UI, testes e CI.
* **Portabilidade**: Java 17 (Windows, Linux, macOS).

## 🗺️ Roadmap sugerido

* Persistência do histórico (arquivo `.history`).
* **Modo programador** (bases 2/8/10/16, bitwise).
* **Conversor de unidades** (comprimento, massa, temperatura).
* **Plot de funções** (gráfico simples).
* **i18n** (PT/EN) e acessibilidade.


## 🧩 Solução de problemas

* Use **ponto** como separador decimal (`3.5`).
* `fact(n)` aceita apenas inteiros ≥ 0.
* `sin`, `cos`, `tan` usam **radianos**.
* Divisão por zero lança erro.
* Diferenças de arredondamento podem ocorrer ao converter `double`→`BigDecimal` nas funções trig/log/sqrt (comportamento esperado em calculadoras científicas).


**MIT** — veja o arquivo `LICENSE`.
