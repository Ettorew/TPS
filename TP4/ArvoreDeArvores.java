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
    
    String getReleaseYear() {
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

class NoPrimeiro{
    int elem;
    NoPrimeiro esq, dir;
    NoSegundo raiz;
}

class NoSegundo{
    String nome;
    NoSegundo esq, dir;
}

class ArvoreDupla{
    
    private NoPrimeiro raiz;
    int length = 0;
    int comparacoes;

    public ArvoreDupla() {
        this.raiz = null;
        this.length = 0;
        criarArvore();
    }

    void criarArvore() {
        inserirNoPrimeiro(7);
        inserirNoPrimeiro(3);
        inserirNoPrimeiro(11);
        inserirNoPrimeiro(1);
        inserirNoPrimeiro(5);
        inserirNoPrimeiro(9);
        inserirNoPrimeiro(13);
        inserirNoPrimeiro(0);
        inserirNoPrimeiro(2);
        inserirNoPrimeiro(4);
        inserirNoPrimeiro(6);
        inserirNoPrimeiro(8);
        inserirNoPrimeiro(10);
        inserirNoPrimeiro(12);
        inserirNoPrimeiro(14);
    }

    
    private void inserirNoPrimeiro(int elem) {
        if (raiz == null) {
            raiz = new NoPrimeiro();
            raiz.elem = elem;
        } else {
            inserirNoPrimeiroRecursivo(elem, raiz);
        }
    }

    private void inserirNoPrimeiroRecursivo(int elem, NoPrimeiro atual) {
        if (elem == atual.elem) {
            return; // Elemento já existe, não insere duplicata
        } else if (elem < atual.elem) {
            if (atual.esq == null) {
                atual.esq = new NoPrimeiro();
                atual.esq.elem = elem;
            } else {
                inserirNoPrimeiroRecursivo(elem, atual.esq);
            }
        } else {
            if (atual.dir == null) {
                atual.dir = new NoPrimeiro();
                atual.dir.elem = elem;
            } else {
                inserirNoPrimeiroRecursivo(elem, atual.dir);
            }
        }
    }

    public void inserir(Show show){
        NoPrimeiro no;
        if(raiz == null){
            raiz = new NoPrimeiro();
            raiz.elem = Integer.parseInt(show.getReleaseYear()) % 15;
            no = raiz;
        }else{
            int elem = Integer.parseInt(show.getReleaseYear()) % 15;
            no = buscaNoPrimeiro(elem, raiz);
        }
        if(no.raiz == null){
            no.raiz = new NoSegundo();
            no.raiz.nome = show.getTitle();
        }else{
            NoSegundo novo = new NoSegundo();
            novo.nome = show.getTitle();
            inserirNoSegundo(no.raiz, novo);
        }
        length++;
    }

    private NoPrimeiro buscaNoPrimeiro(int elem, NoPrimeiro atual) {
        if (atual == null) {
            return null;
        }
        
        if (elem == atual.elem) {
            return atual;
        } else if (elem < atual.elem) {
            return buscaNoPrimeiro(elem, atual.esq);
        } else {
            return buscaNoPrimeiro(elem, atual.dir);
        }
    }

    private void inserirNoSegundo(NoSegundo atual, NoSegundo novo) {
        if (novo.nome.compareTo(atual.nome) < 0) {
            if (atual.esq == null) {
                atual.esq = novo;
            } else {
                inserirNoSegundo(atual.esq, novo);
            }
        } else {
            if (atual.dir == null) {
                atual.dir = novo;
            } else {
                inserirNoSegundo(atual.dir, novo);
            }
        }
    }

    public boolean pesquisar(String nome) {
        comparacoes = 0;
        System.out.print("raiz ");
        return pesquisarRecursivo(raiz, nome);
    }

    private boolean pesquisarRecursivo(NoPrimeiro atual, String nome) {
        boolean encontrou = false;
        
        if (atual != null) {
            comparacoes++;
            encontrou = pesquisarNoSegundo(atual.raiz, nome);

            if (!encontrou) {
                System.out.print("ESQ ");
                encontrou = pesquisarRecursivo(atual.esq, nome);
            }

            if (!encontrou) {
                System.out.print("DIR ");
                encontrou = pesquisarRecursivo(atual.dir, nome);
            }
        }
        
        return encontrou;
    }

    private boolean pesquisarNoSegundo(NoSegundo atual, String titulo) {
        if (atual == null) {
            comparacoes++;
            return false;
        }

        comparacoes++;
        if (titulo.equals(atual.nome)) {
            return true;
        }

        comparacoes++;
        if (titulo.compareTo(atual.nome) < 0) {
            System.out.print("esq ");
            return pesquisarNoSegundo(atual.esq, titulo);
        } else {
            System.out.print("dir ");
            return pesquisarNoSegundo(atual.dir, titulo);
        }
    }
}

public class ArvoreDeArvores {
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

        ArvoreDupla titulos = new ArvoreDupla();
        Scanner scanner = new Scanner(System.in);
        int k = 0;
        String s = scanner.nextLine();

        while(!s.equals("FIM")){
            for(int j=0; j<1368; j++){
                comparacoes++;
                if(s.equals(shows[j].getShowId())){
                    titulos.inserir(shows[j]);
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
        FileWriter fw = new FileWriter("matricula_arvoreDeArvores.txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("matricula: 1491845\ntempo execucao: %d\ncomparacoes: %d\n", tempoExecucao, comparacoes); // substitua 123456 pela sua matrícula
        pw.close();
        scanner.close();
    }
}
