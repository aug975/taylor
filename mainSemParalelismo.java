//VERSAO DE CODIGO SEM USO DE PARALELISMO

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.BigInteger;

class Main {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println("\n\nResult: "+getExp(1,1000,1000));
        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("\nTempo sem paralelismo: %.3f s%n", (elapsed) / 1000d);
    }
    
	private static BigDecimal getExp(double x, int precision, int steps){
         BigDecimal sum = BigDecimal.ZERO;
         BigDecimal X = BigDecimal.valueOf(x);
         BigDecimal m;
         for(int i=0; i<steps; i++){      
             m = X.pow(i).divide(new BigDecimal(Main.factorial(i)),precision,BigDecimal.ROUND_HALF_UP);
             //System.out.println("\nx^"+i+"/"+i+"! = "+m);
             sum = sum.add(m);

             // quando incremento da serie menor do que precisao interrompe a soma
             if (BigDecimal.ZERO.compareTo(m) == 0){
               return sum;
             } 
         }
         return sum;
    }
    
    private static BigInteger factorial(int n){
        BigInteger f = BigInteger.ONE;
        if(n!=0){
            for(int i=1; i<=n; i++){
                f = f.multiply(BigInteger.valueOf(i));
            }
        }
        return f;
    }
}
