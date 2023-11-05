#include "Planicie.h"

int Planicie::conta = 1;

Planicie::Planicie():Continente("Planicie", 5, 1, 1, conta++){
}

void Planicie::alteraProd(int ano, int turnos){
	if(ano == 2)
	setCriaProd(2);
}
