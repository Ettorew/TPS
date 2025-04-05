import java.util.Scanner;
import java.util.Random;

public class AlteracaoAleatoria {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String palavra;
		palavra = scanner.nextLine().trim();
		Random gerador = new Random();
		gerador.setSeed(4);
		
		while(!palavra.equals("FIM")) {
			
			System.out.println(palavraAleatoria(palavra, gerador));
			palavra = scanner.nextLine().trim();
		}
		
		scanner.close();
	}
	
	public static String palavraAleatoria(String palavra, Random gerador) {
		
		StringBuilder sb = new StringBuilder(palavra);
		
		char rand1 = (char)('a' + (Math.abs(gerador.nextInt()) % 26));
		char rand2 = (char)('a' + (Math.abs(gerador.nextInt()) % 26));
		
		for(int i = 0; i < palavra.length(); i++) {
			
			if(palavra.charAt(i) == rand1) {
				sb.setCharAt(i, rand2);
			}
		}
		
		return sb.toString();
	}

}
