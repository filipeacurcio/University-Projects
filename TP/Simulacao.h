// Trabalho Pratico Programacao - LEI
// DEIS-ISEC 2019-2020
// Filipe Daniel Duarte Acurcio nº 2018017242

#ifndef SIMULACAO_H
#define SIMULACAO_H
#include "Pessoa.h"
#include "sala.h"

//Menu da simulação
int menu();
//Devolve a opção escolhida
int escolhe_menu(ppessoa pessoas, int totalp, plocal locais, int totall);
// chama todas as funcoes necessarias para avancar uma iteracao
void avanca(ppessoa pessoas, plocal locais, int totall, int totalp, int* n_imunes);
// verifica se os dias introduzidos pelo utilizador de um doente é aceitável
int verifica_dur_doenca(ppessoa novo);
//Pede ao utilizador os ids de origem e destino
void pede_ids(int *id_ori, int* id_dest, int totall, plocal l);
// verifica a ligacao de origem e destino, se tem capacidade o local
void verifica_cap(int* quant_p, int* id_dest, int id_ori, plocal l, int totall, int qtd_aux, int* cap_dest);
// calcula a taxa de disseminacao e infeta o total de pessoas calculado nessa taxa
void  disseminacao(ppessoa pessoas,plocal locais,int totall, int totalp);
// calcula a probabilidade de um doente ficar saudavel
void p_recupera(ppessoa pessoas, int* n_imunes);
// probabilidade de uma pessoa ficar um imune à doença
void prob_imune(ppessoa pessoas, int* n_imunes);
// guarda o relatorio final da simulacao num ficheiro de texto
void guarda_rel(ppessoa pessoas, plocal locais, int totall, int max_undo, int n_imunes);
//calcula as taxas e guarda no relatório
void taxa_local_rel(ppessoa pessoas, plocal locais, int totall, FILE* f);
//calcula as taxas e guarda no relatório
void calcula_taxas_final(ppessoa pessoas, int id, FILE *f);
// preenche o novo no da copia
void preenche_copia(ppessoa novo, ppessoa p);
//Verifica se o doente ja atingiu o maximo de dias doente
void maxdiasdoente(ppessoa pessoas, int* n_imunes);
//Devolve a lista de pessoas sabendo o numero de vezes a retroceder
ppessoa pessoa_ant(ppessoa p_prim, ppessoa p_sec, ppessoa p_ter, ppessoa p_quart, int vezes, int n_it);
// Devolve o numero de vezes a retroceder na simulacao
int x_vezes(int* n_it, int* max_undo);
// copia as a lista de pessoas para outra lista para ser possivel ser guardada
ppessoa copia_pessoa(ppessoa pessoas);
// calcula as taxas da populacao total
void calcula_taxa_rel(ppessoa pessoas, FILE* f);

#endif /* SIMULACAO_H */