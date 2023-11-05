#include "RecursoAbandonado.h"
using namespace std;

RecursoAbandonado::RecursoAbandonado(): Eventos("Recurso Abandonado"){
}

int RecursoAbandonado::fazEvento(Imperio& imp, int ano){
	if (ano == 1)
		imp.aumentaArmazem(1);
	if (ano == 2)
		imp.aumentaCofre(1);
	return 0;
}

std::string RecursoAbandonado::getAsString(){
	ostringstream os;
	os << "Nome: " << getNome() << endl
		<< "Resumo: " << " fornece ao império uma unidade de produtos ou de ouro" << endl;
	return os.str();
}
