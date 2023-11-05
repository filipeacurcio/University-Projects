#pragma once
#include "Continente.h"

class Mina:public Continente {
	static int conta;
public:
	Mina();
	void alteraProd(int ano, int turnos) override;


};

