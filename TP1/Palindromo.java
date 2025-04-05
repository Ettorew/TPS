import java.util.Scanner;

public class Palindromo{
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String palavra;
		palavra = scanner.nextLine().trim();
		
		while(!palavra.equals("FIM")){
			
			if(ePalindromo(palavra)) {
				System.out.println("SIM");
			} else {
				System.out.println("NAO");
			}
			
			palavra = scanner.nextLine().trim();
		}
		
		scanner.close();
	}
	
	public static boolean ePalindromo(String palavra) {
		
		boolean pal = true;
		for(int i=0; i<palavra.length()/2; i++) {
			if(palavra.charAt(i) != palavra.charAt(palavra.length() - i - 1)) {
				pal = false;
				i = palavra.length();
			}
		}
		return pal;
	}

}
