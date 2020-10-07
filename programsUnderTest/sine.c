#include <stdio.h>
#include <math.h>
#include <stdlib.h>


int main(int argc, char** argv)
{
    double x;
    char *eptr;
    double result;


    x = strtod(argv[1], &eptr);
    printf("Input: %s\n", argv[1]);
    result = sin(x);
    printf("%f", result);
    return 0;
}
