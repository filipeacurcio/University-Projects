#pragma once
#include "Continente.h"


class Montanha: public Continente{
	static int conta;
	int turnos_conquistado;
public:
	Montanha();
	void alteraProd(int ano, int turnos) override;
};


