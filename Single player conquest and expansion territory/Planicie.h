#pragma once
#include "Continente.h"
class Planicie: public Continente{
	static int conta;
public:
	Planicie();
	void alteraProd(int ano, int turnos) override;

};

