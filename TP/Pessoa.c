// Trabalho Pratico Programacao - LEI
// DEIS-ISEC 2019-2020
// Filipe Daniel Duarte Acurcio nº 2018017242

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "Pessoa.h"
#include "Simulacao.h"

int verifica_pessoa(ppessoa x, int total) {

    if (total != 3 && total != 4) {
        printf("Numero de dados da pessoa errada.\n");
        return 0;
    }

    if (x->estado != 'S' && x->estado != 'I' && x->estado != 'D') {
        printf("Estado da pessoa %s esta formato errado.\n", x->identifcador);
        return 0;
    }

    if (x->estado == 'D' && total != 4) {
        printf("A pessoa %s tem o numero de dias doente errado.\n", x->identifcador);
        return 0;
    }
     if (x->estado == 'S' && total == 4) {
        printf("A pessoa %s tem o numero de dias doente errado.\n", x->identifcador);
        return 0;
    }
    if (x->idade <= 0) {
        printf("Idade da pessoa %s num formato errado.\n", x->identifcador);
        return 0;
    }

    return 1;
}

int verifica_nome(ppessoa pessoas) {
    ppessoa aux = pessoas;
    while (aux != NULL) {
        pessoas = aux->prox;
        while (pessoas != NULL) {
            if (strcmp(pessoas->identifcador, aux->identifcador) == 0) {
                printf("Ja existe uma pessoa com nome %s.\n", pessoas->identifcador);
                return 0;
            }
            pessoas = pessoas->prox;
        }
        aux = aux->prox;
    }
    return 1;
}

int verifica_nome_ad(ppessoa pessoas, char* nome) {
    while (pessoas != NULL) {
        if (strcmp(pessoas->identifcador, nome) == 0) {
            return 0;
        }
        pessoas = pessoas->prox;
    }
    return 1;
}

ppessoa init_pessoa(int *total) {
    int auxi = 0;
    char fich[40];
    ppessoa novo, aux, p = NULL;
    printf("\nDigite o nome do ficheiro que contem as pessoas:\n");
    scanf("%s", &fich);
    FILE* f = fopen(fich, "r");
    if (f == NULL) {
        printf("[ERRO] Impossivel de abrir o ficheiro %s, de pessoas, para leitura.\n", fich);
        fclose(f);
        return p;
    }
    while (!feof(f)) {
        (*total)++;
        novo = malloc(sizeof (pessoa));
        if (novo == NULL) {
            printf("[ERRO] Impossivel realocar memoria para guardar informacao das pessoas.\n");
            fclose(f);
            return p;
        }

        auxi = fscanf(f, "%s %d %c %d", &novo->identifcador, &novo->idade, &novo->estado, &novo->dias);
        novo->prox = NULL;
        if (auxi != -1) {
            if (auxi == 3) {
                novo->dias = 0;
            }
            if (verifica_pessoa(novo, auxi) == 0) {
                liberta_p(p);
                p = NULL;
                fclose(f);
                return p;
            }
            if (p == NULL) {
                p = novo;
            } else {
                aux = p;
                while (aux->prox != NULL)
                    aux = aux->prox;
                aux->prox = novo;
            }
        }
    }
    fclose(f);
    if (verifica_nome(p) == 0) {
        p = NULL;
        return p;
    }
    printf("Ficheiro %s de pessoas carregado com sucesso!\n\n", fich);
    return p;
}

void liberta_p(ppessoa pessoas) {
    ppessoa aux;
    if (pessoas == NULL) {
        while (pessoas != NULL) {
            aux = pessoas;
            pessoas = pessoas->prox;
            free(aux);
        }
    }
}

ppessoa associa(ppessoa p, int* totalp, plocal x, int totall) {
    int id = 0, pos = 0, i = 0, j = 0, auxp = *totalp, posi[30] = {};
    ppessoa aux = p;
    while (p != NULL) {
        extremos(x, &id, totall);
        pos = pesquisa_local(x, id, totall);
        p->sala = &x[pos];
        if (x[pos].capacidade <= 0) {
            posi[i] = j;
            i++;
        } else {
            x[pos].capacidade--;
            p = p->prox;
            j++;
        }
    }
    for (j = 0; j < i; j++) {
        elimina(aux, posi[j]);
        (*totalp)--;
    }
    printf("\n\n");
    return aux;
}

void elimina(ppessoa p, int posicao) {
    // Se a lista ligada estiver vazia
    if (p == NULL)
        return;

    // Guarda o primeiro nó
    ppessoa temp = p;

    // Se o primeiro nó tiver de ser eliminado
    if (posicao == 0) {
        p = temp->prox; // Muda o primeiro nó
        free(temp); // Liberta antigo primeiro nó
    }

    // Encontrar o nó anterior do nó a ser eliminado 
    for (int i = 0; temp != NULL && i < posicao - 1; i++)
        temp = temp->prox;

    // Se a posição for superior ao número de nós 
    if (temp == NULL || temp->prox == NULL)
        return;

    // Nó temp->prox é o nó a ser eliminado 
    // Guarda o ponteiro para o proximo nó para ser eliminado
    ppessoa next = temp->prox->prox;
    printf("%s nao entra na simulacao pois foi excedida a capacidade num local.\n", temp->prox->identifcador);

    // Desvincular o nó da lista ligada 
    free(temp->prox); // Liberta memoria

    temp->prox = next; // Desvincular o nó eliminado da lista 

}

void calcula_taxas(ppessoa pessoas, int id) {
    float i = 0.0;
    float taxa_i = 0, taxa_d = 0, taxa_s = 0, totalp = 0;
    while (pessoas != NULL) {
        if (id == pessoas->sala->id) {
            totalp++;
            if (pessoas->estado == 'I')
                taxa_i++;
            else if (pessoas->estado == 'D')
                taxa_d++;
            else if (pessoas->estado == 'S')
                taxa_s++;
        }
        pessoas = pessoas->prox;
    }
    if (totalp == 0)
        printf("Local sem populacao.\n");
    else if (totalp != 0)
        printf("\nTaxa de saudaveis : %.2f\nTaxa de infetados: %.2f\nTaxa de imunes: %.2f\n", taxa_s / totalp, taxa_d / totalp, taxa_i / totalp);
}

void calcula_taxas_finais(ppessoa pessoas) {
    int i = 0;
    float taxa_i = 0, taxa_d = 0, taxa_s = 0, totalp = 0;
    while (pessoas != NULL) {
        totalp++;
        if (pessoas->estado == 'I')
            taxa_i++;
        else if (pessoas->estado == 'D')
            ++taxa_d;
        else if (pessoas->estado == 'S')
            taxa_s++;
        pessoas = pessoas->prox;
    }
    printf("Taxas relativas a populacao:\n");
    printf("Taxa de saudaveis : %.2f\nTaxa de infetados: %.2f\nTaxa de imunes: %.2f\n", taxa_s / totalp, taxa_d / totalp, taxa_i / totalp);
}

void mostra_estatistica(ppessoa pessoas, plocal locais, int totall) {
    int i = 0;
    ppessoa aux = pessoas;
    for (i = 0; i < totall; i++) {
        pessoas = aux;
        printf("ID: %d\n", locais[i].id);
        while (pessoas != NULL) {
            if (locais[i].id == pessoas->sala->id) {
                printf("Nome: %s\t\tEstado: %c\tDias: %d\n", pessoas->identifcador, pessoas->estado, pessoas->dias);
            }
            pessoas = pessoas->prox;
        }
        pessoas = aux;
        calcula_taxas(pessoas, locais[i].id);
    }
    printf("\n");
    calcula_taxas_finais(pessoas);
}

void preenche_D(ppessoa novo, plocal l, int totall, ppessoa p) {
    char nome[30];
    int aux_id = 0, pos;
    ppessoa aux = p;
    printf("Digite o id do local: \n");
    do {
        scanf("%d", &aux_id);
        pos = pesquisa_local(l, aux_id, totall);
        if (pesquisa_local(l, aux_id, totall) == -1) {
            printf("[ERRO] Digite um id valido.\n");
            continue;
        }
        if (l[pos].capacidade <= 0)
            printf("[ERRO] Local sem capacidade para mais pessoas. \nDigite outro id.\n");
    } while (pesquisa_local(l, aux_id, totall) == -1 || l[pos].capacidade <= 0);
    novo->sala = associa_nova(l, pos, totall);
    printf("Digite o identificador: \n");
    do {
        scanf("%s", &novo->identifcador);
        if (verifica_nome_ad(aux, novo->identifcador) == 0)
            printf("[ERRO] Ja existe uma pessoa com esse identificador.\nDigite um nome diferente.\n");
    } while (verifica_nome_ad(aux, novo->identifcador) == 0);
    printf("Digite a idade: \n");
    do {
        scanf("%d", &novo->idade);
        if (novo->idade <= 0)
            printf("[ERRO] Digite uma idade valida.\n");
    } while (novo->idade <= 0);

    printf("Digite o numero de dias em que esta doente: \n");
    do {
        scanf("%d", &novo->dias);
        if (novo->dias <= 0)
            printf("[ERRO] Digite um numero de dias valido.\n");
        if(verifica_dur_doenca(novo) == 0)
            printf("[ERRO] Dias do paciente excede a duracao possivel da infecao.\n");
    } while (novo->dias <= 0 || verifica_dur_doenca(novo) == 0);
    novo->prox = NULL;
}
void preenche_S_I(ppessoa novo, plocal l, int totall, ppessoa p) {
    char nome[30];
    int aux_id = 0, pos;
    ppessoa aux = p;
    printf("Digite o id do local: \n");
    do {
        scanf("%d", &aux_id);
        pos = pesquisa_local(l, aux_id, totall);
        if (pesquisa_local(l, aux_id, totall) == -1) {
            printf("[ERRO] Digite um id valido.\n");
            continue;
        }
        if (l[pos].capacidade <= 0)
            printf("[ERRO] Local sem capacidade para mais pessoas. \nDigite outro id.\n");
    } while (pesquisa_local(l, aux_id, totall) == -1 || l[pos].capacidade <= 0);
    novo->sala = associa_nova(l, pos, totall);
    printf("Digite o identificador: \n");
    do {
        scanf("%s", &novo->identifcador);
        if (verifica_nome_ad(aux, novo->identifcador) == 0)
            printf("[ERRO] Ja existe uma pessoa com esse nome.\nDigite um nome diferente.\n");
    } while (verifica_nome_ad(aux, novo->identifcador) == 0);
    printf("Digite a idade: \n");
    do {
        scanf("%d", &novo->idade);
        if (novo->idade <= 0)
            printf("[ERRO] Digite uma idade valida.\n");
    } while (novo->idade <= 0);
    novo->prox = NULL;
}

ppessoa adiciona_doente(ppessoa pessoas, plocal locais, int totall) {
    ppessoa novo, aux;
    novo = malloc(sizeof (pessoa));
    if (novo == NULL) {
        printf("[ERRO] na alocacao de memoria para guardar um novo doente.\n");
        return pessoas;
    }
    novo->estado = 'D';
    preenche_D(novo, locais, totall, pessoas);
    // Fazer o próximo de um novo nó como cabeça
    novo->prox = pessoas;
    //mover a cabeça para apontar para o novo nó
    pessoas = novo;

    return pessoas;
}

ppessoa adiciona_saudavel(ppessoa pessoas, plocal locais, int totall) {
    ppessoa novo, aux;
    novo = malloc(sizeof (pessoa));
    if (novo == NULL) {
        printf("[ERRO] na alocacao de memoria para guardar um novo doente.\n");
        return pessoas;
    }
    novo->estado = 'S';
        novo->dias = 0;
    preenche_S_I(novo, locais, totall, pessoas);
    // Fazer o próximo de um novo nó como cabeça
    novo->prox = pessoas;
    //mover a cabeça para apontar para o novo nó
    pessoas = novo;

    return pessoas;
}
ppessoa adiciona_imune(ppessoa pessoas, plocal locais, int totall) {
    ppessoa novo, aux;
    novo = malloc(sizeof (pessoa));
    if (novo == NULL) {
        printf("[ERRO] na alocacao de memoria para guardar um novo doente.\n");
        return pessoas;
    }
    novo->estado = 'I';
        novo->dias = 0;
    preenche_S_I(novo, locais, totall, pessoas);
    // Fazer o próximo de um novo nó como cabeça
    novo->prox = pessoas;
    //mover a cabeça para apontar para o novo nó
    pessoas = novo;

    return pessoas;
}

void avanca_dia(ppessoa a) {
    while (a != NULL) {
        if (a->estado == 'D')
            a->dias++;
        a = a->prox;
    }
}

// numero de pessoas em cada local

int qtd_pessoas(ppessoa p, int id) {
    int aux = 0;
    while (p != NULL) {
        if (p->sala->id == id)
            aux++;
        p = p->prox;
    }
    return aux;
}

void setId(ppessoa p, int id_dest, int id_ori, plocal x, int totall, int totalp) {
    int i = 0, pos = 0, j = 0;
    ppessoa aux = p;
    id_dest = pesquisa_local(x, id_dest, totall);
    p->sala = &x[id_dest];
    x[id_dest].capacidade--;
    id_ori = pesquisa_local(x, id_ori, totall);
    x[id_ori].capacidade++;
}

ppessoa getPessoa(ppessoa p, int pos) {
    int i = 0;
    for (i = 0; i < pos; i++)
        p = p->prox;
    return p;
}

ppessoa setPessoa(ppessoa p, int totalp, int id_ori) {
    int pos = 0, i = 0, n = 0;
    ppessoa res = p;
    do {
        p = res;
        n = 0;
        pos = intUniformRnd(0, (totalp - 1));
        for (n = 0; n != pos; n++) {
            p = p->prox;
        }
    } while (p->sala->id != id_ori);

    return p;
}

ppessoa transfere_pessoas(ppessoa pessoas, int totalp, plocal l, int totall) {
    int id_ori, id_dest, quant_p, qtd_aux, cap_dest, i, pos, j;
    ppessoa aux = pessoas;
    do {
        pede_ids(&id_ori, &id_dest, totall, l);
        if (verifica_ids(l, id_ori, id_dest) == 0)
            printf("[ERRO] Ids sem ligacao.\nDigite ids com uma ligacao entre si.\n");
    } while (verifica_ids(l, id_ori, id_dest) == 0);
    qtd_aux = qtd_pessoas(pessoas, id_ori);
    cap_dest = getCapacidade(l, id_dest, totall);
    printf("Digite o numero de pessoas a mover: [0,%d]\n", qtd_aux);
    do {
        scanf("%d", &quant_p);
        if (quant_p > qtd_aux || quant_p < 0) {
            printf("[ERRO] Digite um valor dentro dos limites.\n");
            continue;
        }
        if (cap_dest - quant_p < 0) {
            verifica_cap(&quant_p, &id_dest, id_ori, l, totall, qtd_aux, &cap_dest);
        }
    } while (quant_p < 0 || quant_p > qtd_aux);
    //fazer a transferencia
    for (i = 0; i < quant_p; i++) {
        pessoas = aux;
        pessoas = setPessoa(pessoas, totalp, id_ori);
        setId(pessoas, id_dest, id_ori, l, totall, totalp);
    }
}

ppessoa guarda_dados_p(ppessoa p) {
    char fich[40];
     printf("Digite o nome do ficheiro que pretende guardar as pessoas:\n");
    scanf("%s", &fich);
    FILE* f = fopen(fich, "w");
    if (f == NULL) {
        printf("[ERRO] Impossivel de abrir o ficheiro %s para escrita.\n", fich);
        fclose(f);
        return p;
    }
    while (p != NULL) {
        if (p->dias == 0) {
            fprintf(f, "%s %d %c\n", p->identifcador, p->idade, p->estado);
        } else if (p->dias != 0) {
            fprintf(f, "%s %d %c %d\n", p->identifcador, p->idade, p->estado, p->dias);
        }
        p = p->prox;
    }
    fclose(f);
}
