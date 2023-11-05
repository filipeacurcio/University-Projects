#pragma once
#include "Tecnologias.h"


class BancoCentral:public Tecnologias{

public:
	BancoCentral();
	bool AplicaTec(Imperio& impe) override;
	bool AplicaTecDebug(Imperio& impe) override;
	std::string getAsString()const override;
};

