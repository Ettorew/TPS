#include <stdio.h>
#include <stdlib.h>

int main()
{
    FILE *arquivo;
    int n, i;
    double valor;
    long posicao;

    // Lê o número de valores a serem inseridos
    scanf("%d", &n);

    // Abre o arquivo para escrita em modo binário
    arquivo = fopen("numbers.bin", "wb");
    if (arquivo == NULL)
    {
        printf("Erro ao abrir o arquivo para escrita.\n");
        return 1;
    }

    // Lê os valores e escreve no arquivo
    for (i = 0; i < n; i++)
    {
        scanf("%lf", &valor);
        fwrite(&valor, sizeof(double), 1, arquivo);
    }

    // Fecha o arquivo
    fclose(arquivo);

    // Reabre o arquivo para leitura em modo binário
    arquivo = fopen("numbers.bin", "rb");
    if (arquivo == NULL)
    {
        printf("Erro ao abrir o arquivo para leitura.\n");
        return 1;
    }

    // Determina o tamanho do arquivo
    fseek(arquivo, 0, SEEK_END);
    posicao = ftell(arquivo);

    // Lê os valores de trás para frente
    for (i = 0; i < n; i++)
    {
        posicao -= sizeof(double);
        fseek(arquivo, posicao, SEEK_SET);
        fread(&valor, sizeof(double), 1, arquivo);
        printf("%g\n", valor);
    }

    // Fecha o arquivo
    fclose(arquivo);

    return 0;
}