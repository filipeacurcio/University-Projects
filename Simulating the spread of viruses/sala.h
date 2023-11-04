// Trabalho Pratico Programacao - LEI
// DEIS-ISEC 2019-2020
// Filipe Daniel Duarte Acurcio nº 2018017242

#ifndef SALA_H
#define SALA_H

typedef struct sala local, *plocal;
struct sala{
 int id; // id numérico do local
 int capacidade; // capacidade máxima
 int liga[3]; // id das ligações (-1 nos casos não usados)
};

// le o ficheiro com a informacao de cada local
// e coloca num vetor dinamico
local* init_espaco(int* total);
//faz a verificaçao do espaço
int verifica_espaco(plocal a, int* total);
// mostra toda a informação dos locais depois da associacao
void mostra_local(plocal x, int total);
//Devolve a posição correspondente a um local
int pesquisa_local(plocal x, int id,int totall);
//devolve a capacidade de um local
int getCapacidade(plocal x, int id, int total);
// associa o novo doente
plocal associa_nova(plocal x, int id, int totall);
//verifica se os ids tem ligação
int verifica_ids(plocal l, int id_or, int id_dest);

#endif /* SALA_H */

