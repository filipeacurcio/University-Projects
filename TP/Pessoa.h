// Trabalho Pratico Programacao - LEI
// DEIS-ISEC 2019-2020
// Filipe Daniel Duarte Acurcio nº 2018017242

#ifndef PESSOA_H
#define PESSOA_H
#include "sala.h"
#include "Pessoa.h"
#include "utils.h"

typedef struct individuo pessoa,*ppessoa;

struct individuo{
    char identifcador[30], estado;
    int idade, dias;
    ppessoa prox;
    plocal sala;
};

// mostra todas as pessoas
void mostra_estatistica(ppessoa x, plocal locais , int totall);
// le o ficheiro com a informacao de cada pessoa
// e coloca na lista ligada
pessoa* init_pessoa(int *total);
//verifica se foi lido corretamente cada pessoa
int verifica_pessoa(ppessoa x, int total);
// verifica nomes repetidos
int verifica_nome(ppessoa pessoas);
// verifica nomes repetidos no adiciona doente
int verifica_nome_ad(ppessoa pessoas, char* nome);
// liberta a memoria ocupada pela lista ligada
void liberta_p(ppessoa p);
// associa cada pessoa a um local, eliminando pessoas em que o local nao tenha capacidade
ppessoa associa(ppessoa p, int* totalp, plocal x, int totall);
// adiciona um doente
ppessoa adiciona_doente(ppessoa pessoas,plocal locais, int totall);
// adiciona uma pessoa saudavel
ppessoa adiciona_saudavel(ppessoa pessoas, plocal locais, int totall);
// adiciona uma pessoa imune
ppessoa adiciona_imune(ppessoa pessoas, plocal locais, int totall);
//avança um dia para todas as pessoas doentes
void avanca_dia(ppessoa a);
// transfere pessoas de um local para outro
ppessoa transfere_pessoas(ppessoa pessoas, int totalp, plocal locais, int totall);
// numero de pessoas em cada local
int qtd_pessoas(ppessoa p, int id);
// devolve um no do tipo ppessoa dado uma determinadda posição
ppessoa getPessoa(ppessoa p, int pos);
// altera o id de uma pessoa
void setId(ppessoa p, int id_dest, int id_ori, plocal x, int totall, int totalp);
// devolve a lista na posiçao da pessoa a alterar
ppessoa setPessoa(ppessoa p, int totalp, int id_ori);
//preenche uma pessoa doente
void preenche_D(ppessoa novo, plocal l, int totall, ppessoa p);
void preenche_S_I(ppessoa novo, plocal l, int totall, ppessoa p);
// elimina uma pessoa da lista ligada
// sempre que a capacidade for ultrapassada
void elimina(ppessoa p, int pos);
//Guarda a população num ficheiro de texto
ppessoa guarda_dados_p(ppessoa p);
//Calcula as taxas do estado das pessoas por local
void calcula_taxas(ppessoa pessoas, int id);
//Calcula as taxas do estado da populção
void calcula_taxas_finais(ppessoa pessoas);

#endif /* PESSOA_H */

