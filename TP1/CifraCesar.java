import java.util.Scanner;

public class CifraCesar {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String palavra;
		palavra = scanner.nextLine().trim();
		
		while(!palavra.equals("FIM")){
			
			String novaPalavra = cifraCesar(palavra);
			System.out.println(novaPalavra);
			palavra = scanner.nextLine().trim();
		}
		
		scanner.close();
	}
	
	public static String cifraCesar(String palavra) {
		
		StringBuilder codificada = new StringBuilder();
		
		for(int i=0; i<palavra.length(); i++) {
			char c = palavra.charAt(i);
			char novoChar = (char) (c+3);
			codificada.append(novoChar);
		}
		
		return codificada.toString();
	}
}
