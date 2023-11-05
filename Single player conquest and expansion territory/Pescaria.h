#pragma once
#include "Ilhas.h"


class Pescaria: public Ilhas{
	static int conta;
public:
	Pescaria();
	bool isConquistavel(Imperio& impe)override;
	 void alteraProd(int ano, int turnos)override;
};

