#include "DefesasTerritoriais.h"
#include "Imperio.h"
using namespace std;

DefesasTerritoriais::DefesasTerritoriais():Tecnologias("Defesas territoriais", 4){
}

bool DefesasTerritoriais::AplicaTec(Imperio& impe){
	if (impe.diminuiCofre(getCusto()) == true) {
		impe.adicionaTecnologia(new DefesasTerritoriais);
		return true;
	}
	else
		return false;
}

bool DefesasTerritoriais::AplicaTecDebug(Imperio& impe){
		impe.adicionaTecnologia(new DefesasTerritoriais);
		return true;
}

std::string DefesasTerritoriais::getAsString() const{
	ostringstream os;
	os << "Nome: " << getNome() << endl
		<< "Custo: " << getCusto() << endl;
		if (isAdquirido() == true)
			os << "Tecnologia adquirida." << endl;
		else
			os << "Tecnologia nao adquirida" << endl;
		os << "Resumo da tecnologia :" << endl << "E um equipamento especial de defesa que acrescenta 1 unidade a resistência do territorio invadido durante um evento de invasao" << endl;
	return os.str();
}
