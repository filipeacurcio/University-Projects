#pragma once
#include "Eventos.h"

class SemEvento:public Eventos{
public:
	SemEvento();
	int fazEvento(Imperio& imp, int ano)override;
	std::string getAsString() override;
};

