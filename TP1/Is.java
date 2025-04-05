import java.util.Scanner;

public class Is {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String expressao;
		expressao = scanner.nextLine().trim();
		
		while(!expressao.equals("FIM")) {
			
			System.out.print((eVogais(expressao) ? "SIM" : "NAO") + " ");
            System.out.print((eConsoantes(expressao) ? "SIM" : "NAO") + " ");
            System.out.print((eInteiro(expressao) ? "SIM" : "NAO") + " ");
            System.out.println((eReal(expressao) ? "SIM" : "NAO"));
            
            expressao = scanner.nextLine().trim();
		}
		
		scanner.close();
	}
	
	public static boolean eVogais(String expressao) {
		
		for(int i=0; i<expressao.length(); i++) {
			char caractere = expressao.charAt(i);
			if(!(caractere == 'a' || caractere == 'e' || caractere == 'i' || caractere == 'o' || caractere == 'u'
					|| caractere == 'A' || caractere == 'E' || caractere == 'I' || caractere == 'O' || caractere == 'U')) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean eConsoantes(String expressao) {
		
		for(int i=0; i<expressao.length(); i++) {
			char caractere = expressao.charAt(i);
			if (!((caractere >= 'a' && caractere <= 'z') || (caractere >= 'A' && caractere <= 'Z'))) {
				return false;
			}
			if ((caractere == 'a' || caractere == 'e' || caractere == 'i' || caractere == 'o' || caractere == 'u'
			    || caractere == 'A' || caractere == 'E' || caractere == 'I' || caractere == 'O' || caractere == 'U')){
			    return false;
			}
		}
		
		return true;
	}

	public static boolean eInteiro(String expressao) {
		
		for(int i=0; i<expressao.length(); i++) {
			char caractere = expressao.charAt(i);
			if(!(isNumeroOuPonto(caractere))) {
				return false;
			}
			if(caractere == '.') {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean eReal(String expressao) {
		
		for(int i=0; i<expressao.length(); i++) {
			char caractere = expressao.charAt(i);
			if(!(isNumeroOuPonto(caractere))) {
				return false;
			}
		}
		
		return true;
	}
	
    public static boolean isNumeroOuPonto(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }
	
}
