#include "Continente.h"
using namespace std;

Continente::Continente(std::string nome, int resistencia, int cria_prod, int cria_ouro, int conta):Territorio(nome, resistencia, cria_prod, cria_ouro, conta, 1){
}

bool Continente::isConquistavel(Imperio& impe){
	return true;
}
