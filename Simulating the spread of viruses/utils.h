// Trabalho Pratico Programacao - LEI
// DEIS-ISEC 2019-2020
// Filipe Daniel Duarte Acurcio nº 2018017242

#ifndef UTILS_H
#define UTILS_H


// Inicializa o gerador de numeros aleatorios.
// Esta funcao deve ser chamada apenas uma vez no inicio da execucao do programa
void initRandom();

//Devolve um valor inteiro aleatorio distribuido uniformemente entre [a, b]
int intUniformRnd(int a, int b);

// Devolve o valor 1 com probabilidade prob. Caso contrario, devolve 0
int probEvento(float prob);

// calcula o maior e menor id dos locais
void extremos(plocal x,int *id, int total);

// altera a variavel "id" para um valor aleatorio entre o menor e maior id(com verificação se existe esse id)
void verifica_extremos(plocal x, int maior, int menor, int total, int* id);

#endif /* UTILS_H */
