#pragma once
#include "Continente.h"

class Castelo: public Continente{
	static int conta;
public:
	Castelo();
	void alteraProd(int ano, int turnos) override;
};

