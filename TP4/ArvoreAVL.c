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

typedef struct No {
    char nome[100];
    struct No* esq;
    struct No* dir;
    int altura;
} No;

typedef struct AVL {
    No* raiz;
    int comparacoes;
} AVL;

// Função para criar um novo nó
No* criarNo(const char* nome) {
    No* novoNo = (No*)malloc(sizeof(No));
    if (novoNo == NULL) {
        printf("Erro de alocação de memória\n");
        exit(1);
    }
    strcpy(novoNo->nome, nome);
    novoNo->esq = NULL;
    novoNo->dir = NULL;
    novoNo->altura = 1; // altura inicial de uma nova folha
    return novoNo;
}

// Função para inicializar a AVL
void inicializarAVL(AVL* avl) {
    avl->raiz = NULL;
    avl->comparacoes = 0;
}

// Função para obter a altura de um nó
int altura(No* n) {
    return n == NULL ? 0 : n->altura;
}

// Função para calcular o fator de balanceamento
int fatorBalanceamento(No* n) {
    return n == NULL ? 0 : altura(n->esq) - altura(n->dir);
}

// Função para encontrar o máximo entre dois inteiros
int max(int a, int b) {
    return (a > b) ? a : b;
}

// Rotação simples à direita
No* rotacaoDireita(No* y) {
    No* x = y->esq;
    No* T2 = x->dir;

    // rotação
    x->dir = y;
    y->esq = T2;

    // atualiza alturas
    y->altura = max(altura(y->esq), altura(y->dir)) + 1;
    x->altura = max(altura(x->esq), altura(x->dir)) + 1;

    return x; // nova raiz
}

// Rotação simples à esquerda
No* rotacaoEsquerda(No* x) {
    No* y = x->dir;
    No* T2 = y->esq;

    // rotação
    y->esq = x;
    x->dir = T2;

    // atualiza alturas
    x->altura = max(altura(x->esq), altura(x->dir)) + 1;
    y->altura = max(altura(y->esq), altura(y->dir)) + 1;

    return y; // nova raiz
}

// Função recursiva para inserir um nó
No* inserirRecursivo(No* atual, const char* nome) {
    if (atual == NULL) {
        return criarNo(nome);
    }

    int comparacao = strcmp(nome, atual->nome);
    
    if (comparacao < 0) {
        atual->esq = inserirRecursivo(atual->esq, nome);
    } else if (comparacao > 0) {
        atual->dir = inserirRecursivo(atual->dir, nome);
    } else {
        // nomes iguais não são inseridos (sem duplicatas)
        return atual;
    }

    // Atualiza a altura do nó atual
    atual->altura = 1 + max(altura(atual->esq), altura(atual->dir));

    // Calcula o fator de balanceamento
    int balance = fatorBalanceamento(atual);

    // Casos de desbalanceamento:

    // Rotação simples à direita (esquerda-esquerda)
    if (balance > 1 && strcmp(nome, atual->esq->nome) < 0)
        return rotacaoDireita(atual);

    // Rotação simples à esquerda (direita-direita)
    if (balance < -1 && strcmp(nome, atual->dir->nome) > 0)
        return rotacaoEsquerda(atual);

    // Rotação dupla esquerda-direita
    if (balance > 1 && strcmp(nome, atual->esq->nome) > 0) {
        atual->esq = rotacaoEsquerda(atual->esq);
        return rotacaoDireita(atual);
    }

    // Rotação dupla direita-esquerda
    if (balance < -1 && strcmp(nome, atual->dir->nome) < 0) {
        atual->dir = rotacaoDireita(atual->dir);
        return rotacaoEsquerda(atual);
    }

    return atual; // retorno sem rotação
}

// Função pública para inserir
void inserir(AVL* avl, const char* nome) {
    avl->raiz = inserirRecursivo(avl->raiz, nome);
}

// Função recursiva para pesquisar
int pesquisarRecursivo(No* atual, const char* titulo, int* comparacoes) {
    (*comparacoes)++;
    if (atual == NULL) return 0; // false

    (*comparacoes)++;
    if (strcmp(titulo, atual->nome) == 0) return 1; // true

    (*comparacoes)++;
    if (strcmp(titulo, atual->nome) < 0) {
        printf("esq ");
        return pesquisarRecursivo(atual->esq, titulo, comparacoes);
    } else {
        printf("dir ");
        return pesquisarRecursivo(atual->dir, titulo, comparacoes);
    }
}

// Função pública para pesquisar
int pesquisar(AVL* avl, const char* titulo) {
    avl->comparacoes = 0;
    printf("raiz ");
    return pesquisarRecursivo(avl->raiz, titulo, &avl->comparacoes);
}

// Função para liberar a memória da árvore
void liberarArvore(No* raiz) {
    if (raiz != NULL) {
        liberarArvore(raiz->esq);
        liberarArvore(raiz->dir);
        free(raiz);
    }
}

// Função para liberar a AVL
void liberarAVL(AVL* avl) {
    liberarArvore(avl->raiz);
    avl->raiz = NULL;
}

// Função para imprimir a árvore em ordem (para teste)
void imprimirEmOrdem(No* raiz) {
    if (raiz != NULL) {
        imprimirEmOrdem(raiz->esq);
        printf("%s ", raiz->nome);
        imprimirEmOrdem(raiz->dir);
    }
}

int main() {
    int comparacoes = 0;
    FILE *file = fopen("/tmp/disneyplus.csv", "r");
    if (file == NULL) {
        perror("Erro ao abrir o arquivo");
        return 1;
    }

    Show shows[MAX_SHOWS];
    char linha[TAM_LINHA];
    int i = 0;

    // Ignora o cabeçalho
    fgets(linha, sizeof(linha), file);

    clock_t inicio = clock();

    while (fgets(linha, TAM_LINHA, file) != NULL && i < MAX_SHOWS) {
        int j = 0;
        while (linha[j] != '\0') {
            if (linha[j] == '\n' || linha[j] == '\r') {
                linha[j] = '\0';
            }
            j++;
        }

        shows[i] = *ler(linha);
        i++;
    }

    fclose(file);

    AVL titulos;
    int k = 0;

    char entrada[100];

    // Primeira parte: leitura dos show_ids
    while (1) {
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\n")] = '\0';
        if (strcmp(entrada, "FIM") == 0) break;

        for (int j = 0; j < MAX_SHOWS; j++) {
            comparacoes++;
            if (strcmp(entrada, shows[j].show_id) == 0) {
                inserir(&titulos, shows[j].title);
                break;
            }
        }
    }

    while (1) {
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\n")] = '\0';
        if (strcmp(entrada, "FIM") == 0) break;

        int existe = pesquisar(&titulos, entrada);
        
        if (existe) {
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }
    }

    clock_t fim = clock();
    double tempoExecucao = ((double)(fim - inicio)) / CLOCKS_PER_SEC * 1000000000;

    // Criação do arquivo de log
    FILE *log = fopen("832722_avl.txt", "w");
    if (log != NULL) {
        fprintf(log, "832722\t%.0lf\t%d\n", tempoExecucao, comparacoes);
        fclose(log);
    } else {
        perror("Erro ao criar o arquivo de log");
    }

    return 0;
}