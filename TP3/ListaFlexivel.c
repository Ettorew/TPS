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

typedef struct Celula {
    Show *dado;
    struct Celula *prox;
} Celula;

typedef struct {
    Celula *inicio;
    Celula *fim;
    int n;
} ListaFlexivel;

// Criar lista
ListaFlexivel *criarLista() {
    ListaFlexivel *lista = (ListaFlexivel *)malloc(sizeof(ListaFlexivel));
    lista->inicio = lista->fim = NULL;
    lista->n = 0;
    return lista;
}

// Inserir em posição específica
void inserir(ListaFlexivel *lista, int pos, Show *s) {
    if (pos < 0 || pos > lista->n) {
        printf("Posição inválida!\n");
        return;
    }

    Celula *nova = (Celula *)malloc(sizeof(Celula));
    nova->dado = s;
    nova->prox = NULL;

    if (pos == 0) {
        nova->prox = lista->inicio;
        lista->inicio = nova;
        if (lista->n == 0) lista->fim = nova;
    } else {
        Celula *atual = lista->inicio;
        for (int i = 0; i < pos - 1; i++) {
            atual = atual->prox;
        }
        nova->prox = atual->prox;
        atual->prox = nova;
        if (nova->prox == NULL) lista->fim = nova;
    }

    lista->n++;
}

// Inserir no início
void inserirInicio(ListaFlexivel *lista, Show *s) {
    inserir(lista, 0, s);
}

// Inserir no fim
void inserirFim(ListaFlexivel *lista, Show *s) {
    inserir(lista, lista->n, s);
}

// Remover de posição
Show *remover(ListaFlexivel *lista, int pos) {
    if (lista->n == 0) {
        printf("Lista vazia!\n");
        return NULL;
    }
    if (pos < 0 || pos >= lista->n) {
        printf("Posição inválida!\n");
        return NULL;
    }

    Celula *removida;
    Show *valor;

    if (pos == 0) {
        removida = lista->inicio;
        lista->inicio = lista->inicio->prox;
        if (lista->inicio == NULL) lista->fim = NULL;
    } else {
        Celula *anterior = lista->inicio;
        for (int i = 0; i < pos - 1; i++) {
            anterior = anterior->prox;
        }
        removida = anterior->prox;
        anterior->prox = removida->prox;
        if (anterior->prox == NULL) lista->fim = anterior;
    }

    valor = removida->dado;
    free(removida);
    lista->n--;
    return valor;
}

// Remover do início
Show *removerInicio(ListaFlexivel *lista) {
    return remover(lista, 0);
}

// Remover do fim
Show *removerFim(ListaFlexivel *lista) {
    return remover(lista, lista->n - 1);
}

// Imprimir lista
void imprimirLista(ListaFlexivel *lista) {
    Celula *atual = lista->inicio;
    while (atual != NULL) {
        imprimir(atual->dado);
        atual = atual->prox;
    }
}

// Tamanho da lista
int getN(ListaFlexivel *lista) {
    return lista->n;
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


    ListaFlexivel *lista = criarLista(1368);
    char entrada[20];
    scanf("%s", entrada);
    while(strcmp(entrada, "FIM") != 0) {
        for(int j = 0; j < i; j++) {
            if(strcmp(entrada, shows[j]->show_id) == 0) {
                inserirFim(lista, shows[j]);
                j = i;  
            }
        }
        scanf("%s", entrada);
    }

    int tamComandos;
    scanf("%d", &tamComandos);
    char comando[5];
    char id[20];
    char *removidos[tamComandos];
    int k = 0;

    for (int j = 0; j < tamComandos; j++) {
        scanf("%s", comando);

        if (strcmp(comando, "I*") == 0) {
            int posicao;
            scanf("%d %s", &posicao, id);
            int pos = atoi(id + 1);  // Pega número após o 'S'
            inserir(lista, posicao, shows[pos - 1]);

        } else if (strcmp(comando, "II") == 0) {
            scanf("%s", id);
            int pos = atoi(id + 1);
            inserirInicio(lista, shows[pos - 1]);

        } else if (strcmp(comando, "IF") == 0) {
            scanf("%s", id);
            int pos = atoi(id + 1);
            inserirFim(lista, shows[pos - 1]);

        } else if (strcmp(comando, "R*") == 0) {
            int posicao;
            scanf("%d", &posicao);
            Show *removido = remover(lista, posicao);
            removidos[k++] = removido->title;

        } else if (strcmp(comando, "RI") == 0) {
            Show *removido = removerInicio(lista);
            removidos[k++] = removido->title;

        } else if (strcmp(comando, "RF") == 0) {
            Show *removido = removerFim(lista);
            removidos[k++] = removido->title;
        }
    }

    for (int j = 0; j < k; j++) {
        printf("(R) %s\n", removidos[j]);
    }

    imprimirLista(lista);

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