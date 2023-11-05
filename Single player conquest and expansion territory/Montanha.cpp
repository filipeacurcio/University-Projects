#include "Montanha.h"
using namespace std;

int Montanha::conta = 1;
Montanha::Montanha():Continente("Montanha", 6, 0, 0, conta++){
	turnos_conquistado = 0;

}

void Montanha::alteraProd(int ano, int turnos){
	if (turnos_conquistado == 2)
		setCriaProd(1);

	if (isConquistado() == true)
		turnos_conquistado++;

}
