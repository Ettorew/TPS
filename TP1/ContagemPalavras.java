import java.util.Scanner;

public class ContagemPalavras {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String linha;
		linha = scanner.nextLine().trim();
		
		while(!linha.equals("FIM")) {
			
			String[] palavras = linha.split(" ");
			int n = palavras.length;
			System.out.println(n);
			
			linha = scanner.nextLine().trim();
		}
		
		scanner.close();
	}

}
