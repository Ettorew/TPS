import java.util.Scanner;

public class SomaDigitos {

    public static int somaDigitos(int numero) {
        if (numero == 0) {
            return 0;
        } else {
            return numero % 10 + somaDigitos(numero / 10);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        String entrada = scanner.nextLine().trim();
        
        while(!entrada.equals("FIM")) {
        	
        	int numero = Integer.parseInt(entrada);
        	System.out.println(somaDigitos(numero));
        	
        	entrada = scanner.nextLine().trim();
        }
        
        scanner.close();
    }
}
