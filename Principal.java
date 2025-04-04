import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class Show{
    private String show_id,type,title,director,cast,
    country,date_added,release_year,rating,duration,listed_in,description;

    public Show(){
        return;
    }

    public static Show ler(String linha){
        Show show = new Show();
        int tam = linha.length();
        int virgula = 0;
        boolean aspas = false;
        String separadores[] = new String[12];
        separadores[0] = "";

        for(int i=0; i<tam; i++){
            if(!aspas){
                if(linha.charAt(i) == '"'){
                    aspas = true;
                }else if(linha.charAt(i) == ','){
                    virgula++;
                    separadores[virgula] = "";
                }else{
                    separadores[virgula] += linha.charAt(i);
                }
            }else{
                if(linha.charAt(i) == '"'){
                    aspas = false;
                }else{
                    separadores[virgula] += linha.charAt(i);
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

        return show;
    }

    public void imprimir() {
        System.out.printf("[=> %s ## %s ## %s ##  ## %s ## %s ##  ## %s ## %s ## %s ## %s ## %s ## %s\r\n",
            this.show_id, this.type, this.title, this.director, this.cast, this.country, this.date_added, 
            this.release_year, this.rating, this.duration, this.listed_in, this.description);
    }
    

}

public class Principal{
    public static void main(String args[]) throws FileNotFoundException{
        File file = new File("disneyplus.csv");
        Scanner sc = new Scanner(file);
        String linha = new String();
        int i = 0;
        Show[] shows = new Show[1368];
        
        while(sc.hasNext()){
            linha = sc.nextLine();
            shows[i] = Show.ler(linha);
        }

        for(; i>=0; i--){
            shows[i].imprimir();
        }

        sc.close();
    }
}