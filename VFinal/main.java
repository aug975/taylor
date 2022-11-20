import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;


class Main {
    
    public static void main(String[] args) {
    	
      int precision = 1000;
      int T = 1000;
      int cores = Runtime.getRuntime().availableProcessors();
      int tds = cores;
      
      if (args.length != 0) {
          tds = Integer.parseInt(args[0]);
      }   

      long start = System.currentTimeMillis();     
      BigDecimal tot = BigDecimal.ZERO;
      Thread[] t = new Thread[tds];
      
	  //inicia a execução paralela das duas tarefas, iniciando duas novas threads no programa
      //cria  tarefas
      for (int contt = 0; contt < tds; contt++) {
    	  t[contt] = new Tarefa(1, precision, T/tds, contt+1, tds);
        t[contt].start();
      }
      
      for (int contt = 0; contt < tds; contt++) {
    	  //aguarda a finalização das tarefas
	      //faz o somatório dos totalizadores de cada Thread
		    try {
  			  t[contt].join();
  			  tot = tot.add(((Tarefa) t[contt]).getTotal());
  		  }  
  		  catch (InterruptedException ex) {
  			  ex.printStackTrace();
  		  }
      }
      
      System.out.println("\nTotal: " + tot);
      long elapsed = System.currentTimeMillis() - start;
      System.out.printf("\nTempo usando %d CPU core(s) com %d Threads, para %d casas, %d termos: %.3f s%n", cores, tds, precision, T,  (elapsed) / 1000d);
            
    }
}


class Tarefa extends Thread {
    private double x;
    private int precision;
    private int interval;
    private int steps;
    private int tds;
    private BigDecimal sum = BigDecimal.ZERO;
    int cores = Runtime.getRuntime().availableProcessors();

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
    public Tarefa(double x, int precision, int interval, int steps, int tds) {
        this.x = x;
        this.precision = precision;
        this.interval = interval;
        this.steps = steps;
        this.tds = tds;   
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
        termo = (i*tds)+steps-1;						
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
