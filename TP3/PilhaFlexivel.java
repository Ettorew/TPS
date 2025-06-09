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

class No {
    public Show elemento;
    public No proximo;

    public No(Show elemento) {
        this.elemento = elemento;
        this.proximo = null;
    }
}

class Pilha {
    private No topo;
    private int n;

    public Pilha() {
        topo = null;
        n = 0;
    }

    // Inserir no topo (push)
    public void push(Show show) {
        No novo = new No(show);
        novo.proximo = topo;
        topo = novo;
        n++;
    }

    // Remover do topo (pop)
    public Show pop() {
        if (topo == null) {
            System.out.println("Pilha vazia!");
            return null;
        }
        Show elemento = topo.elemento;
        topo = topo.proximo;
        n--;
        return elemento;
    }

    // Consultar o topo sem remover (peek)
    public Show peek() {
        if (topo == null) {
            System.out.println("Pilha vazia!");
            return null;
        }
        return topo.elemento;
    }

    // Verifica se a pilha está vazia
    public boolean isEmpty() {
        return topo == null;
    }

    // Imprimir a pilha do topo até o fundo
    public void imprimir() {
        No atual = topo;
        int i = n - 1;
        while (atual != null) {
            System.out.printf("[%d] ", i);
            atual.elemento.imprimir();
            atual = atual.proximo;
            i--;
        }
    }
}

public class PilhaFlexivel {
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

        Pilha pilha = new Pilha();
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();

        while(!s.equals("FIM")){
            int pos = Integer.parseInt(s.substring(1)); 
            pilha.push(shows[pos-1]);
            s = scanner.nextLine();
        }

        int tamComandos = scanner.nextInt();
        String removidos[] = new String[tamComandos];
        int k = 0;

        for(int j = 0; j < tamComandos; j++){
            String comando = scanner.next();
            if(comando.equals("I")){
                String id = scanner.next();
                int pos = Integer.parseInt(id.substring(1)); 
                pilha.push(shows[pos-1]);
            }else if(comando.equals("R")){
                removidos[k] = pilha.pop().getTitle();
                k++;
            }
        }

        for(int j = 0; j < k; j++){
            System.out.println("(R) " + removidos[j]);
        }

        pilha.imprimir();
    }
}

