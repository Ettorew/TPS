import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

class Show {
    private String show_id, type, title, director, cast,
            country, date_added, release_year, rating, duration, listed_in, description;
    private String elenco[], listados[];

    public Show() {
        return;
    }

    String getShowId(){
        return show_id;
    }

    String getTitle(){
        return title;
    }

    private static String[] separaStrings(String linha) {
        String[] s = linha.split(",");

        for (int k = 0; k < s.length - 1; k++) {
            int menor = k;
            for (int l = k + 1; l < s.length; l++) {
                if (s[l].trim().compareTo(s[menor].trim()) < 0) {
                    menor = l;
                }
            }
            String aux = s[k];
            s[k] = s[menor];
            s[menor] = aux;
        }

        return s;
    }

    public LocalDate transformaData(String texto) {
        LocalDate data;
        if (texto == "Nan") {
            data = null;
        } else {
            String s[] = texto.replace(",", "").split(" ");
            Map<String, Integer> meses = new HashMap<>();

            meses.put("January", 1);
            meses.put("February", 2);
            meses.put("March", 3);
            meses.put("April", 4);
            meses.put("May", 5);
            meses.put("June", 6);
            meses.put("July", 7);
            meses.put("August", 8);
            meses.put("September", 9);
            meses.put("October", 10);
            meses.put("November", 11);
            meses.put("December", 12);
            int mes = meses.get(s[0]);
            int dia = Integer.parseInt(s[1]);
            int ano = Integer.parseInt(s[2]);
            data = LocalDate.of(ano, mes, dia);
        }

        return data;
    }

    public static Show ler(String linha) {
        Show show = new Show();
        int virgula = 0, caracteres = 0, tam = linha.length();
        boolean aspas = false;
        String separadores[] = new String[12];
        separadores[0] = "";

        for (int i = 0; i < tam; i++) {
            if (!aspas) {
                if (linha.charAt(i) == '"') {
                    aspas = true;
                } else if (linha.charAt(i) == ',') {
                    if (caracteres == 0) {
                        separadores[virgula] = "NaN";
                    }
                    virgula++;
                    separadores[virgula] = "";
                    caracteres = 0;
                } else {
                    separadores[virgula] += linha.charAt(i);
                    caracteres++;
                }
            } else {
                if (linha.charAt(i) == '"') {
                    aspas = false;
                } else {
                    separadores[virgula] += linha.charAt(i);
                    caracteres++;
                }
            }
        }

        show.show_id = separadores[0];
        show.type = separadores[1];
        show.title = separadores[2];
        show.director = separadores[3];
        show.cast = separadores[4];
        show.country = separadores[5];
        show.date_added = separadores[6];
        show.release_year = separadores[7];
        show.rating = separadores[8];
        show.duration = separadores[9];
        show.listed_in = separadores[10];
        show.description = separadores[11];

        show.elenco = separaStrings(show.cast);
        show.listados = separaStrings(show.listed_in);

        return show;
    }

    public void imprimir() {
        System.out.printf("=> %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## \r\n",
                this.show_id, this.title, this.type, this.director, Arrays.toString(this.elenco), this.country,
                this.date_added,
                this.release_year, this.rating, this.duration, Arrays.toString(this.listados));
    }

}

class HashTable {
    private String[] tabela = new String[30]; // 0–20: principais | 21–29: reserva
    private int proxReserva = 21;
    int comparacoes = 0;

    private int funcaoHash(String titulo) {
        int soma = 0;
        for (int i = 0; i < titulo.length(); i++) {
            soma += titulo.charAt(i);
        }
        return soma % 21;
    }

    public void inserir(String titulo) {
        int pos = funcaoHash(titulo);

        if (tabela[pos] == null) {
            tabela[pos] = titulo;
        } else {
            // Colisão → procurar espaço na área de reserva
            if (proxReserva >= 30) {
                return;
            }

            tabela[proxReserva++] = titulo;
        }
    }

    public boolean pesquisar(String titulo) {
        comparacoes = 0;
        int pos = funcaoHash(titulo);
        
        System.out.print(" (Posicao: " + pos + ") ");
        
        // Primeiro verifica a posição calculada pela função hash
        if (tabela[pos] != null && tabela[pos].equals(titulo)) {
            comparacoes++;
            return true;
        }
        comparacoes++;

        // Se não encontrou na posição principal, verifica na área de reserva
        for (int i = 21; i < proxReserva; i++) {
            comparacoes++;
            if (tabela[i] != null && tabela[i].equals(titulo)) {
                return true;
            }
        }

        return false;
    }

    public void exibirTabela() {
        System.out.println("=== TABELA HASH ===");
        System.out.println("Área Principal (0-20):");
        for (int i = 0; i <= 20; i++) {
            System.out.printf("[%2d] %s\n", i, (tabela[i] != null ? tabela[i] : "---"));
        }
        System.out.println("\nÁrea de Reserva (21-29):");
        for (int i = 21; i < 30; i++) {
            System.out.printf("[%2d] %s\n", i, (tabela[i] != null ? tabela[i] : "---"));
        }
        System.out.println("Próxima posição reserva: " + proxReserva);
    }
}

public class TabelaHashReserva {
    public static void main(String args[]) throws FileNotFoundException, IOException {
        long inicio = System.nanoTime();
        int comparacoes = 0;
        File file = new File("/tmp/disneyplus.csv");
        Scanner sc = new Scanner(file);
        String linha = new String();
        int i = 0;
        Show[] shows = new Show[1368];
        sc.nextLine();

        while (sc.hasNext()) {
            linha = sc.nextLine();
            shows[i] = Show.ler(linha);
            i++;
        }

        sc.close();

        HashTable titulos = new HashTable();
        Scanner scanner = new Scanner(System.in);
        int k = 0;
        String s = scanner.nextLine();

        while(!s.equals("FIM")){
            for(int j=0; j<1368; j++){
                comparacoes++;
                if(s.equals(shows[j].getShowId())){
                    titulos.inserir(shows[j].getTitle());
                    k++;
                    j = 1368;
                }
            }
            s = scanner.nextLine();
        }

        s = scanner.nextLine();

        while(!s.equals("FIM")){
            boolean existe = titulos.pesquisar(s);
            comparacoes += titulos.comparacoes;
            if(existe){
                System.out.println("SIM");
            }else{
                System.out.println("NAO");
            }
                s = scanner.nextLine();
        }
        long fim = System.nanoTime();
        long tempoExecucao = fim - inicio;

        // Criação do arquivo de log
        FileWriter fw = new FileWriter("832722_hashReserva.txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("matricula: 832722\ntempo execucao: %d\ncomparacoes: %d\n", tempoExecucao, comparacoes); // substitua 123456 pela sua matrícula
        pw.close();
        scanner.close();
    }
}

