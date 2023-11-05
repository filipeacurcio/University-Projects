#include "Mina.h"

int Mina::conta = 1;
Mina::Mina():Continente("Mina", 5, 0, 1, conta++){
}

void Mina::alteraProd(int ano, int turnos){
	if (turnos == 1)
		setCriaOuro(1);
	else if (turnos == 4)
		setCriaOuro(2);

}
