#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAX_SHOWS 1368
#define TAM_LINHA 1000

typedef struct Show {
    char *show_id;
    char *type;
    char *title;
    char *director;
    char *cast;
    char *country;
    char *date_added;
    char *release_year;
    char *rating;
    char *duration;
    char *listed_in;
    char *description;
    char **elenco;
    char **listados;
    int tam_elenco;
    int tam_listados;
} Show;

int getTam(char* linha){
    int tam = 0;
    while(linha[tam] != '\0'){
        tam++;
    }
    return tam;
}


void trim(char *str) {
    int start = 0;
    while (str[start] == ' ') start++;

    int end = strlen(str) - 1;
    while (end > start && str[end] == ' ') end--;

    int i, j = 0;
    for (i = start; i <= end; i++) {
        str[j++] = str[i];
    }
    str[j] = '\0';
}

char *extraiCampo(char *next, int *extraido){
    char *campo = malloc(1000);
    int i=0, j=0, verifica = 1, aspas = 0, caracteres = 0;

    while(verifica && next[i] != '\0' ){
        if(!aspas){
            if(next[i] == ','){
                verifica = !verifica;
            }else{
                if(next[i] == '"'){
                    aspas = !aspas;
                }else{
                campo[j++] = next[i];
                caracteres++;
                }
            }
        }else{
            if(next[i] == '"'){
                aspas = !aspas;
            }else{
            campo[j++] = next[i];
            caracteres++;
            }
        }
        i++;
    }
    if(caracteres == 0){
        campo[j++] = 'N';
        campo[j++] = 'a';
        campo[j++] = 'N';
    }
    campo[j] = '\0';
    *extraido = i;

    return campo;
}

char **separaString(char *linha, int *tam) {
    int i = 0, j = 0, k = 0;
    *tam = 1;

    for (i = 0; linha[i] != '\0'; i++) {
        if (linha[i] == ',') {
            (*tam)++;
        }
    }

    char **s = (char **)malloc(sizeof(char*) * (*tam));
    for (i = 0; i < *tam; i++) {
        s[i] = NULL;
    }

    i = j = k = 0;
    s[k] = (char *)malloc(sizeof(char) * (strlen(linha) + 1));

    while (linha[i] != '\0') {
        if (linha[i] == ',') {
            s[k][j] = '\0';
            trim(s[k]);
            k++;
            j = 0;
            s[k] = (char *)malloc(sizeof(char) * (strlen(linha) + 1));
        } else if (linha[i] != '"') {
            s[k][j++] = linha[i];
        }
        i++;
    }

    s[k][j] = '\0';
    trim(s[k]);

    for (int m = 0; m < *tam - 1; m++) {
        int menor = m;
        for (int n = m + 1; n < *tam; n++) {
            if (strcmp(s[n], s[menor]) < 0) {
                menor = n;
            }
        }
        if (menor != m) {
            char *temp = s[m];
            s[m] = s[menor];
            s[menor] = temp;
        }
    }

    return s;
}

Show *ler(char *linha){
    Show *show = (Show *)malloc(sizeof(Show));
    int i = 0, offset = 0;
    char *campos[12];

    for (int c = 0; c < 12; c++) {
        campos[c] = extraiCampo(linha + i, &offset);
        i += offset;
    }

    show->show_id = campos[0];
    show->type = campos[1];
    show->title = campos[2];
    show->director = campos[3];
    show->cast = campos[4];
    show->country = campos[5];
    show->date_added = campos[6];
    show->release_year = campos[7];
    show->rating = campos[8];
    show->duration = campos[9];
    show->listed_in = campos[10];
    show->description = campos[11];

    show->elenco = separaString(show->cast, &show->tam_elenco);
    show->listados = separaString(show->listed_in, &show->tam_listados);

    return show;
}

void imprimiArray(char **array, int tam){
    printf("[");
    for(int i=0; i<tam; i++){
        printf("%s", array[i]);
        if(i < tam-1){
            printf(", ");
        }
    }
    printf("]");
}

void imprimir(Show *show){
    printf("=> %s ## %s ## %s ## %s ## ", show->show_id, show->title, show->type, show->director);
    imprimiArray(show->elenco, show->tam_elenco);
    printf(" ## %s ## %s ## %s ## %s ## %s ## ", show->country, show->date_added, 
        show->release_year, show->rating, show->duration);
    imprimiArray(show->listados, show->tam_listados);
    printf(" ## \n");
}

typedef struct {
    Show **array;
    int capacidade, primeiro, ultimo, n;
} FilaCircular;

// Criar fila
FilaCircular *newFila(int tam) {
    FilaCircular *fila = (FilaCircular *)malloc(sizeof(FilaCircular));
    fila->array = (Show **)malloc(tam * sizeof(Show *));
    fila->capacidade = tam;
    fila->primeiro = fila->ultimo = fila->n = 0;
    return fila;
}

Show *remover(FilaCircular *fila) {
    if (fila->n == 0) {
        return NULL;
    }
    Show *removido = fila->array[fila->primeiro];
    fila->primeiro = (fila->primeiro + 1) % fila->capacidade;
    fila->n--;
    return removido;
}

void inserir(FilaCircular *fila, Show *s) {
    if (fila->n >= fila->capacidade) {
        remover(fila);
    }
    fila->array[fila->ultimo] = s;
    fila->ultimo = (fila->ultimo + 1) % fila->capacidade;
    fila->n++;
}

// Imprimir fila
void imprimirFila(FilaCircular *fila) {
    for (int i = fila->primeiro; i != fila->ultimo; i = (i + 1) % fila->capacidade) {
        printf("[%d] ", i - 1);
        imprimir(fila->array[i]);
    }
}

void mediaRY(FilaCircular *fila) {
    int media = 0;
    if(fila->n != 0){
        int j = 0;
        for(int i = 0, idx = fila->primeiro; i < fila->n; i++, idx = (idx + 1) % fila->capacidade) {
            media += atoi(fila->array[idx]->release_year);
            j++;
        }
        media /= j;
    }
    printf("[Media] %d\n", media);
}

// Tamanho da fila
int getN(FilaCircular *fila) {
    return fila->n;
}

int main() {
    FILE *file = fopen("/tmp/disneyplus.csv", "r");
    if (file == NULL) {
        printf("Erro ao abrir o arquivo.\n");
        return 1;
    }

    char linha[TAM_LINHA];
    int i = 0;

    Show *shows[MAX_SHOWS];

    // Skip header line
    fgets(linha, TAM_LINHA, file);

    while (fgets(linha, TAM_LINHA, file) != NULL && i < MAX_SHOWS) {
        int j = 0;
        while (linha[j] != '\0') {
            if (linha[j] == '\n' || linha[j] == '\r') {
                linha[j] = '\0';
            }
            j++;
        }

        shows[i] = ler(linha);
        i++;
    }
    fclose(file);


    FilaCircular *fila = newFila(5);
    char entrada[20];
    scanf("%s", entrada);
    while(strcmp(entrada, "FIM") != 0) {
        for(int j = 0; j < i; j++) {
            if(strcmp(entrada, shows[j]->show_id) == 0) {
                inserir(fila, shows[j]);
                mediaRY(fila);
                j = i;  
            }
        }
        scanf("%s", entrada);
    }

    int tamComandos;
    scanf("%d", &tamComandos);
    char comando[5];
    char id[20];
    int k = 0;

    for (int j = 0; j < tamComandos; j++) {
        scanf("%s", comando);
        if (strcmp(comando, "I") == 0) {
            scanf("%s", id);
            int pos = atoi(id + 1);
            inserir(fila, shows[pos - 1]);
            mediaRY(fila);
        }else if (strcmp(comando, "R") == 0) {
            Show *removido = remover(fila);
            if (removido != NULL) {
                printf("(R) %s\n", removido->title);
            }
        }
    }

    imprimirFila(fila);

    // Free allocated memory
    for(int j = 0; j < i; j++) {
        // Free the elenco array elements
        for(int k = 0; k < shows[j]->tam_elenco; k++) {
            free(shows[j]->elenco[k]);
        }
        free(shows[j]->elenco);

        // Free the listados array elements
        for(int k = 0; k < shows[j]->tam_listados; k++) {
            free(shows[j]->listados[k]);
        }
        free(shows[j]->listados);

        // Free the string fields
        free(shows[j]->show_id);
        free(shows[j]->type);
        free(shows[j]->title);
        free(shows[j]->director);
        free(shows[j]->cast);
        free(shows[j]->country);
        free(shows[j]->date_added);
        free(shows[j]->release_year);
        free(shows[j]->rating);
        free(shows[j]->duration);
        free(shows[j]->listed_in);
        free(shows[j]->description);

        // Free the show struct itself
        free(shows[j]);
    }

    return 0;
}