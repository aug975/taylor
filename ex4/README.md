# Exercício 04 - Decomposição de tarefas

## 1. Quais são as tarefas no caminho crítico para cada grafo de dependência? Para responder a pergunta, apresente um DAG do seu projeto.

### DAG: 

![image](https://user-images.githubusercontent.com/101229028/197435640-70dfa9c9-66cc-4703-96b2-303c535b5ca0.png)

A forma como foi feito o paralelismo (de dados baseado em entrada e saída) desdobrou o mesmo número de termos a serem calculados por cada tarefa. 
Além disso, o agrupamento de dados para cálculo foi feito alternando-se os termos da série para cada tarefa, de forma a evitar um volume maior de cálculo para alguma delas,
dado que os termos crescem de forma exponencial. Ainda assim, as tarefas progressivamente (da primeira à quarta) terão uma carga de processamento maior pois lidarão
com números de fatorial maiores. Por exemplo, a quarta tarefa terá que calcular fatoriais maiores (3!,7!,11!...1.000.000!) do que a primeira tarefa (0,4!,8!,...,999.997!).

![image](https://user-images.githubusercontent.com/101229028/197437824-ac638674-c5e2-40cc-873d-47953d4f8c28.png)
>Os valores de peso são apenas para dar uma ideia do nível de esforço de cada tarefa.

**Portanto, as tarefas no caminho crítico são T0, T4 e T5 (somente devido ao peso de trabalho dos cálculos de fatorial).**

## 2. Qual é o limite inferior do tempo de execução paralela para cada decomposição?
Baseado na execução mais longa realizada, T1 foi 1305 segundos, e Tp foi 643. Como T1/P é menor que Tp, posso pressupor que o T-inf, para as condições dadas, é 643 segundos.
Portanto, pelos dados obtidos, Tp seria 643 segundos.

## 3. Quantos processadores são necessários para se conseguir o tempo mínimo de execução?
Pode-se inferir que o tempo mínimo para essa versão do código (que instancia quatro tarefas) pode ser alcançado com quatro cores de processador. Empiricamente, este resultado
foi confirmado.

## 4. Qual é o grau máximo de concorrência?
O ponto em que temos o máximo de tarefas concorrentes se encontra durante o cálculo próprio dos termos da série. Durante esse tempo, temos as _quatro_ tarefas T1-T4 executando
em simultâneo. O grau máximo, portanto, pode ser dito como **4**.

## 5. Qual é o paralelismo médio?
Na forma em que este código foi projetado, o número de tarefas em qualquer momento é controlado. Na maior parte do tempo, temos quatro tarefas em funcionamento.
Portanto, o grau médio seria ~4.
