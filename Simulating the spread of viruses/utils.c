// Trabalho Pratico Programacao - LEI
// DEIS-ISEC 2019-2020
// Filipe Daniel Duarte Acurcio nº 2018017242

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "sala.h"
#include "Pessoa.h"
#include "utils.h"


void initRandom(){
    srand(time(NULL));
}

int intUniformRnd(int a, int b){
    return a + rand()%(b-a+1);
}

int probEvento(float prob){
    return prob > ((float)rand()/RAND_MAX);
}

void extremos(plocal x,int *id, int total){
    int i = 0, maior = 0, menor = 0;
    maior = x[0].id;
   menor = x[0].id;
    for(i = 1 ; i < total; i++){
        if(x[i].id > maior)
             maior = x[i].id;
        else if(x[i].id <  menor)
            menor = x[i].id;       
    }  
   //maior = maior id ----- menor = menor id existente
   // verificar se existe o id sendo ele aleatório
    verifica_extremos(x, maior, menor, total,id);
}

void verifica_extremos(plocal x, int maior, int menor, int total, int * id){
    int i = 0, flag = 0, aux = 0;
    
    do{
        aux = intUniformRnd(menor, maior); // aux =  valor aleatório entre o menor e maior id
        for(i = 0; i < total; i++){
            if(aux == x[i].id){
                flag = 1;
                *id = aux;
            }
        }      
    }while(flag == 0); // apenas acaba o ciclo quando encontrar um valor aletório igual a um id existente
    
}

// Função main () com alguns exemplos simples de utilizacao das funcoes
//int main(){

 // int i;

  //  initRandom();   // esta função só deve ser chamada uma vez

 //   printf("10 valores aleatorios uniformes entre [4, 10]:\n");
 //   for(i=0; i<10; i++)
 ///       printf("%d\n", intUniformRnd(4, 100));

  //  printf(" Probabilidade 0.25 de um evento suceder: \n");
  //  for(i=0; i<10; i++)
    //    printf("%d\n", probEvento(0.25));
//
 //   return 0;
//}
