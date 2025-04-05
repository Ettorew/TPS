#include <stdlib.h>
#include <stdio.h>

int recursao(int digitos, int soma)
{
    if (digitos > 0)
    {
        soma = recursao(digitos / 10, soma + (digitos % 10));
    }
    return soma;
}

int somaDig(int digitos)
{
    int soma = recursao(digitos, 0);
    return soma;
}

int main()
{
    int num;
    int soma;
    char entrada[100];
    scanf("%s", entrada);
    while (!(entrada[0] == 'F' && entrada[1] == 'I' && entrada[2] == 'M' && entrada[3] == '\0'))
    {
        num = atoi(entrada);
        soma = somaDig(num);
        printf("%d\n", soma);
        scanf("%s", &entrada);
    }
    return 0;
}