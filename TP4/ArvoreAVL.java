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

class No {
    String nome;
    No esq, dir;
    int altura;

    public No(String nome) {
        this.nome = nome;
        this.altura = 1; // altura inicial de uma nova folha
    }
}

class AVL {
    private No raiz;
    int comparacoes;

    public void inserir(String nome) {
        raiz = inserirRecursivo(raiz, nome);
    }

    private No inserirRecursivo(No atual, String nome) {
        if (atual == null) {
            return new No(nome);
        }

        if (nome.compareTo(atual.nome) < 0) {
            atual.esq = inserirRecursivo(atual.esq, nome);
        } else if (nome.compareTo(atual.nome) > 0) {
            atual.dir = inserirRecursivo(atual.dir, nome);
        } else {
            // nomes iguais não são inseridos (sem duplicatas)
            return atual;
        }

        // Atualiza a altura do nó atual
        atual.altura = 1 + Math.max(altura(atual.esq), altura(atual.dir));

        // Calcula o fator de balanceamento
        int balance = fatorBalanceamento(atual);

        // Casos de desbalanceamento:

        // Rotação simples à direita (esquerda-esquerda)
        if (balance > 1 && nome.compareTo(atual.esq.nome) < 0)
            return rotacaoDireita(atual);

        // Rotação simples à esquerda (direita-direita)
        if (balance < -1 && nome.compareTo(atual.dir.nome) > 0)
            return rotacaoEsquerda(atual);

        // Rotação dupla esquerda-direita
        if (balance > 1 && nome.compareTo(atual.esq.nome) > 0) {
            atual.esq = rotacaoEsquerda(atual.esq);
            return rotacaoDireita(atual);
        }

        // Rotação dupla direita-esquerda
        if (balance < -1 && nome.compareTo(atual.dir.nome) < 0) {
            atual.dir = rotacaoDireita(atual.dir);
            return rotacaoEsquerda(atual);
        }

        return atual; // retorno sem rotação
    }

    private int altura(No n) {
        return n == null ? 0 : n.altura;
    }

    private int fatorBalanceamento(No n) {
        return n == null ? 0 : altura(n.esq) - altura(n.dir);
    }

    private No rotacaoDireita(No y) {
        No x = y.esq;
        No T2 = x.dir;

        // rotação
        x.dir = y;
        y.esq = T2;

        // atualiza alturas
        y.altura = Math.max(altura(y.esq), altura(y.dir)) + 1;
        x.altura = Math.max(altura(x.esq), altura(x.dir)) + 1;

        return x; // nova raiz
    }

    private No rotacaoEsquerda(No x) {
        No y = x.dir;
        No T2 = y.esq;

        // rotação
        y.esq = x;
        x.dir = T2;

        // atualiza alturas
        x.altura = Math.max(altura(x.esq), altura(x.dir)) + 1;
        y.altura = Math.max(altura(y.esq), altura(y.dir)) + 1;

        return y; // nova raiz
    }

    public boolean pesquisar(String titulo) {
        comparacoes = 0;
        System.out.print("=>raiz ");
        return pesquisarRecursivo(raiz, titulo);
    }

    private boolean pesquisarRecursivo(No atual, String titulo) {
        comparacoes++;
        if (atual == null) return false;

        comparacoes++;
        if (titulo.equals(atual.nome)) return true;

        comparacoes++;
        if (titulo.compareTo(atual.nome) < 0) {
            System.out.print("esq ");
            return pesquisarRecursivo(atual.esq, titulo);
        } else {
            System.out.print("dir ");
            return pesquisarRecursivo(atual.dir, titulo);
        }
    }
}

public class ArvoreAVL {
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

        AVL titulos = new AVL();
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
        FileWriter fw = new FileWriter("matricula_arvoreBinaria.txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("matricula: 1491845\ntempo execucao: %d\ncomparacoes: %d\n", tempoExecucao, comparacoes); // substitua 123456 pela sua matrícula
        pw.close();
        scanner.close();
    }
}
