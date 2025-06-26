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
    Show show;
    No esq, dir;
    boolean cor; // true = vermelho, false = preto

    public No(Show show) {
        this.show = show;
        this.cor = true; // novo nó é vermelho
        this.esq = null;
        this.dir = null;
    }

    public No(Show show, boolean cor) {
        this.show = show;
        this.cor = cor;
        this.esq = null;
        this.dir = null;
    }

    public No() {
        this.show = null;
        this.cor = false;
        this.esq = null;
        this.dir = null;
    }
}

class Arvore {
    private No raiz;
    public int comparacoes;

    public Arvore() {
        raiz = null;
    }

    private No rotacaoEsq(No no) {
        No dir = no.dir;
        No dirEsq = dir.esq;

        dir.esq = no;
        no.dir = dirEsq;
        return dir;
    }

    private No rotacaoDir(No no) {
        No esq = no.esq;
        No esqDir = esq.dir;

        esq.dir = no;
        no.esq = esqDir;
        return esq;
    }

    private No rotacaoDirEsq(No no) {
        no.dir = rotacaoDir(no.dir);
        return rotacaoEsq(no);
    }

    private No rotacaoEsqDir(No no) {
        no.esq = rotacaoEsq(no.esq);
        return rotacaoDir(no);
    }

    public void inserir(Show s) {
        if (raiz == null) {
            raiz = new No(s);
        

        } else if (raiz.esq == null && raiz.dir == null) {
            if (s.getTitle().compareTo(raiz.show.getTitle()) < 0) {
                raiz.esq = new No(s);
            } else {
                raiz.dir = new No(s);
            }
        

        } else if (raiz.esq == null) {
            if (s.getTitle().compareTo(raiz.show.getTitle()) < 0) {
                raiz.esq = new No(s);
            } else if (s.getTitle().compareTo(raiz.dir.show.getTitle()) < 0) {
                raiz.esq = new No(raiz.show);
                raiz.show = s;
            } else {
                raiz.esq = new No(raiz.show);
                raiz.show = raiz.dir.show;
                raiz.dir.show = s;
            }
            raiz.esq.cor = raiz.dir.cor = false;

        } else if (raiz.dir == null) {
            if (s.getTitle().compareTo(raiz.show.getTitle()) > 0) {
                raiz.dir = new No(s);
            } else if (s.getTitle().compareTo(raiz.esq.show.getTitle()) > 0) {
                raiz.dir = new No(raiz.show);
                raiz.show = s;
            } else {
                raiz.dir = new No(raiz.show);
                raiz.show = raiz.esq.show;
                raiz.esq.show = s;
            }
            raiz.esq.cor = raiz.dir.cor = false;

        } else {
            inserir(s, null, null, null, raiz);
        }

        raiz.cor = false;
    }

    private void inserir(Show s, No bisavo, No avo, No pai, No i) {
        if (i == null) {
            No novo = new No(s, true);
            if (s.getTitle().compareTo(pai.show.getTitle()) < 0) {
                pai.esq = novo;
            } else {
                pai.dir = novo;
            }
        

            if (pai.cor == true) {
                balancear(bisavo, avo, pai, novo);
            }

        } else {
            if (i.esq != null && i.dir != null && i.esq.cor && i.dir.cor) {
                i.cor = true;
                i.esq.cor = false;
                i.dir.cor = false;

                if (i == raiz) {
                    i.cor = false;
                } else if (pai != null && pai.cor == true) {
                    balancear(bisavo, avo, pai, i);
                }
            }

            if (s.getTitle().compareTo(i.show.getTitle()) < 0) {
                inserir(s, avo, pai, i, i.esq);
            } else if (s.getTitle().compareTo(i.show.getTitle()) > 0) {
                inserir(s, avo, pai, i, i.dir);
            } else {
                // Repetido, não insere
            }
        }
    }

    private void balancear(No bisavo, No avo, No pai, No i) {
        if (pai.cor == true) {
            if (pai.show.getTitle().compareTo(avo.show.getTitle()) > 0) {
                if (i.show.getTitle().compareTo(pai.show.getTitle()) > 0) {
                    avo = rotacaoEsq(avo);
                } else {
                    avo = rotacaoDirEsq(avo);
                }
            } else {
                if (i.show.getTitle().compareTo(pai.show.getTitle()) < 0) {
                    avo = rotacaoDir(avo);
                } else {
                    avo = rotacaoEsqDir(avo);
                }
            }

            if (bisavo == null) {
                raiz = avo;
            } else if (avo.show.getTitle().compareTo(bisavo.show.getTitle()) < 0) {
                bisavo.esq = avo;
            } else {
                bisavo.dir = avo;
            }

            avo.cor = false;
            avo.esq.cor = true;
            avo.dir.cor = true;
        }
    }

    public String pesquisar(String s) {
        comparacoes = 0;
        return "=>raiz  " + pesquisar(s, raiz);
    }

    private String pesquisar(String s, No i) {
        if (i == null) return "NAO";

        comparacoes++;
        if (s.equals(i.show.getTitle())) {
            return "SIM";
        } else {
            comparacoes++;
            if (s.compareTo(i.show.getTitle()) < 0) {
                return "esq " + pesquisar(s, i.esq);
            } else {
                return "dir " + pesquisar(s, i.dir);
            }
        }
    }
}


public class ArvoreAlvinegra {
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

        Arvore titulos = new Arvore();
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
            System.out.println(titulos.pesquisar(s));
            s = scanner.nextLine();
        }
        long fim = System.nanoTime();
        long tempoExecucao = fim - inicio;

        // Criação do arquivo de log
        FileWriter fw = new FileWriter("832722_arvoreAlvinegra.txt");
        PrintWriter pw = new PrintWriter(fw);
        pw.printf("matricula: 832722\ntempo execucao: %d\ncomparacoes: %d\n", tempoExecucao, comparacoes); // substitua 123456 pela sua matrícula
        pw.close();
        scanner.close();
    }
}
