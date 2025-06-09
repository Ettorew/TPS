#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
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
    int dia, mes, ano;
    int tem_data;
} Show;

int getTam(char* linha){
    int tam = 0;
    while(linha[tam] != '\0'){
        tam++;
    }
    return tam;
}

void liberarShow(Show *s) {
    if (s == NULL) return;

    free(s->show_id);
    free(s->type);
    free(s->title);
    free(s->director);
    free(s->cast);
    free(s->country);
    free(s->date_added);
    free(s->release_year);
    free(s->rating);
    free(s->duration);
    free(s->listed_in);
    free(s->description);

    if (s->elenco != NULL) {
        for (int i = 0; i < s->tam_elenco; i++) {
            free(s->elenco[i]);
        }
        free(s->elenco);
    }

    if (s->listados != NULL) {
        for (int i = 0; i < s->tam_listados; i++) {
            free(s->listados[i]);
        }
        free(s->listados);
    }
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
    Show *show;
    struct Celula *prox;
    struct Celula *ant;
} Celula;

typedef struct {
    Celula *inicio;
    Celula *fim;
    int n;
} ListaDuplamenteEncadeada;

// Criar lista
ListaDuplamenteEncadeada *criarLista() {
    ListaDuplamenteEncadeada *lista = (ListaDuplamenteEncadeada *)malloc(sizeof(ListaDuplamenteEncadeada));
    lista->inicio = lista->fim = NULL;
    lista->n = 0;
    return lista;
}

void inserir(ListaDuplamenteEncadeada *lista, int pos, Show *s) {
    if (pos < 0 || pos > lista->n) {
        printf("Posição inválida!\n");
        return;
    }

    Celula *nova = (Celula *)malloc(sizeof(Celula));
    nova->show = s;
    nova->prox = nova->ant = NULL;

    if (pos == 0) { // Início
        nova->prox = lista->inicio;
        if (lista->inicio != NULL)
            lista->inicio->ant = nova;
        lista->inicio = nova;
        if (lista->fim == NULL)
            lista->fim = nova;
    } else if (pos == lista->n) { // Fim
        nova->ant = lista->fim;
        if (lista->fim != NULL)
            lista->fim->prox = nova;
        lista->fim = nova;
        if (lista->inicio == NULL)
            lista->inicio = nova;
    } else { // Meio
        Celula *atual = lista->inicio;
        for (int i = 0; i < pos; i++)
            atual = atual->prox;

        nova->prox = atual;
        nova->ant = atual->ant;
        if (atual->ant != NULL)
            atual->ant->prox = nova;
        atual->ant = nova;
        if (pos == 0)
            lista->inicio = nova;
    }

    lista->n++;
}

void inserirInicio(ListaDuplamenteEncadeada *lista, Show *s) {
    inserir(lista, 0, s);
}

void inserirFim(ListaDuplamenteEncadeada *lista, Show *s) {
    inserir(lista, lista->n, s);
}

Show *remover(ListaDuplamenteEncadeada *lista, int pos) {
    if (lista->n == 0) {
        printf("Lista vazia!\n");
        return NULL;
    }
    if (pos < 0 || pos >= lista->n) {
        printf("Posição inválida!\n");
        return NULL;
    }

    Celula *rem;
    Show *valor;

    if (pos == 0) {
        rem = lista->inicio;
        lista->inicio = rem->prox;
        if (lista->inicio != NULL)
            lista->inicio->ant = NULL;
        if (rem == lista->fim)
            lista->fim = NULL;
    } else if (pos == lista->n - 1) {
        rem = lista->fim;
        lista->fim = rem->ant;
        if (lista->fim != NULL)
            lista->fim->prox = NULL;
        if (rem == lista->inicio)
            lista->inicio = NULL;
    } else {
        rem = lista->inicio;
        for (int i = 0; i < pos; i++)
            rem = rem->prox;

        rem->ant->prox = rem->prox;
        rem->prox->ant = rem->ant;
    }

    valor = rem->show;
    free(rem);
    lista->n--;
    return valor;
}

Show *removerInicio(ListaDuplamenteEncadeada *lista) {
    return remover(lista, 0);
}

Show *removerFim(ListaDuplamenteEncadeada *lista) {
    return remover(lista, lista->n - 1);
}

void imprimirLista(ListaDuplamenteEncadeada *lista) {
    Celula *atual = lista->inicio;
    int i = 0;
    while (atual != NULL) {
        imprimir(atual->show);
        atual = atual->prox;
    }
}

int mesParaInt(char *mes) {
    if (strcmp(mes, "January") == 0) return 1;
    if (strcmp(mes, "February") == 0) return 2;
    if (strcmp(mes, "March") == 0) return 3;
    if (strcmp(mes, "April") == 0) return 4;
    if (strcmp(mes, "May") == 0) return 5;
    if (strcmp(mes, "June") == 0) return 6;
    if (strcmp(mes, "July") == 0) return 7;
    if (strcmp(mes, "August") == 0) return 8;
    if (strcmp(mes, "September") == 0) return 9;
    if (strcmp(mes, "October") == 0) return 10;
    if (strcmp(mes, "November") == 0) return 11;
    if (strcmp(mes, "December") == 0) return 12;
    return 0;
}

void transformaData(Show *s) {
    if (strcmp(s->date_added, "NaN") == 0 || s->date_added == NULL) {
        s->tem_data = 0;
        return;
    }

    trim(s->date_added);

    char mes[20];
    int dia, ano;

    if (sscanf(s->date_added, "%s %d, %d", mes, &dia, &ano) == 3) {
        s->mes = mesParaInt(mes);
        s->dia = dia;
        s->ano = ano;
        s->tem_data = 1;
    } else {
        s->tem_data = 0;
    }
}

void iteraTransformaData(ListaDuplamenteEncadeada *lista) {
    Celula *atual = lista->inicio;
    while (atual != NULL) {
        transformaData(atual->show);
        atual = atual->prox;
    }
}

int compararShows(const Show *a, const Show *b, int *comparacoes) {
    (*comparacoes)++;
    if (!a->tem_data && !b->tem_data) {
        (*comparacoes)++;
        return strcmp(a->title, b->title);
    } else if (!a->tem_data) return -1;
    else if (!b->tem_data) return 1;

    if (a->ano != b->ano) return a->ano - b->ano;
    if (a->mes != b->mes) return a->mes - b->mes;
    if (a->dia != b->dia) return a->dia - b->dia;
    (*comparacoes)++;
    return strcmp(a->title, b->title);
}

void swapCelulas(Celula *a, Celula *b, int *movimentacoes) {
    Show *temp = a->show;
    a->show = b->show;
    b->show = temp;
    *movimentacoes += 3;
}

// Retorna o ponteiro para a célula na posição `index`
Celula *obterCelula(ListaDuplamenteEncadeada *lista, int index) {
    Celula *atual = lista->inicio;
    for (int i = 0; i < index && atual != NULL; i++) {
        atual = atual->prox;
    }
    return atual;
}

// Função para obter o índice de uma célula (útil para debug ou adaptações)
int indiceDeCelula(ListaDuplamenteEncadeada *lista, Celula *cel) {
    Celula *atual = lista->inicio;
    int i = 0;
    while (atual != NULL) {
        if (atual == cel) return i;
        atual = atual->prox;
        i++;
    }
    return -1;
}

// Verifica se uma célula vem antes de outra na lista
int celulaAntesDe(Celula *a, Celula *b) {
    if (a == NULL || b == NULL) return 0;
    
    Celula *atual = a;
    while (atual != NULL && atual != b) {
        atual = atual->prox;
    }
    return atual == b; // retorna 1 se encontrou b, 0 caso contrário
}

// Particiona a lista e retorna o pivô na posição correta
Celula *particionar(Celula *inicio, Celula *fim, int *comparacoes, int *movimentacoes) {
    if (inicio == NULL || fim == NULL || inicio == fim) {
        return inicio;
    }
    
    // Escolhe o último elemento como pivô
    Show *pivo = fim->show;
    Celula *i = inicio->ant; // Começa antes do início
    
    Celula *atual = inicio;
    while (atual != fim) {
        (*comparacoes)++;
        if (compararShows(atual->show, pivo, comparacoes) <= 0) {
            // Move i para frente
            i = (i == NULL) ? inicio : i->prox;
            
            // Troca elementos se i != atual
            if (i != atual) {
                swapCelulas(i, atual, movimentacoes);
            }
        }
        atual = atual->prox;
    }
    
    // Coloca o pivô na posição correta
    i = (i == NULL) ? inicio : i->prox;
    if (i != fim) {
        swapCelulas(i, fim, movimentacoes);
    }
    
    return i;
}

void quicksortLista(Celula *inicio, Celula *fim, int *comparacoes, int *movimentacoes) {
    if (inicio == NULL || fim == NULL || inicio == fim || !celulaAntesDe(inicio, fim)) {
        return;
    }
    
    // Particiona e obtém a posição do pivô
    Celula *pivo = particionar(inicio, fim, comparacoes, movimentacoes);
    
    // Ordena recursivamente as duas partes
    if (pivo != NULL) {
        // Ordena a parte esquerda (antes do pivô)
        if (pivo->ant != NULL && celulaAntesDe(inicio, pivo->ant)) {
            quicksortLista(inicio, pivo->ant, comparacoes, movimentacoes);
        }
        
        // Ordena a parte direita (depois do pivô)
        if (pivo->prox != NULL && celulaAntesDe(pivo->prox, fim)) {
            quicksortLista(pivo->prox, fim, comparacoes, movimentacoes);
        }
    }
}

void ordenarLista(ListaDuplamenteEncadeada *lista, int *comparacoes, int *movimentacoes) {
    *comparacoes = 0;
    *movimentacoes = 0;
    
    if (lista == NULL || lista->inicio == NULL || lista->fim == NULL) {
        return;
    }
    
    quicksortLista(lista->inicio, lista->fim, comparacoes, movimentacoes);
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

    ListaDuplamenteEncadeada *lista = criarLista();  // cria a lista
    char s[50];

    while (scanf("%s", s) && strcmp(s, "FIM") != 0) {
        for (int j = 0; j < i; j++) {
            if (strcmp(s, shows[j]->show_id) == 0) {
                inserirFim(lista, shows[j]);
                break;  // encontrou e inseriu, pode sair do loop interno
            }
        }
    }

    iteraTransformaData(lista);

    int comparacoes = 0, movimentacoes = 0;
    clock_t inicio = clock();
    ordenarLista(lista, &comparacoes, &movimentacoes);
    clock_t fim = clock();

    double tempo = (double)(fim - inicio) / (double)CLOCKS_PER_SEC;

    imprimirLista(lista);

    FILE *log = fopen("matricula_quicksort2.txt", "w");
    fprintf(log, "matricula: 1491845\ncomparacoes: %d\nmovimentacoes: %d\ntempo execucao: %lf\n", comparacoes, movimentacoes, tempo);
    fclose(log);

    for(int j = 0; j < i; j++) {
        liberarShow(shows[j]);
    }

    return 0;
}
