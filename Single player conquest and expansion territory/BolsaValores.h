#pragma once
#include "Tecnologias.h"

class BolsaValores:public Tecnologias{

public:
	BolsaValores();
	bool AplicaTec(Imperio& impe) override;
	bool AplicaTecDebug(Imperio& impe) override;
	std::string getAsString()const override;
};

