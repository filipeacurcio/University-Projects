#pragma once
#include "Territorio.h"

class Continente: public Territorio{
public:

	Continente(std::string nome, int resistencia, int cria_prod, int cria_ouro, int conta);
	bool isConquistavel(Imperio& impe);
};

