import java.util.Scanner;

public class InversaoString {

    public static String inverterString(String str) {
        StringBuilder invertida = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {  
            invertida.append(str.charAt(i));            
        }
        return invertida.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        String entrada = scanner.nextLine().trim();
        
        while(!entrada.equals("FIM")) {
        	
            System.out.println(inverterString(entrada));
            
            entrada = scanner.nextLine().trim();
            
        }
        

        
        scanner.close();
    }
}
