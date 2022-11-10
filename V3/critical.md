# Controle de acesso à região crítica


```
class Tarefa extends Thread {

    private double x;
    private int precision;
    private int interval;
    private int steps;
    private BigDecimal sum = BigDecimal.ZERO;
```

> Threads operam em espaço de instância. As variáveis que afetam são dentro da instância de cada thread, e não colidem com os espaços das outras threads.

```
 for(int i=0; i<=interval; i++){ 
        termo = (i*4)+steps-1;
        m = X.pow(termo).divide(new BigDecimal(Tarefa.factorial(termo)),precision,BigDecimal.ROUND_HALF_UP);   
        if (BigDecimal.ZERO.compareTo(m) == 0){
          return;
        }  
       sum = sum.add(m);
```

> O loop for que afeta a variável final está dentro do `run` de cada thread, que também ocorre dentro da instância. Mesmo após somar à variável final, nenhum valor afetou o espaço global do código. Isto ocorre apenas a partir do `.join`. Este funcionamento, em conjunto com o fato de a interação final ocorrer com uma soma, resulta no fato de que não há uma região crítica aqui, as operações ocorrem sempre independentes.

```
public BigDecimal getTotal() {
        return sum;
    }
```
> Com este método `getTotal`, podemos somar os valores finais das operações de cada thread de uma só vez, em sequência, no espaço da `main`. Assim, não há risco de interferência entre threads.

```
 try {
        t1.join();
        tot = tot.add(t1.getTotal());
        t2.join();
        tot = tot.add(t2.getTotal());
        t3.join();
        tot = tot.add(t3.getTotal());
        t4.join();
        tot = tot.add(t4.getTotal());
        } 
      catch (InterruptedException ex) {
            ex.printStackTrace();
        }
```
> Com o `join`, os valores de cada instância se juntam e compõem o valor total de forma controlada.

# Ou seja,

No problema proposto, o programa realiza o somatório de todos os valores da série de taylor. Em um programa sequencial, se inicia no primeiro valor e a mesma thread vai até seu último valor.

Neste caso onde usamos multithreading, criamos N threads, subdividindo as tarefas, onde cada uma recebe um intervalo de valores para somar, de forma alternada para balancear melhor o volume, pois os termos crescem de forma fatorial. No final cada thread fornece seu somatório para totalizar com as somas das outras threads. Depois de darmos início às threads, temos que esperar as mesmas terminarem seu processamento para depois pegarmos o total de cada uma. Isso é possível através da função `join()` da thread. A ideia disto é _“Dividir para conquistar”_.

Por se tratar também de uma soma de termos que foi subdividida não foi necessário utilizar uma variável global comum. Cada thread acumulou a soma dos seus termos em uma variável `sum` com escopo dentro da própria thread, e ao final o valor acumulado em cada instância foi sendo adicionado em uma nova variável `tot` do programa principal. Por conta disso, não foi necessário se pensar em nenhum mecanismo de controle da região critica.

A solução foi feita de uma forma simples e prática de paralelizarmos tarefas em um programa, e de forma estática, pois a quantidade de threads está prefixada no código para facilitar a execução dos diferentes cenários de teste.

Poderíamos tornar este processo dinâmico, capturando a informação sobre o ambiente no qual o programa está sendo executado e criando a quantidade de threads ideal para o número de núcleos de processamento existentes, como na versão experimental nThreads.
