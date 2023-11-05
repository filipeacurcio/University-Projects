#include "Pescaria.h"
#include "Imperio.h"
using namespace std;
int Pescaria::conta = 1;

Pescaria::Pescaria():Ilhas("Pescaria", 9, 2, 0, conta++){
}

bool Pescaria::isConquistavel(Imperio& impe){
	if (impe.verificaTecTerr() == true) {
		return true;
	}
	else
		return false;
}

void Pescaria::alteraProd(int ano, int turnos){
	if (ano == 2)
		setCriaProd(4);
}


