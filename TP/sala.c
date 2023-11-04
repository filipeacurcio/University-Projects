// Trabalho Pratico Programacao - LEI
// DEIS-ISEC 2019-2020
// Filipe Daniel Duarte Acurcio nº 2018017242

#include <stdlib.h>
#include <stdio.h>
#include "sala.h"
#include "Pessoa.h"
#define LIGACOES 3

int verifica_espaco(plocal a, int* total) { // retorna 1 se estiver tudo bem, 0 se tiver erros
    int i = 0, j = 0, flag = 0, k = 0;
    // verifica os id's
    for (i = 0; i < *total - 1; i++) {
        for (j = i + 1; j < *total; j++) {
            if (a[i].id == a[j].id || a[i].id <= 0) {
                if (a[i].id == a[j].id) {
                    printf("Existem dois locais com o mesmo id.\n");
                    return 0;
                } else if (a[i].id <= 0) {
                    printf("Existe um local com um id negativo.\n");
                    return 0;
                }
            }
        }
    }
    // verifica ligacoes
    for (i = 0; i < *total; i++) {
        for (j = 0; j < LIGACOES; j++) {
            flag = 0;
            if (a[i].liga[j] != -1) {
                for (k = 0; k < *total; k++) {
                    if (a[k].id == a[i].liga[j]) {
                        flag = 1;
                    }
                }
                if (flag != 1) {
                    printf("Existem dois locais que nao estão ligados entre si.\n");
                    return 0;
                }
            }
        }
    }
}

int verifica_ids(plocal l, int id_or, int id_dest) {
    int i = 0, j = 0, flag = 0;
    while (l[i].id != id_or)
        i++; // pos do id
    for (j = 0; j < LIGACOES; j++) {
        if (id_dest == l[i].liga[j])
            flag = 1;
    }
    if (flag != 1)
        return 0;
    i = 0;
    while (l[i].id != id_dest)
        i++; // pos do id
    for (j = 0; j < LIGACOES; j++) {
        if (id_or == l[i].liga[j])
            flag = 1;
    }
     if (flag != 1)
        return 0;
    return 1;
}

local* init_espaco(int* total) {
    int i = 0, j = 0;
    char fich[30];
    plocal p = NULL, a = NULL;
    local aux;
     printf("Digite o nome do ficheiro que contem o espaco:\n");
    scanf("%s", &fich);
    FILE *f = fopen(fich, "rb");
    if (f == NULL) {
        printf("[ERRO] Impossivel de abrir o ficheiro, %s, de locais, para leitura.\n",fich);
        *total = 0;
        return NULL;
    } else {
        while (fread(&aux, sizeof (local), 1, f) == 1) {
            (*total)++;
            p = realloc(a, sizeof (local) * (*total));
            if (p == NULL) {
                printf("[ERRO] Impossivel realocar memoria para guardar informacao dos locais.\n");
                fclose(f);
                *total = 0;
                return a;
            }
            a = p;
            a[(*total) - 1] = aux;
        }
    }
    if (verifica_espaco(a, total) == 0) {
        a = NULL;
        fclose(f);
        return a;
    }
    fclose(f);
    printf("Ficheiro %s de locais carregado com sucesso!\n", fich);
    return a;
}

int pesquisa_local(plocal x, int id, int totall) {
    int i = 0;
    for (i = 0; i < totall; i++) {
        if (x[i].id == id) // quando encontrado o id é returnado a sua pos no vetor
            return i;
    }
    return -1;
}

void mostra_local(plocal x, int total) {
    int i = 0, j = 0;
    for (i = 0; i < total; i++) {
        printf("ID: %d, Capacidade: %d\n", x[i].id, x[i].capacidade);
        for (j = 0; j < 3; j++) {
            printf("Liga: %d\n", x[i].liga[j]);
        }
    }
}

plocal associa_nova(plocal x, int pos, int totall) {
    x[pos].capacidade--; // diminui a capacidade do local 
    return &x[pos]; // returna o local
}

int getCapacidade(plocal x, int id, int total){
    int aux;
    aux = pesquisa_local(x, id, total);
    return x[aux].capacidade; // retorna a capacidade do local
}