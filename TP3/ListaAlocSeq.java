import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

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

    String getDirector(){
        return director;
    }

    String getReleaseYear(){
        return release_year;
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

class ListaSequencial {
    private Show[] array;
    private int n;

    public ListaSequencial(int tam) {
        this.array = new Show[tam];
        this.n = 0;
    }

    public void inserir(int pos, Show show) {
        if (n >= array.length) {
            System.out.println("Lista cheia!");
            return;
        }
        if (pos < 0 || pos > n) {
            System.out.println("Posição inválida!");
            return;
        }
        for (int i = n; i > pos; i--) {
            array[i] = array[i - 1];
        }
        array[pos] = show;
        n++;
    }

    public void inserirInicio(Show show){
        inserir(0, show);
    }

    public void inserirFim(Show show){
        inserir(n, show);
    }

    public Show remover(int pos) {
        if (n == 0) {
            System.out.println("Lista vazia!");
            return null;
        }
        if (pos < 0 || pos >= n) {
            System.out.println("Posição inválida!");
            return null;
        }
        Show removido = array[pos];
        for (int i = pos; i < n - 1; i++) {
            array[i] = array[i + 1];
        }
        array[n - 1] = null;
        n--;
        return removido;
    }

    public Show removerInicio() {
        return remover(0);
    }

    public Show removerFim() {
        return remover(n - 1);
    }

    public void imprimir() {
        for (int i = 0; i < n; i++) {
            array[i].imprimir();
        }
    }

    public int getN() {
        return n;
    }
}

public class ListaAlocSeq {
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

        ListaSequencial lista = new ListaSequencial(1368);
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();

        while(!s.equals("FIM")){
            int pos = Integer.parseInt(s.substring(1)); 
            lista.inserirFim(shows[pos-1]);
            s = scanner.nextLine();
        }

        int tamComandos = scanner.nextInt();
        String removidos[] = new String[tamComandos];
        int k = 0;

        for(int j = 0; j < tamComandos; j++){
            String comando = scanner.next();
            if(comando.equals("I*")){
                int posicao = scanner.nextInt();
                String id = scanner.next();
                int pos = Integer.parseInt(id.substring(1)); 
                lista.inserir(posicao, shows[pos-1]);
            }else if(comando.equals("II")){
                String id = scanner.next();
                int pos = Integer.parseInt(id.substring(1)); 
                lista.inserirInicio(shows[pos-1]);
            }else if(comando.equals("IF")){
                String id = scanner.next();
                int pos = Integer.parseInt(id.substring(1)); 
                lista.inserirFim(shows[pos-1]);
            }else if(comando.equals("R*")){
                int posicao = scanner.nextInt();
                removidos[k] = lista.remover(posicao).getTitle();
                k++;
            }else if(comando.equals("RI")){
                removidos[k] = lista.removerInicio().getTitle();
                k++;
            }else if(comando.equals("RF")){
                removidos[k] = lista.removerFim().getTitle();
                k++;
            }
        }

        for(int j = 0; j < k; j++){
            System.out.println("(R) " + removidos[j]);
        }

        lista.imprimir();
    }
}
