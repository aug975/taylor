import java.math.BigDecimal;
import java.math.MathContext;
import java.math.BigInteger;

public class Main {
    
    public static void main(String[] args) {

      int precision = 10000;
      int T = 1000000;
      int cores = Runtime.getRuntime().availableProcessors();

      long start = System.currentTimeMillis();
      //cria duas tarefas
      Tarefa t1 = new Tarefa(1, precision, T/2, 1);
      t1.setName("Tarefa1");
      Tarefa t2 = new Tarefa(1, precision, T/2, 2);
      t2.setName("Tarefa2");
      
      BigDecimal tot = BigDecimal.ZERO;

      //inicia a execução paralela das duas tarefas, iniciando duas novas threads no programa
      t1.start();
      t2.start();

      //aguarda a finalização das tarefas
      //faz o somatório dos totalizadores de cada Thread
      try {
        t1.join();
        tot = tot.add(t1.getTotal());
        t2.join();
        tot = tot.add(t2.getTotal());
        } 
      catch (InterruptedException ex) {
            ex.printStackTrace();
        }

      System.out.println("Total: " + tot);
      long elapsed = System.currentTimeMillis() - start;
      System.out.printf("\nTempo usando %d CPU core(s) com paralelismo de 2 Threads, para %d casas, %d termos: %.3f s%n", cores, precision, T,  (elapsed) / 1000d);
            
    }

}

class Tarefa extends Thread {

    private double x;
    private int precision;
    private int interval;
    private int steps;
    private BigDecimal sum = BigDecimal.ZERO;

    private static BigInteger factorial(int n){  
      BigInteger f = BigInteger.ONE;
      if(n!=0){
        for(int i=1; i<=n; i++){
          f = f.multiply(BigInteger.valueOf(i));
        }
      }
    return f;
    }
  
    //método construtor que receberá os parâmetros da tarefa
    public Tarefa(double x, int precision, int interval, int steps) {
        this.x = x;
        this.precision = precision;
        this.interval = interval;
        this.steps = steps;
          
    }

    //método que retorna o total calculado
    public BigDecimal getTotal() {
        return sum;
    }

    /*
     Este método se faz necessário para que possamos dar start() na Thread
     e iniciar a tarefa em paralelo
     */
    @Override
    public void run() {
      BigDecimal X = BigDecimal.valueOf(x);
      BigDecimal m;
      int termo;
         
      for(int i=0; i<=interval; i++){ 
        termo = (i*2)+steps-1;
        m = X.pow(termo).divide(new BigDecimal(Tarefa.factorial(termo)),precision,BigDecimal.ROUND_HALF_UP);   
        // quando incremento da serie menor do que precisao interrompe a soma
        if (BigDecimal.ZERO.compareTo(m) == 0){
          return;
        }  
        //print apenas para acompanhar execucao
        //System.out.println(".");
       sum = sum.add(m);
      
      }
      return;

    }
}
