import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;


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

    public static int[] ordenaInsercao(Show[] arr) {
        int[] comp_mov = new int[2];
        comp_mov[0] = 0; // comparações
        comp_mov[1] = 0; // movimentações
    
        for (int i = 1; i < arr.length; i++) {
            Show tmp = arr[i];
            int j = i - 1;
    
            while (j >= 0) {
                comp_mov[0]++;
                int cmp = tmp.type.compareTo(arr[j].type);
    
                if (cmp < 0 || (cmp == 0 && tmp.title.compareTo(arr[j].title) < 0)) {
                    comp_mov[0]++;
                    arr[j + 1] = arr[j];
                    comp_mov[1]++;
                    j--;
                } else {
                    break;
                }
            }
    
            arr[j + 1] = tmp;
            comp_mov[1]++;
        }
    
        return comp_mov;
    }
    

}

public class Insercao {
    public static void main(String args[]) throws FileNotFoundException, IOException {
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

        Show[] meusShows = new Show[300];
        int k = 0;
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();

        while(!s.equals("FIM")){
            for(int j=0; j<1368; j++){
                if(s.equals(shows[j].getShowId())){
                    meusShows[k] = shows[j];
                    k++;
                    j = 1368;
                }
            }
            s = scanner.nextLine();
        }

        long inicio = System.nanoTime();
        int[] comp_mov = Show.ordenaInsercao(meusShows);
        long fim = System.nanoTime();
        int movimentacoes = comp_mov[1];
        int comparacoes = comp_mov[0];

        for(int j=0; j<meusShows.length; j++){
            meusShows[j].imprimir();
        }

        long tempoExecucao = fim - inicio;
        FileWriter fw = new FileWriter("matricula_insercao.txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("matricula: 1491845\ncomparacoes: %d\nmovimentacoes: %d\ntempo execucao: %d\n", comparacoes, movimentacoes, tempoExecucao); // substitua 123456 pela sua matrícula
        pw.close();
        scanner.close();
    }
}

