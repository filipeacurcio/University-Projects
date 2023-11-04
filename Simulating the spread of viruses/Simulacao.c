// Trabalho Pratico Programacao - LEI
// DEIS-ISEC 2019-2020
// Filipe Daniel Duarte Acurcio nº 2018017242

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "Simulacao.h"
#define TAXA_DISSEMINACAO 5
#define MAX_DUR 5
#define TAXA_IMUNE 20
#define P_REC 1.0

int menu() {
    int i;

    puts("\nMenu Principal: \n");
    printf("1 - Avancar 1 iteracao na simulacao\t");
    printf("\t2 - Apresentar estatistica\n\n");
    printf("3 - Adicionar doente\t");
    printf("\t\t\t4 - Adicionar pessoa saudavel\n\n");
    printf("5 - Adicionar pessoa imune\t");
    printf("\t\t6 - Transferir pessoas\n\n");
    printf("7 - Mostrar locais depois da associacao\t");
    printf("\t8 - Voltar atras X iteracoes\n\n");
    printf("9 - Terminar Simulacao\n\n");
    printf("Escolha uma opcao: ");

    do {
        scanf(" %d", &i);
        if (i < 0 || i > 9)
            puts("Digite um numero valido.");
    } while (i < 0 || i > 9);

    return i;
}

int verifica_dur_doenca(ppessoa novo) {
    int t = 0;
    t = (novo->idade / 10) % 10;
    if ((novo->dias) > (t + MAX_DUR))
        return 0;
    else
        return 1;



}

void disseminacao(ppessoa pessoas, plocal locais, int totall, int totalp) {
    ppessoa aux = pessoas;
    int i = 0, taxa_diss, j = 0;
    for (i = 0; i < totall; i++) {
        pessoas = aux;
        taxa_diss = (TAXA_DISSEMINACAO * qtd_pessoas(pessoas, locais[i].id)) / 100;
        //printf("TAXA %d NO ID  %d\n", taxa_diss, locais[i].id);
        for (j = 0; j < taxa_diss; j++) {
            pessoas = aux;
            pessoas = setPessoa(pessoas, totalp, locais[i].id);
            //printf("NOME: %s NO ID: %d\n", pessoas->identifcador, locais[i].id);
            if (pessoas->estado != 'I') {
                pessoas->estado = 'D';
            }
        }
    }
}

void prob_imune(ppessoa pessoas, int* n_imunes) {
    int aux;
    float taxa = (TAXA_IMUNE / 100.0);
    aux = probEvento(taxa);
    if (aux == 1) {
        printf("%s ficou imune a doenca.\n", pessoas->identifcador);
        pessoas->estado = 'I';
        (*n_imunes)++;
    }
}

void p_recupera(ppessoa pessoas, int* n_imunes) {
    int aux;
    float prob;
    while (pessoas != NULL) {       
        prob = ((float)P_REC / (float)pessoas->idade);
        aux = probEvento(prob);
        //printf("NOME: %s, prob : %.4f acontece? %d\n", pessoas->identifcador, prob, aux);
        if (pessoas->estado != 'I' && aux == 1) {
            pessoas->estado = 'S';
            pessoas->dias = 0;
            prob_imune(pessoas, n_imunes);
        }
        pessoas = pessoas->prox;
    }
}

void maxdiasdoente(ppessoa pessoas, int* n_imunes) {
    int t;
    while (pessoas != NULL) {
        t = (pessoas->idade / 10) % 10;
        t += MAX_DUR;
        if (t == pessoas->dias && pessoas->estado != 'I') {
            pessoas->estado = 'S';
            pessoas->dias = 0;
            prob_imune(pessoas, n_imunes);
        }
        pessoas = pessoas->prox;
    }
}

void avanca(ppessoa pessoas, plocal locais, int totall, int totalp, int* n_imunes) {
    disseminacao(pessoas, locais, totall, totalp);
    p_recupera(pessoas, n_imunes);
    maxdiasdoente(pessoas, n_imunes);
    avanca_dia(pessoas);
}

void verifica_cap(int* quant_p, int* id_dest, int id_ori, plocal l, int totall, int qtd_aux, int* cap_dest) {
    int opcao;
    do {
        printf("[ERRO] Impossivel de transferir.\nO local com id %d nao tem espaco suficiente para receber %d pessoas.\n", *id_dest, *quant_p);
        do {
            printf("Pressione 1 para mudar numero de pessoas a mover.\nPressione 2 para mudar o local destino.\n");
            scanf("%d", &opcao);
            if (opcao != 1 && opcao != 2)
                printf("[ERRO] Digite um numero valido. \n");
        } while (opcao != 1 && opcao != 2);
        if (opcao == 1) {
            printf("Digite o numero de pessoas a mover: [0,%d]\n", qtd_aux);
            scanf("%d", quant_p);
        } else if (opcao == 2) {
            printf("Digite o id de destino: \n");
            do {
                scanf("%d", id_dest);
                if (pesquisa_local(l, *id_dest, totall) == -1) {
                    printf("[ERRO] Digite um id valido.\n");
                    continue;
                }
                if (verifica_ids(l, id_ori, *id_dest) == 0) {
                    do {
                        printf("[ERRO]Locais sem ligação. Digite um id com ligacao ao id de origem.\n");
                        printf("Digite o id de destino: \n");
                        scanf("%d", id_dest);
                        if (pesquisa_local(l, *id_dest, totall) == -1) {
                            printf("[ERRO] Digite um id valido.\n");
                        }
                    } while (verifica_ids(l, id_ori, *id_dest) == 0);
                }
                do {
                    printf("Digite o numero de pessoas a mover: [0,%d]\n", qtd_aux);
                    scanf("%d", quant_p);
                    if (*quant_p > qtd_aux && *quant_p < 0)
                        printf("[ERRO] Digite um numero dentro dos limites\n");
                } while (*quant_p > qtd_aux || *quant_p < 0);
            } while (pesquisa_local(l, *id_dest, totall) == -1);
        }
        (*cap_dest) = getCapacidade(l, *id_dest, totall);
    } while (*(cap_dest) - (*quant_p) < 0);
}

void pede_ids(int* id_ori, int* id_dest, int totall, plocal l) {
    printf("Digite o id de origem: \n");
    do {
        scanf("%d", id_ori);
        if (pesquisa_local(l, *id_ori, totall) == -1)
            printf("[ERRO] Digite um id valido.\n");
    } while (pesquisa_local(l, *id_ori, totall) == -1);
    printf("Digite o id de destino: \n");
    do {
        scanf("%d", id_dest);
        if (pesquisa_local(l, *id_dest, totall) == -1)
            printf("[ERRO] Digite um id valido.\n");
    } while (pesquisa_local(l, *id_dest, totall) == -1);
}

void calcula_taxa_rel(ppessoa pessoas, FILE* f) {
    int i = 0;
    float taxa_i = 0, taxa_d = 0, taxa_s = 0, totalp = 0;
    while (pessoas != NULL) {
        totalp++;
        if (pessoas->estado == 'I')
            taxa_i++;
        else if (pessoas->estado == 'D')
            taxa_d++;
        else if (pessoas->estado == 'S')
            taxa_s++;
        pessoas = pessoas->prox;
    }
    fprintf(f, "Taxas relativas a populacao final:\n");
    fprintf(f, "Taxa de saudaveis : %.2f\nTaxa de infetados: %.2f\nTaxa de imunes: %.2f\n", taxa_s / totalp, taxa_d / totalp, taxa_i / totalp);

}

void calcula_taxas_final(ppessoa pessoas, int id, FILE *f) {
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
        fprintf(f, "Local sem populacao.\nTaxa de saudaveis : %.2f\nTaxa de infetados: %.2f\nTaxa de imunes: %.2f\n", i, i, i);
    else if (totalp != 0)
        fprintf(f, "\nTaxa de saudaveis : %.2f\nTaxa de infetados: %.2f\nTaxa de imunes: %.2f\n", taxa_s / totalp, taxa_d / totalp, taxa_i / totalp);
}

void taxa_local_rel(ppessoa pessoas, plocal locais, int totall, FILE* f) {
    int i = 0;
    ppessoa aux = pessoas;
    for (i = 0; i < totall; i++) {
        pessoas = aux;
        if (i == 0)
            fprintf(f, "ID: %d\n", locais[i].id);
        else
            fprintf(f, "\nID: %d\n", locais[i].id);
        while (pessoas != NULL) {
            if (locais[i].id == pessoas->sala->id) {
                fprintf(f, "Nome: %s\tEstado: %c\tDias: %d\n", pessoas->identifcador, pessoas->estado, pessoas->dias);
            }
            pessoas = pessoas->prox;
        }
        pessoas = aux;
        calcula_taxas_final(pessoas, locais[i].id, f);
    }
}

void guarda_rel(ppessoa pessoas, plocal locais, int totall, int max_undo, int n_imunes) {
    char fich[30];
    FILE *f = fopen("report.txt", "w");
    if (f == NULL) {
        printf("[ERRO] Impossivel de abrir o ficheiro ""report.txt"" para leitura.\n");
        return;

    } else {
        fprintf(f, "Valores considerados para esta simulacao:\n");
        fprintf(f, "Taxa de disseminacao: %d%\nProbalidade de recuperacao: %.2f/idade\nDuracao maxima da infecao: %d + 1 dia por cada dezena de anos de vida\nTaxa de imunidade: %d%\n",TAXA_DISSEMINACAO,P_REC, MAX_DUR, TAXA_IMUNE);
        fprintf(f,"\nQuantidade de pessoas que ficaram imunes durante a simulação: %d\n",n_imunes);
        fprintf(f,"\nForam usados %d undos.\n\n", 3 - max_undo);
        taxa_local_rel(pessoas, locais, totall, f);
        fprintf(f, "\n\n");
        calcula_taxa_rel(pessoas, f);

    }
}

void preenche_copia(ppessoa novo, ppessoa p) {
    novo->dias = p->dias;
    novo->idade = p->idade;
    novo->estado = p->estado;
    strcpy(novo->identifcador, p->identifcador);
    novo->sala = p->sala;
    novo->prox = NULL;
}

int x_vezes(int* n_it, int* max_undo) {
    int vezes = 0;
    if ((*n_it) == 0) {
        printf("[ERRO] Ainda nao avancou nenhuma iteracao. Impossivel retroceder na simulacao.\n");
        return -1;
    }
    printf("Digite quantas iteracoes quer voltar atras: 1, 2 ou 3.\n");
    do {
        scanf("%d", &vezes);
        if (vezes < 0 || vezes > 3) {
            printf("[ERRO] Digite um valor dentro dos limites.\n");
        } else if ((*max_undo) <= 0) {
            printf("[ERRO] Esgotou as vezes para retroceder\n.");
            return -1;
        } else if ((*max_undo) - vezes < 0) {
            printf("[ERRO] Apenas tem %d undos por fazer.\n", *(max_undo));
        } else if ((*n_it) - vezes < 0) {
            printf("[ERRO] Impossivel andar %d iteracoes para tras.\n", vezes);
            return -1;
        } else {
            (*max_undo) -= vezes;
            (*n_it) -= vezes;
        }

    } while (vezes < 0 || vezes > 3 && (*max_undo) - vezes < 0);
    return vezes;
}

ppessoa pessoa_ant(ppessoa p_prim, ppessoa p_sec, ppessoa p_ter, ppessoa p_quart, int vezes, int n_it) {
    n_it += vezes;
    while (n_it != 0 && n_it != 1 && n_it != 2 && n_it != 3) {
        n_it -= 4;
    }
    if (n_it == 3) {
        if (vezes == 1)
            return p_ter;
        if (vezes == 2)
            return p_sec;
        if (vezes == 3)
            return p_prim;
    } else if (n_it == 2) {
        if (vezes == 3)
            return p_quart;
        if (vezes == 1)
            return p_sec;
        if (vezes == 2)
            return p_prim;
    } else if (n_it == 1) {
        if (vezes == 1) {
            return p_prim;
        }
        if (vezes == 2)
            return p_quart;
        if (vezes == 3)
            return p_ter;
    } else if (n_it == 0) {
        if (vezes == 1)
            return p_quart;
        if (vezes == 2)
            return p_ter;
        if (vezes == 3)
            return p_sec;
    }
}

ppessoa copia_pessoa(ppessoa pessoas) {
    ppessoa atual = pessoas; //para percorrer a lista original
    ppessoa novaLista = NULL; // primeiro no da nova lista 
    ppessoa ult_no = NULL; // ponteiro para o ultimo no da nova lista

    while (atual != NULL) {
        // caso especial para o primeiro no novo
        if (novaLista == NULL) {
            novaLista = malloc(sizeof (pessoa));
            if (novaLista == NULL) {
                printf("[ERRO] Impossivel alocar memoria para a nova lista.\n");
                return NULL;
            }
            preenche_copia(novaLista, atual);
            novaLista->prox = NULL;
            ult_no = novaLista;
        } else {
            ult_no->prox = malloc(sizeof (pessoa));
            if (ult_no->prox == NULL) {
                printf("[ERRO] Impossivel alocar memoria para o ultimo nó.\n");
                return NULL;
            }
            ult_no = ult_no->prox;
            preenche_copia(ult_no, atual);
            ult_no->prox = NULL;
        }
        atual = atual->prox;
    }

    return novaLista;
}

int escolhe_menu(ppessoa pessoas, int totalp, plocal locais, int totall) {
    int i = 0, j = 0, n_it = 0, vezes = 0, max_undo = 3, n_imunes = 0;
    ppessoa p_prim = NULL, p_sec = NULL, p_ter = NULL, p_quart = NULL;
    do {
        i = menu();
        switch (i) {
            case 1:
                n_it++;
                if (j == 0)
                    p_prim = copia_pessoa(pessoas);
                if (j == 1)
                    p_sec = copia_pessoa(pessoas);
                if (j == 2)
                    p_ter = copia_pessoa(pessoas);
                if (j == 3)
                    p_quart = copia_pessoa(pessoas);
                j++;
                if (j > 3)
                    j = 0;
                avanca(pessoas, locais, totall, totalp, &n_imunes);
                break;
            case 2:
                mostra_estatistica(pessoas, locais, totall);
                break;
            case 3:
                pessoas = adiciona_doente(pessoas, locais, totall);
                break;
            case 4:
                pessoas = adiciona_saudavel(pessoas, locais, totall);
                break;
            case 5:
                pessoas = adiciona_imune(pessoas, locais, totall);
                break;
            case 6:
                transfere_pessoas(pessoas, totalp, locais, totall);
                break;
            case 7:
                mostra_local(locais, totall);
                break;
            case 8:
                vezes = x_vezes(&n_it, &max_undo);
                if (vezes != -1) {
                    pessoas = pessoa_ant(p_prim, p_sec, p_ter, p_quart, vezes, n_it);
                    j -= vezes;
                }
                break;
            case 9:
                guarda_dados_p(pessoas);
                guarda_rel(pessoas, locais, totall, max_undo, n_imunes);
                free(locais);
                liberta_p(pessoas);
                break;
        }
    } while (i != 9);

    liberta_p(p_prim);
    liberta_p(p_sec);
    liberta_p(p_ter);
    liberta_p(p_quart);
    return 0;
}