#include "Invasao.h"
#include <time.h>
using namespace std;

Invasao::Invasao(): Eventos("Invasao"){
}

int Invasao::fazEvento(Imperio& imp, int anos){
	srand(time(NULL));
	int f_sorte = 0;
	f_sorte = rand() % (6 - 1 + 1) + 1;

	if (anos == 1)
		f_sorte += 2;
	if (anos == 2)
		f_sorte += 3;
	cout << "Fator sorte : " << f_sorte << endl;

	if (f_sorte > imp.getResistenciaLastTerr() && imp.GetNterritorios() == 1) {
		return 1;
	}
	if (f_sorte < imp.getResistenciaLastTerr()) {
		return 2;
	}
	else {
		imp.Invadido();
		imp.eliminaTerritorio();
		return 3;
	}


}

std::string Invasao::getAsString(){
	ostringstream os;
	os << "Nome: " << getNome() << endl
		<< "Resumo: " << " um qualquer outro império concorrente, está a tentar conquistar um dos territórios do jogador" << endl;
	return os.str();
}
