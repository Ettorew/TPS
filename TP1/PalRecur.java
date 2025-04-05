import java.util.Scanner;

public class PalRecur {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String palavra = scanner.nextLine().trim();
        
        while (!palavra.equals("FIM")) {
            if (ePalindromo(palavra, 0, palavra.length() - 1)) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }
            
            palavra = scanner.nextLine().trim();
        }
        
        scanner.close();
    }
    
    public static boolean ePalindromo(String palavra, int inicio, int fim) {
        if (inicio >= fim) {
            return true;
        }
        if (palavra.charAt(inicio) != palavra.charAt(fim)) {
            return false;
        }
        return ePalindromo(palavra, inicio + 1, fim - 1);
    }
}