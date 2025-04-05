import java.util.Scanner;

public class ValidaSenha {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String senha;
		senha = scanner.nextLine().trim();
		
		while(!senha.equals("FIM")) {
			
			boolean minuscula = false;
			boolean maiuscula = false;
			boolean especial = false;
			boolean numero = false;
			boolean minCar8 = false;
			for(int i=0; i<senha.length(); i++) {
				
				if (senha.charAt(i) >= 'a' && senha.charAt(i) <= 'z') {
					minuscula = true;
				}
				
				if (senha.charAt(i) >= 'A' && senha.charAt(i) <= 'Z') {
					maiuscula = true;
				}
				
				if (!Character.isLetterOrDigit(senha.charAt(i))) {
				    especial = true;
				}
				
				if (senha.charAt(i) >= '0' && senha.charAt(i) <= '9') {
					numero = true;
				}
				
				if (senha.length() > 8) {
					minCar8 = true;
				}
			}
			
			if(minuscula && maiuscula && especial && numero && minCar8) {
				System.out.println("SIM");
			} else {
				System.out.println("NAO");
			}
			
			senha = scanner.nextLine().trim();
		}
		
		scanner.close();
	}

}
