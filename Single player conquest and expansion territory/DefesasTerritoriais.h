#pragma once
#include "Tecnologias.h"

class DefesasTerritoriais: public Tecnologias{

public:
	DefesasTerritoriais();
	bool AplicaTec(Imperio& impe) override;
	bool AplicaTecDebug(Imperio& impe) override;
	std::string getAsString()const override;
};

