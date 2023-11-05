#pragma once
#include "Tecnologias.h"


class MisseisTeleguiados: public Tecnologias{

public:
	MisseisTeleguiados();
	bool AplicaTec(Imperio& impe) override;
	bool AplicaTecDebug(Imperio& impe) override;
	std::string getAsString()const override;
};

