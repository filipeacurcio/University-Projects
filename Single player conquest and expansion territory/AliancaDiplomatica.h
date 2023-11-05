#pragma once
#include "Eventos.h"

class AliancaDiplomatica:public Eventos{

public:
	AliancaDiplomatica();
	int fazEvento(Imperio& imp,int anos)override;
	std::string getAsString() override;
};

