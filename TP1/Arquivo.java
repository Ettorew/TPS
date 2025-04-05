import java.io.*;
import java.util.Scanner;

public class Arquivo {
	
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fileName = "numbers.txt";

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
        	int n = Integer.parseInt(scanner.nextLine().trim());
            
            // Escrevendo os valores no arquivo
            for (int i = 0; i < n; i++) {
            	double value = Double.parseDouble(scanner.nextLine().trim());
                raf.writeDouble(value);
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
            scanner.close();
            return;
        }
        
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {
            long fileLength = raf.length();
            long position = fileLength - 8;

            while (position >= 0) {
                raf.seek(position);
                double value = raf.readDouble();
                System.out.println(value);
                position -= 8;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        
        scanner.close();
    }
}

