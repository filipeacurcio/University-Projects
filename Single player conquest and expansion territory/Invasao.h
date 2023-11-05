#pragma once
#include "Eventos.h"
#include <time.h>

class Invasao:public Eventos{

public:
	Invasao();
	int fazEvento(Imperio& imp, int anos)override;
	std::string getAsString() override;
};

