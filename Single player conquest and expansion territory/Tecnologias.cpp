#include "Tecnologias.h"
#include "Imperio.h"

using namespace std;

Tecnologias::Tecnologias(std::string nome, int custo){
	this->nome = nome;
	this->custo = custo;
	this->adquirido = false;
}

int Tecnologias::getCusto() const{
	return custo;
}

std::string Tecnologias::getNome() const{
	return nome;
}

void Tecnologias::Adquire(){
	adquirido = true;
}

bool Tecnologias::isAdquirido() const{
	return adquirido;
}

std::string Tecnologias::getAsString() const{
	ostringstream os;
	os << "Nome: " << nome << endl
		<< "Custo: " << custo << endl;
	return os.str();
}

bool Tecnologias::AplicaTec(Imperio& impe){
	return false;
}

bool Tecnologias::AplicaTecDebug(Imperio& impe){
	return false;
}


Tecnologias::~Tecnologias(){
}
