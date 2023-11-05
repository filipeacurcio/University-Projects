#include "Ilhas.h"
#include "Imperio.h"


Ilhas::Ilhas(std::string nome, int resistencia, int cria_prod, int cria_ouro, int conta):Territorio(nome, resistencia, cria_ouro, cria_ouro, conta, 2){
}

bool Ilhas::isConquistavel(Imperio& impe){
	return false;
}

