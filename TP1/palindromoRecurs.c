#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int tamPalavra(char *str, int tam)
{
    int resultado = tam;
    if (*str != '\0')
    {
        resultado = tamPalavra(str + 1, tam + 1);
    }
    return resultado;
}

int recursao(char *inicio, char *final, int count)
{
    int pal = 1;

    if (count > 0)
    {
        if (*inicio == *final)
        {
            pal = recursao(inicio + 1, final - 1, count - 1);
        }
        else
        {
            pal = 0;
        }
    }

    return pal;
}

int ePalindromo(char *str)
{
    int tam = tamPalavra(str, 0);
    return recursao(str, str + tam - 1, tam / 2);
}

int main()
{
    char str[100];
    fgets(str, sizeof(str), stdin);
    str[strcspn(str, "\n")] = '\0';

    while (strcmp(str, "FIM") != 0)
    {
        int palindromo = ePalindromo(str);
        printf(palindromo ? "SIM\n" : "NAO\n");
        fgets(str, sizeof(str), stdin);
        str[strcspn(str, "\n")] = '\0';
    }

    return 0;
}