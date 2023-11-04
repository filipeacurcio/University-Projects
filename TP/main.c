// Trabalho Pratico Programacao - LEI
// DEIS-ISEC 2019-2020
// Filipe Daniel Duarte Acurcio nº 2018017242

#include <stdio.h>
#include <stdlib.h>
#include "sala.h"
#include "utils.h"
#include "Pessoa.h"
#include "Simulacao.h"

int main(int argc, char** argv) {
    // totalp -> numero de pessoas; totall -> total de locals
    int totalp = 0, totall = 0;
    ppessoa pessoas = NULL;
    plocal locais = NULL;
    initRandom();
    // le os dados de espaço
    locais = init_espaco(&totall);
    // Caso algo correr mal liberta o espaço ocupado pelo vetor dinamico e acaba o programa
    if (locais == NULL) {
        free(locais);
        return 2;
    }
    // le os dados das pessoas
    pessoas = init_pessoa(&totalp);
    // Caso algo correr mal liberta o espaço ocupado pelo vetor dinamico, da lista e acaba o programa 
    if (pessoas == NULL) {
        free(locais);
        liberta_p(pessoas);
        return 3;
    }
    // Associa cada pessoa com um local aleatoriamente
    pessoas = associa(pessoas, &totalp, locais, totall);
    
    // Simulação
    escolhe_menu(pessoas, totalp, locais, totall);
    
    return 1;
}

