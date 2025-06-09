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
        if (texto.equals("NaN")) {
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

    public LocalDate getDateAdded(){
        return transformaData(date_added);
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

// Classe para representar uma célula da lista duplamente encadeada
class Celula {
    Show show;
    Celula prox;
    Celula ant;
    
    public Celula(Show show) {
        this.show = show;
        this.prox = null;
        this.ant = null;
    }
}

// Classe para a lista duplamente encadeada
class ListaDuplamenteEncadeada {
    private Celula inicio;
    private Celula fim;
    private int n;
    
    // Construtor - equivalente ao criarLista() em C
    public ListaDuplamenteEncadeada() {
        this.inicio = null;
        this.fim = null;
        this.n = 0;
    }
    
    // Getters para acessar os atributos privados
    public Celula getInicio() {
        return inicio;
    }
    
    public Celula getFim() {
        return fim;
    }
    
    public int getTamanho() {
        return n;
    }
    
    public boolean isEmpty() {
        return n == 0;
    }
    
    // Inserir em posição específica
    public void inserir(int pos, Show s) {
        if (pos < 0 || pos > n) {
            System.out.println("Posição inválida!");
            return;
        }

        Celula nova = new Celula(s);

        if (pos == 0) { // Início
            nova.prox = inicio;
            if (inicio != null) {
                inicio.ant = nova;
            }
            inicio = nova;
            if (fim == null) {
                fim = nova;
            }
        } else if (pos == n) { // Fim
            nova.ant = fim;
            if (fim != null) {
                fim.prox = nova;
            }
            fim = nova;
            if (inicio == null) {
                inicio = nova;
            }
        } else { // Meio
            Celula atual = inicio;
            for (int i = 0; i < pos; i++) {
                atual = atual.prox;
            }

            nova.prox = atual;
            nova.ant = atual.ant;
            if (atual.ant != null) {
                atual.ant.prox = nova;
            }
            atual.ant = nova;
            if (pos == 0) {
                inicio = nova;
            }
        }

        n++;
    }
    
    // Inserir no início
    public void inserirInicio(Show s) {
        inserir(0, s);
    }
    
    // Inserir no fim
    public void inserirFim(Show s) {
        inserir(n, s);
    }
    
    // Remover de posição específica
    public Show remover(int pos) {
        if (n == 0) {
            System.out.println("Lista vazia!");
            return null;
        }
        if (pos < 0 || pos >= n) {
            System.out.println("Posição inválida!");
            return null;
        }

        Celula rem;
        Show valor;

        if (pos == 0) { // Remover do início
            rem = inicio;
            inicio = rem.prox;
            if (inicio != null) {
                inicio.ant = null;
            }
            if (rem == fim) {
                fim = null;
            }
        } else if (pos == n - 1) { // Remover do fim
            rem = fim;
            fim = rem.ant;
            if (fim != null) {
                fim.prox = null;
            }
            if (rem == inicio) {
                inicio = null;
            }
        } else { // Remover do meio
            rem = inicio;
            for (int i = 0; i < pos; i++) {
                rem = rem.prox;
            }

            rem.ant.prox = rem.prox;
            rem.prox.ant = rem.ant;
        }

        valor = rem.show;
        // Em Java não precisamos fazer free() - o garbage collector cuida disso
        n--;
        return valor;
    }
    
    // Remover do início
    public Show removerInicio() {
        return remover(0);
    }
    
    // Remover do fim
    public Show removerFim() {
        return remover(n - 1);
    }
    
    // Buscar elemento na posição
    public Show buscar(int pos) {
        if (pos < 0 || pos >= n) {
            System.out.println("Posição inválida!");
            return null;
        }
        
        Celula atual = inicio;
        for (int i = 0; i < pos; i++) {
            atual = atual.prox;
        }
        return atual.show;
    }
    
    // Imprimir a lista
    public void imprimirLista() {
        Celula atual = inicio;
        int i = 0;
        while (atual != null) {
            atual.show.imprimir();
            atual = atual.prox;
            i++;
        }
    }
}

class QuickSortShowLista {

    private ListaDuplamenteEncadeada lista;
    private int comparacoes = 0;
    private int movimentacoes = 0;

    public QuickSortShowLista(ListaDuplamenteEncadeada lista) {
        this.lista = lista;
    }

    public void sort() {
        if (lista == null || lista.getInicio() == null || lista.getFim() == null) {
            return;
        }
        comparacoes = 0;
        movimentacoes = 0;
        quicksort(lista.getInicio(), lista.getFim());
    }

    private void quicksort(Celula esq, Celula dir) {
        if (esq == null || dir == null || esq == dir || !celulaAntesDe(esq, dir)) {
            return;
        }

        Celula i = esq;
        Celula j = dir;
        Show pivo = obterPivo(esq, dir);

        while (i != null && j != null && (celulaAntesDe(i, j) || i == j)) {
            // Move i para direita enquanto elemento for menor que pivô
            while (i != null && compareShows(i.show, pivo) < 0) {
                i = i.prox;
            }
            
            // Move j para esquerda enquanto elemento for maior que pivô
            while (j != null && compareShows(j.show, pivo) > 0) {
                j = j.ant;
            }

            // Se os ponteiros não se cruzaram, faz a troca
            if (i != null && j != null && (celulaAntesDe(i, j) || i == j)) {
                swap(i, j);
                i = i.prox;
                j = j.ant;
            }
        }

        // Chamadas recursivas
        if (j != null && celulaAntesDe(esq, j)) {
            quicksort(esq, j);
        }
        if (i != null && celulaAntesDe(i, dir)) {
            quicksort(i, dir);
        }
    }

    // Obtém o pivô (elemento do meio)
    private Show obterPivo(Celula esq, Celula dir) {
        Celula slow = esq;
        Celula fast = esq;
        
        // Algoritmo da tartaruga e lebre para encontrar o meio
        while (fast != dir && fast.prox != dir && fast.prox != null) {
            fast = fast.prox.prox;
            slow = slow.prox;
        }
        
        return slow.show;
    }

    // Verifica se uma célula vem antes de outra na lista
    private boolean celulaAntesDe(Celula a, Celula b) {
        if (a == null || b == null) return false;
        
        Celula atual = a;
        while (atual != null && atual != b) {
            atual = atual.prox;
        }
        return atual == b;
    }

    // ✅ Compara primeiro pela data, depois pelo título
    private int compareShows(Show a, Show b) {
        LocalDate dateA = a.getDateAdded();
        LocalDate dateB = b.getDateAdded();
    
        comparacoes++;
        if (dateA == null && dateB == null) {
            // Desempate por título se ambas datas forem nulas
            comparacoes++;
            return a.getTitle().compareTo(b.getTitle());
        } else if (dateA == null) {
            return -1; // null é "menor", vem antes
        } else if (dateB == null) {
            return 1; // não-null vem depois
        }
    
        int cmp = dateA.compareTo(dateB);
        if (cmp != 0) return cmp;
    
        comparacoes++;
        return a.getTitle().compareTo(b.getTitle());
    }

    private void swap(Celula a, Celula b) {
        Show temp = a.show;
        a.show = b.show;
        b.show = temp;
        movimentacoes += 3; // cada troca tem 3 movimentações
    }

    public ListaDuplamenteEncadeada getSortedList() {
        return lista;
    }

    public int getComparacoes() {
        return comparacoes;
    }

    public int getMovimentacoes() {
        return movimentacoes;
    }

    // Método auxiliar para imprimir a lista (útil para debug)
    public void imprimirLista() {
        Celula atual = lista.getInicio();
        System.out.print("Lista: ");
        while (atual != null) {
            System.out.print(atual.show.getTitle() + " (" + atual.show.getDateAdded() + ") ");
            atual = atual.prox;
        }
        System.out.println();
    }
}

public class Quicksort {
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

        ListaDuplamenteEncadeada lista = new ListaDuplamenteEncadeada();
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();

        while(!s.equals("FIM")){
            for(int j=0; j<1368; j++){
                if(s.equals(shows[j].getShowId())){
                    lista.inserirFim(shows[j]);
                    j = 1368;
                }
            }
            s = scanner.nextLine();
        }

        long inicio = System.nanoTime();
        QuickSortShowLista quick = new QuickSortShowLista(lista);
        quick.sort();
        long fim = System.nanoTime();
        lista = quick.getSortedList();

        lista.imprimirLista();

        
        long tempoExecucao = fim - inicio;
        FileWriter fw = new FileWriter("matricula_quicksort3.txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("matricula: 1491845\ncomparacoes: %d\nmovimentacoes: %d\ntempo execucao: %d\n", quick.getComparacoes(), quick.getMovimentacoes(), tempoExecucao); // substitua 123456 pela sua matrícula
        pw.close();
        scanner.close();
    }

}


