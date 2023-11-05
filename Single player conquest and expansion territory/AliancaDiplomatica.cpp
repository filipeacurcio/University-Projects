#include "AliancaDiplomatica.h"
using namespace std;

AliancaDiplomatica::AliancaDiplomatica():Eventos("Alianca Diplomatica"){
}

int AliancaDiplomatica::fazEvento(Imperio& imp, int ano){
	imp.aumentaForcaMilitar(1);
	return 0;
}

std::string AliancaDiplomatica::getAsString(){
	ostringstream os;
	os << "Nome: " << getNome() << endl
		<< "Resumo: " << "A forca militar aumenta uma unidade" << endl;
	return os.str();
}
