#include "Territorio.h"
#include "Imperio.h"

using namespace std;

Territorio::Territorio(std::string nome, int resistencia, int cria_prod, int cria_ouro,int p_vitoria){
	this->nome = nome;
	this->resistencia = resistencia;
	this->cria_prod = cria_prod;
	this->cria_ouro = cria_ouro;
	this->conquistado = false;
	this->p_vitoria = p_vitoria;
}
Territorio::Territorio(std::string nome, int resistencia, int cria_prod, int cria_ouro, int conta,int p_vitoria) {
	ostringstream os;
	os << nome << conta;
	nome = os.str();
	this->nome = nome;
	this->resistencia = resistencia;
	this->cria_prod = cria_prod;
	this->cria_ouro = cria_ouro;
	this->conquistado = false;
	this->p_vitoria = p_vitoria;
}


std::string Territorio::getAsString() const{
	ostringstream oss;
	oss << "Nome: " << nome << endl
		<< "Resistencia: " << resistencia << endl
		<< "Cria Produtos: " << cria_prod << endl
		<< "Cria Ouro: " << cria_ouro << endl;

	return oss.str();
}

string Territorio::getNome() const{
	return nome;
}

int Territorio::getResistencia() const{
	return resistencia;
}

bool Territorio::isConquistado() const{
	return conquistado;
}

int Territorio::getCriaOuro() const{
	return cria_ouro;
}

int Territorio::getCriaProd() const{
	return cria_prod;
}

int Territorio::getPVitorias(){
	return p_vitoria;
}

void Territorio::setCriaProd(int valor){
	cria_prod = valor;
}

void Territorio::setCriaOuro(int valor){
	cria_ouro = valor;
}

void Territorio::Conquistado(){
	conquistado = true;
}

void Territorio::Invadido(){
	conquistado = false;
}

void Territorio::aumentaResistencia(int valor){
	resistencia += valor;
}

void Territorio::alteraProd(int ano, int turnos){
}

bool Territorio::isConquistavel(Imperio& impe){
	return true;
}

Territorio::~Territorio(){
}
