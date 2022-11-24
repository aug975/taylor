# Entrega Final - Computação Paralela - Série de Taylor

### Augusto Rassi Scrideli 42023092

## Considerações gerais

O desafio proposto pressupunha o cálculo de um milhão de termos de uma série, cujos termos se expandem com base fatorial no denominador, gerando rapidamente incrementos
muito pequenos.

Para buscar um valor com o maior número de termos possível, era então fundamental encontrar uma solução que permitisse **precisão** e **exatidão** de cada termo da série
e da soma total dos mesmos. 

Além disso, pelo volume de cálculos envolvido, esta busca de precisão e exatidão precisava encontrar uma implementação que não comprometesse demasiadamente a 
**performance** dos cálculos. 

A partir desta análise, foram feitos testes com diferentes linguagens como Python, C, C++ e finalmente Java, com a utilização de tipo de ponto flutuante com precisão 
arbitrária implementada em base decimal. A classe **BigDecimal** do Java demonstrou a possibilidade de alta precisão, exatidão no resultado e um bom nível de performance,
além de simplicidade para construção do código.

### Precisão e exatidão

Tipos de ponto flutuante costumam ser implementados por representação binária para que os cálculos sejam feitos rapidamente pelos processadores. 
Se a representação é decimal o cálculo não é natural para o processador e leva mais tempo. Mas há uma desvantagem nessa escolha - não há exatidão quando se 
implementa por representação binária, então nem todos os números podem ser representados dessa forma. 
Sempre dá para representar um número muito próximo, que é quase o mesmo, mas não exatamente o mesmo.

Importante ressaltar que exatidão e precisão são coisas diferentes. Precisão tem a ver com a capacidade de expressar números mais extensos, 
mais casas à direita ou esquerda. Exatidão se refere à conformidade com o valor real.
Números com ponto flutuante com representação binária são armazenados com um valor significante multiplicado por uma base fixa elevada a um expoente, além do sinal.

![image](https://user-images.githubusercontent.com/101229028/203676404-c4ca6748-9760-4d21-9237-308525e9cd63.png)

### Tipo decimal

O tipo decimal tem exatidão, ou seja, ele permite ter o número exato que se pretende. Ele se chama decimal por ter base 10 e não binária.

Cada linguagem o implementa de alguma forma diferente. É comum guardar a parte inteira e decimal separadamente em inteiros, ou guardar tudo junto em um inteiro 
e determinar uma escala, ou seja, onde o ponto flutuante está, quantas casas ele deve assumir, em geral é um inteiro dividido por 1, 10, 100, 1000, etc.

Algumas tecnologias implementam o `SmallDecimal` com 64 bits (16 dígitos), e o `TinyDecimal` com 32 bits (7 dígitos). É comum também ter o `BigDecimal` com mais de 128 bits,
em geral até ilimitados. Neste caso, os cálculos são feitos com instruções de inteiros do processador, o que é rápido, mas precisa de vários passos de normalização 
do número, precisa prover algum arredondamento, muitas vezes o seu código precisa fazer alguma conta extra, então acaba ficando mais lento, 
mas normalmente nada crítico.

### A representação numérica utilizada no Java

Assim como em outras linguagens, o Java representa tipos como *double* na JVM com uma representação binária que segue o padrão IEEE 754.
Pelo fato da JVM trabalhar com representação binária para o tipo double, um valor como “0.1”, por exemplo, é transformado para binário e vira uma dízima periódica, 
ou seja, um valor infinito, algo como “0.110011001100..”. A classe **BigDecimal** trabalha com pontos flutuantes de precisão arbitrária, ou seja, você consegue 
estipular qual o nível de precisão você precisa trabalhar. 

Diferentemente do double que trabalha com representação binária, o BigDecimal guardará seu valor usando a base decimal. Por exemplo: enquanto que o double tenta 
representar o valor “0.1” em binário e encontra uma dízima, o BigDecimal representa o mesmo valor através da base decimal “1 x 10^-1”. 
Sempre que trabalhar com BigDecimal você precisa chamar seus métodos específicos para realizar operações aritméticas, assim teremos garantia da precisão que tanto 
precisamos. 

Temos ainda outro ponto de atenção em relação o BigDecimal: este não faz nenhum tipo de arredondamento por si próprio, ou seja, 
você precisa especificar como deseja arredondar determinado valor se for necessário, caso contrário, ele lançará uma exceção. 
Sendo assim, precisamos dizer explicitamente como desejamos que o arredondamento ocorra caso haja uma dízima.

### Paralelismo em Java
 
Na plataforma Java, as threads são, de fato, o único mecanismo de concorrência suportado. De forma simples, podemos entender esse recurso como trechos de código que operam independentemente da sequência de execução principal. Como diferencial,  as threads dividem um mesmo espaço de memória, e isso lhes permite compartilhar dados 
e informações dentro do contexto do software.
Desde seu início a plataforma Java foi projetada para suportar programação concorrente. De lá para cá, principalmente a partir da versão 5, foram incluídas APIs de alto nível que fornecem cada vez mais recursos para a implementação de tarefas paralelas. Toda aplicação Java possui, no mínimo, uma thread, que é criada e iniciada pela JVM quando iniciamos a aplicação e tem como função executar o método main() da classe principal.

Em Java, existem basicamente duas maneiras de criar threads:
-	Extender a classe Thread (java.lang.Thread)
-	Implementar a interface Runnable (java.lang.Runnable)

## O Algoritmo

O algoritmo implementa a Série de Taylor da constante de Euler, buscando a maior aproximação para o valor de ***e***.
A partir das definições para as questões já apresentadas ligadas a precisão, exatidão e paralelismo nos cálculos dos valores da série, 
o algoritmo é relativamente simples, com uma [função principal `getTotal()`](https://github.com/aug975/taylor/blob/cd1e39b7f3964a435b561fafbcead3a3196d3b31/VFinal/main.java#L79), [descrita no método `run()`](https://github.com/aug975/taylor/blob/cd1e39b7f3964a435b561fafbcead3a3196d3b31/VFinal/main.java#L88-L103) que calcula a soma dos termos da série:

O cálculo dos termos da série se baseia em funções da classe BigDecimal para manipulação adequada de valores de pontos flutuantes de precisão arbitrária.
Utiliza-se também uma [função auxiliar](https://github.com/aug975/taylor/blob/cd1e39b7f3964a435b561fafbcead3a3196d3b31/VFinal/main.java#L59) para cálculo do fatorial.

Para permitir o paralelismo de forma simples e balanceada, a variável `steps` é passada como parâmetro para definir o subconjunto de termos que será somado naquela thread. 

Se temos **2 threads**, por exemplo, quando `steps` tem valor *1* na primeira thread, os termos somados são com denominador *0!, 2!, 4!, ...*

Quando tem valor *2* na segunda thread, os termos somados tem denominador *1!, 3!, 5!, ...* 

![image](https://user-images.githubusercontent.com/101229028/203701508-2269e394-ac24-48bb-a758-2b727c722df8.png)

Da mesma forma, com **n threads**, a primeira thread somaria os termos com denominador *0!, n!, 2n!, ...* a segunda thread *1!, (n+1)!, (2n+1)!, ...* e assim sucessivamente até a última com denominadores *(n-1)!, (2n-1)!, (3n-1)!, ...*

O programa principal estende a classe Threads, criando n threads, onde n é por default o número de cores de CPU detectado no host onde o programa é executado, ou parâmetro passado como argumento - caso informado, e após cada uma delas fazer a soma de parte dos termos, as somas parciais são agregadas em uma variável `tot` na classe Main, que ao final acumula o valor total da série para a aproximação de ***e***.

## Testes e Speedup

Foram realizados testes em máquina local e na AWS. Os testes em máquina local foram realizados com Java em Windows.

### Testes locais  - Tempo de execução

| CPU Cores | 10 000 casas | 100 000 casas |
| :---: | :---: | :---: |
| 1 | 2.676s | 1204.127s |
| 2 | 1.735s | 709.192s |
| 4 | 1.519s | 546.092s |
| 8 | 1.388s | 542.362s |
| 12 | 1.388s | 526.924s |

![image](https://user-images.githubusercontent.com/101229028/203711156-f7d275c6-93ef-4897-a328-dad0cee989a6.png)

![image](https://user-images.githubusercontent.com/101229028/203711289-75c2aa6d-d9d4-46b4-845a-bc99847bfdec.png)

### Testes locais - Speedup

| CPU Cores | 10 000 casas | 100 000 casas |
| :---: | :---: | :---: |
| 1 | 1 | 1 |
| 2 | 1.542 | 1.698 |
| 4 | 1.762 | 2.205 |
| 8 | 1.928 | 2.220 |
| 12 | 1.928 | 2.285 |

![image](https://user-images.githubusercontent.com/101229028/203711858-2619675e-2470-4750-b4b2-778f3027c07d.png)

![image](https://user-images.githubusercontent.com/101229028/203711879-50b6a7aa-9d50-4432-a2f5-5380a0e5e42d.png)

### Testes AWS - Tempo de execução

| CPU Cores | 1 000 casas | 10 000 casas | 100 000 casas |
| :---: | :---: | :---: | :---: |
| 1 | 0.239s | 8.208s | 1946.987s |
| 2 | 0.130s | 5.842s | 1217.731s |

![image](https://user-images.githubusercontent.com/101229028/203712438-c9d70a1c-3801-4b98-8b21-ff5eb32b7747.png)

![image](https://user-images.githubusercontent.com/101229028/203712461-0e4f3eb1-ddd7-4cca-a112-58c0b242f7ff.png)

![image](https://user-images.githubusercontent.com/101229028/203712585-3ff3c749-5de1-4e43-9034-25360722af80.png)

### Testes AWS - Speedup

| CPU Cores | 1 000 casas | 10 000 casas | 100 000 casas |
| :---: | :---: | :---: | :---: |
| 1 | 1 | 1 | 1 |
| 2 | 1.838 | 1.404 | 1.598 |

![image](https://user-images.githubusercontent.com/101229028/203712738-b6356457-fbb1-4a9c-b1bf-0d934f2f1664.png)

## Modelo de Paralelismo

No algoritmo desenvolvido para a solução, o programa realiza o somatório de todos os valores da série de Taylor. 
A primeira versão foi baseada em uma solução sequencial, que se inicia no primeiro termo e *a mesma thread vai até seu último*.

Quando passamos a usar **multi-threading**, estendemos a classe `Thread`, com a criação de uma classe `Tarefa` responsável por realizar o somatório de um subconjunto de termos da série no momento em que ela é criada e armazená-lo em uma variável para que possa ser agregado posteriormente.

Cada thread recebe assim, um intervalo de valores para somar, de forma alternada para balancear melhor o volume, pois os termos crescem de forma fatorial.  
Ao final, cada thread fornece seu somatório para totalizar com as somas das outras threads. 
Nesta solução, depois de darmos início a cada thread, temos que esperar cada uma terminar seu processamento para só então pegar o seu total.
Isso é possível através da função `join()` da thread. O conceito por trás disto é o de ***“Dividir para conquistar”***.

Por se tratar também de uma soma de termos que foi subdividida, no algoritmo desenvolvido ___não foi necessário utilizar uma variável global comum___. 
Cada thread acumulou a soma dos seus termos em uma variavel `sum` com escopo dentro da própria thread, e ao final o valor acumulado em cada instância foi sendo adicionado em uma nova variavel `tot` do programa principal. 
Por conta disso, **não foi necessário se pensar em nenhum mecanismo de controle da região crítica.**

A solução de paralelismo foi feita inicialmente de uma forma simples e prática, paralelizando as tarefas de forma estática, com a quantidade de threads prefixada nas versões de código, para facilitar a execução dos diferentes cenários de teste.

Nesta etapa do desafio, acrescentamos no GitHub uma nova versão do código, que identifica o número de CPU cores do ambiente de execução, e de forma dinâmica estende o número de classes equivalente, buscando otimizar a performance.
