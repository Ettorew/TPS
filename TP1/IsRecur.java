import java.util.Scanner;

public class IsRecur {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expressao = scanner.nextLine().trim();
        
        while (!expressao.equals("FIM")) {
            System.out.print((eVogais(expressao, 0) ? "SIM" : "NAO") + " ");
            System.out.print((eConsoantes(expressao, 0) ? "SIM" : "NAO") + " ");
            System.out.print((eInteiro(expressao, 0) ? "SIM" : "NAO") + " ");
            System.out.println((eReal(expressao, 0, false) ? "SIM" : "NAO"));
            
            expressao = scanner.nextLine().trim();
        }
        
        scanner.close();
    }
    
    public static boolean eVogais(String expressao, int index) {
        if (index == expressao.length()) return true;
        char c = expressao.charAt(index);
        if (!(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
              c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')) return false;
        return eVogais(expressao, index + 1);
    }
    
    public static boolean eConsoantes(String expressao, int index) {
        if (index == expressao.length()) return true;
        char c = expressao.charAt(index);
        if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) ||
            (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
             c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')) return false;
        return eConsoantes(expressao, index + 1);
    }
    
    public static boolean eInteiro(String expressao, int index) {
        if (index == expressao.length()) return true;
        char c = expressao.charAt(index);
        if (!(c >= '0' && c <= '9')) return false;
        return eInteiro(expressao, index + 1);
    }
    
    public static boolean eReal(String expressao, int index, boolean pontoEncontrado) {
        if (index == expressao.length()) return true;
        char c = expressao.charAt(index);
        if (c == '.') {
            if (pontoEncontrado) return false;
            return eReal(expressao, index + 1, true);
        }
        if (!(c >= '0' && c <= '9')) return false;
        return eReal(expressao, index + 1, pontoEncontrado);
    }
}
