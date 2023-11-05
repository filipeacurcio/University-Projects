#pragma once
#include "Territorio.h"

class Ilhas: public Territorio{

public:
	Ilhas(std::string nome, int resistencia, int cria_prod, int cria_ouro, int conta);
	virtual bool isConquistavel(Imperio& impe);
};

