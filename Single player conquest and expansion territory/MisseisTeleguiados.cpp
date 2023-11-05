#include "MisseisTeleguiados.h"
#include "Imperio.h"
using namespace std;

MisseisTeleguiados::MisseisTeleguiados():Tecnologias("Misseis teleguiados",4){
}

bool MisseisTeleguiados::AplicaTec(Imperio& impe){
	if (impe.diminuiCofre(getCusto()) == true) {
		impe.adicionaTecnologia(new MisseisTeleguiados);
		return true;
	}
	else
		return false;

}

bool MisseisTeleguiados::AplicaTecDebug(Imperio& impe){
		impe.adicionaTecnologia(new MisseisTeleguiados);
		return true;
}

std::string MisseisTeleguiados::getAsString() const{
	ostringstream os;
	os << "Nome: " << getNome() << endl
		<< "Custo: " << getCusto() << endl;
		if (isAdquirido() == true)
			os << "Tecnologia adquirida." << endl;
		else
			os << "Tecnologia nao adquirida" << endl;
		os << "Resumo da tecnologia :" << endl << "Esta tecnologia e necessaria para conquistar ilhas" << endl;
	return os.str();
}
