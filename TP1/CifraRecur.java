import java.util.Scanner;

public class CifraRecur {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String palavra = scanner.nextLine().trim();
        
        while (!palavra.equals("FIM")) {
            System.out.println(cifraCesar(palavra, 0));
            palavra = scanner.nextLine().trim();
        }
        
        scanner.close();
    }
    
    public static String cifraCesar(String palavra, int index) {
        if (index == palavra.length()) {
            return "";
        }
        char novoChar;
        if (palavra.charAt(index) != '?') {
        	novoChar = (char) (palavra.charAt(index) + 3);
        }
        else {
        	novoChar = '?';
        }	
        return novoChar + cifraCesar(palavra, index + 1);
    }
}
