#include "Eventos.h"
using namespace std;
Eventos::Eventos(std::string nome){
	this->nome = nome;
}

int Eventos::fazEvento(Imperio& imp, int ano){
	return 0;
}

std::string Eventos::getNome(){
	return nome;
}

std::string Eventos::getAsString(){
	ostringstream os;
	os << "Nome: " << nome << endl;
	return os.str();
}
