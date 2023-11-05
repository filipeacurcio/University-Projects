#include "BolsaValores.h"
#include "Imperio.h"
using namespace std;

BolsaValores::BolsaValores():Tecnologias("Bolsa de valores", 2){
}

bool BolsaValores::AplicaTec(Imperio& impe){
	if (impe.diminuiCofre(getCusto()) == true) {
		impe.adicionaTecnologia(new BolsaValores);
		impe.implementaSistemaComercial();
		return true;
	}
	else
		return false;

}

bool BolsaValores::AplicaTecDebug(Imperio& impe){
	impe.adicionaTecnologia(new BolsaValores);
	impe.implementaSistemaComercial();
	return true;
}

std::string BolsaValores::getAsString() const{
	ostringstream os;
	os << "Nome: " << getNome() << endl
		<< "Custo: " << getCusto() << endl;
	if (isAdquirido() == true)
		os << "Tecnologia adquirida." << endl;
	else
		os << "Tecnologia nao adquirida" << endl;
		os << "Resumo da tecnologia :" << endl << "Sistema comercial que torna possiveis as trocas entre produtos e ouro" << endl;
	return os.str();
}
