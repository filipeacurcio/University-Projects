#pragma once
#include "Eventos.h"
class RecursoAbandonado: public Eventos{

public:
	RecursoAbandonado();
	int fazEvento(Imperio& imp, int ano) override;
	std::string getAsString() override;
};

