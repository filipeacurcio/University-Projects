#include "Castelo.h"


int Castelo::conta = 1;
Castelo::Castelo():Continente("Castelo", 7, 3, 1, conta++){
}

void Castelo::alteraProd(int ano, int turnos){
	if (turnos > 2)
		setCriaProd(0);
	else if (turnos == 1)
		setCriaProd(3);
}
