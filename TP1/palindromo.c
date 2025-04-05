#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int ePalindromo(char *str)
{
    int count = 0;
    int resp = 1;
    while (str[count] != '\0')
    {
        count++;
    }
    for (int i = 0; i < count / 2; i++)
    {
        if (str[i] != str[count - i - 1])
        {
            resp = 0;
            i = count;
        }
    }
    return resp;
}

int main()
{
    char str[100];
    fgets(str, sizeof(str), stdin);
    str[strcspn(str, "\n")] = '\0';
    while (str[0] != 'F' || str[1] != 'I' || str[2] != 'M' || str[3] != '\0')
    {
        int palindromo = ePalindromo(str);
        printf(palindromo ? "SIM\n" : "NAO\n");
        fgets(str, sizeof(str), stdin);
        str[strcspn(str, "\n")] = '\0';
    }
    return 0;
}