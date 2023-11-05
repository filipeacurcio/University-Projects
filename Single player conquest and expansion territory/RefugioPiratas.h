#pragma once
#include "Ilhas.h"

class RefugioPiratas: public Ilhas{
	static int conta;
public:
	RefugioPiratas();
	bool isConquistavel(Imperio& impe);

};

