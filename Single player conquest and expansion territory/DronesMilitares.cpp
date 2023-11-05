#include "DronesMilitares.h"
#include "Imperio.h"
using namespace std;

DronesMilitares::DronesMilitares():Tecnologias("Drones militares", 3){
}

bool DronesMilitares::AplicaTec(Imperio& impe){
	if (impe.diminuiCofre(getCusto()) == true) {
		impe.setLimiteForca(5);
		impe.adicionaTecnologia(new DronesMilitares);
		return true;
	}
	else
		return false;
}

bool DronesMilitares::AplicaTecDebug(Imperio& impe){
		impe.setLimiteForca(5);
		impe.adicionaTecnologia(new DronesMilitares);
		return true;
}

std::string DronesMilitares::getAsString() const{
	ostringstream os;
	os << "Nome: " << getNome() << endl
		<< "Custo: " << getCusto() << endl;
	if (isAdquirido() == true)
		os << "Tecnologia adquirida." << endl;
	else
		os << "Tecnologia nao adquirida" << endl;
		os << "Resumo da tecnologia :" << endl << "Esta tecnologia faz passar o limite maximo da forca militar para 5" << endl;
	return os.str();
}
