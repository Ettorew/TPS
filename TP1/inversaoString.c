#include <stdlib.h>
#include <stdio.h>

int tamanhoString(char *str, int cont)
{
    if (*str != '\0')
    {
        cont = tamanhoString(str + 1, cont + 1);
    }
    return cont;
}

void recursao(char *inicio, char *fim)
{
    if (inicio >= fim)
    {
        return;
    }
    char aux = *inicio;
    *inicio = *fim;
    *fim = aux;
    recursao(inicio + 1, fim - 1);
}

void inverteString(char *str)
{
    int tam = tamanhoString(str, 0);
    recursao(str, str + tam - 1);
}

int main()
{
    char str[100];
    scanf("%s", str);
    while (!(str[0] == 'F' && str[1] == 'I' && str[2] == 'M' && str[3] == '\0'))
    {
        inverteString(str);
        printf("%s\n", str);
        scanf("%s", str);
    }
    return 0;
}