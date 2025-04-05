import java.util.Scanner;

public class Anagramas {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		String palavras = scanner.nextLine().trim();
		
		while (!palavras.equals("FIM")) {
			
			palavras = palavras.toLowerCase();
			String[] anagramas = palavras.split(" - ");
			
			System.out.println((saoAnagramas(anagramas[0], anagramas[1]) ? "SIM" : "NAO"));
			
			palavras = scanner.nextLine().trim();	
		}
		
		scanner.close();
	}
	
	public static boolean saoAnagramas(String a1, String a2) {
		
		char[] ao1 = a1.toCharArray();
		char[] ao2 = a2.toCharArray();
		
		bubbleSort(ao1);
		bubbleSort(ao2);
		
		String str1 = new String(ao1);
		String str2 = new String(ao2);
		
		if(!str1.equals(str2)) {
			return false;
		}
		
		return true;
	}
	
	public static void bubbleSort(char[] array) {
        int n = array.length;
        
        for (int i = 0; i < n - 1; i++) {
            
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    char temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
	}

}
