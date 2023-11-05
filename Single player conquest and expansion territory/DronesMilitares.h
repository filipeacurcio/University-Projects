#pragma once
#include "Tecnologias.h"

class DronesMilitares: public Tecnologias{

public:
	DronesMilitares();
	bool AplicaTec(Imperio& impe) override;
	bool AplicaTecDebug(Imperio& impe) override;
	std::string getAsString()const override;
};

