import java.util.Scanner;

public class SubstringLonga {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String linha;
		linha = scanner.nextLine().trim();
		
		while(!linha.equals("FIM")) {
			
			boolean[] visto = new boolean[256];
			int count = 0;
			int maior = 0;
			
			for(int j=0; j<linha.length(); j++) {
				char caractere = linha.charAt(j);
				int valorASCII = (int)caractere;
					
				if(!visto[valorASCII]) {
					visto[valorASCII] = true;
					count++;
				}
			}
			if(count > maior) {
				maior = count;
			}
			
			System.out.println(maior);
			linha = scanner.nextLine().trim();
		}
		
		scanner.close();
	}

}
