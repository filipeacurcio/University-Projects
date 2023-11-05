#include "SemEvento.h"
using namespace std;

SemEvento::SemEvento(): Eventos("Sem Evento"){
}

int SemEvento::fazEvento(Imperio& imp, int ano){
	return 2;
}

std::string SemEvento::getAsString(){
	ostringstream os;
	os << "Nome: " << getNome() << endl
		<< "Resumo: " << " Nada ocorre e todos podem dormir descansados" << endl;
	return os.str();
}
