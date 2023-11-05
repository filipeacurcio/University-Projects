#include "BancoCentral.h"
#include "Imperio.h"
using namespace std;

BancoCentral::BancoCentral():Tecnologias("Banco Central", 3){
}

bool BancoCentral::AplicaTec(Imperio& impe){
	if (impe.diminuiCofre(getCusto()) == true) {
		impe.aumentaCapCofre(5);
		impe.aumentaCapArmazem(5);
		impe.adicionaTecnologia(new BancoCentral);
		return true;
	}
	else
		return false;
}

bool BancoCentral::AplicaTecDebug(Imperio& impe){
	impe.aumentaCapCofre(5);
	impe.aumentaCapArmazem(5);
	impe.adicionaTecnologia(new BancoCentral);
	return true;
}

std::string BancoCentral::getAsString() const{
	ostringstream os;
	os << "Nome: " << getNome() << endl
		<< "Custo: " << getCusto() << endl;
	if (isAdquirido() == true)
		os << "Tecnologia adquirida." << endl;
	else
		os << "Tecnologia nao adquirida" << endl;
		os << "Resumo da tecnologia :" << endl << "Permite aumentar em duas unidades a capacidade de armazenamento do armazem e do cofre do imperio" << endl;
	return os.str();
}
