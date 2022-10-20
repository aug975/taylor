import java.math.BigDecimal;
import java.math.MathContext;
import java.math.BigInteger;

@SuppressWarnings("unused")
class Tarefa extends Thread {

    private double x;
    private int precision;
    private int stepsini;
    private int stepsend;
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
    public Tarefa(double x, int precision, int stepsini, int stepsend) {
        this.x = x;
        this.precision = precision;
        this.stepsini = stepsini;
        this.stepsend = stepsend;
          
    }

    //método que retorna o total calculado
    public BigDecimal getTotal() {
        return sum;
    }

    /*
     Este método se faz necessário para que possamos dar start() na Thread
     e iniciar a tarefa em paralelo
     */
    @SuppressWarnings("deprecation")
	@Override
    public void run() {
      BigDecimal X = BigDecimal.valueOf(x);
      BigDecimal m;
      int vprecision = precision;
         
      for(int i=stepsini; i<=stepsend; i++){      
        m = X.pow(i).divide(new BigDecimal(Tarefa.factorial(i)),vprecision,BigDecimal.ROUND_HALF_UP);
       sum = sum.add(m);
      }
      return;

    }
}

class Main {
    
    public static void main(String[] args) {

      int precision = 100;
      int T = 10000;

      long start = System.currentTimeMillis();
      //cria quatro tarefas
      Tarefa t1 = new Tarefa(1, precision, 0, T/4);
      t1.setName("Tarefa1");
      Tarefa t2 = new Tarefa(1, precision, T/4 + 1, T/2);
      t2.setName("Tarefa2");
      Tarefa t3 = new Tarefa(1, precision, T/2 + 1, 3*T/4);
      t3.setName("Tarefa3");
      Tarefa t4 = new Tarefa(1, precision, 3*T/4 + 1, T);
      t4.setName("Tarefa4");

      //inicia a execução paralela das quatro tarefas, iniciando quatro novas threads no programa
      t1.start();
      t2.start();
      t3.start();
      t4.start();

      //aguarda a finalização das tarefas
      try {
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

      //Exibimos o somatório dos totalizadores de cada Thread
      
      BigDecimal tot = BigDecimal.ZERO;
      tot = tot.add(t1.getTotal());;
      tot = tot.add(t2.getTotal());
      tot = tot.add(t3.getTotal());
      tot = tot.add(t4.getTotal());
      
      System.out.println("Total: " + tot);
      
      long elapsed = System.currentTimeMillis() - start;
      System.out.printf("\nTempo com paralelismo: %.3f ms%n", (elapsed) / 1000d);

      
    // Processando com um unico thread
      
      long start0 = System.currentTimeMillis();
      //cria quatro tarefas
      Tarefa t0 = new Tarefa(1, precision, 0, T);
      t0.setName("Tarefa1");

      //inicia a execução paralela das quatro tarefas, iniciando quatro novas threads no programa
      t0.start();

      //aguarda a finalização das tarefas
      try {
        t0.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

      //Exibimos o somatório dos totalizadores de cada Thread
      
      BigDecimal tot0 = BigDecimal.ZERO;
      tot0 = tot0.add(t0.getTotal());;
      
      System.out.println("\n\nTotal: " + tot);
      
      long elapsed0 = System.currentTimeMillis() - start0;
      System.out.printf("\nTempo sem paralelismo: %.3f ms%n", (elapsed0) / 1000d);
      
    }

} 
