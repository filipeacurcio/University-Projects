#include "Imperio.h"


using namespace std;
Imperio::Imperio(){
	cofre = 0;
	armazem = 0;
	cap_cofre = 3;
	cap_armazem = 3;
	forca_militar = 3;
	limite_forcamilitar = 3;
	sistema_comercial = false;
}

int Imperio::recolheOuro() const{
	int total = 0;
	for (int i = 0; i < territorios.size(); ++i) {
		total += territorios.at(i)->getCriaOuro();
	}

	return total;
}

int Imperio::recolheProdutos() const{
	int total = 0;
	for (int i = 0; i < territorios.size(); ++i)
		total += territorios.at(i)->getCriaProd();
	return total;
}

void Imperio::diminuiForca(){
	if (forca_militar - 1 >= 0)
		forca_militar--;
}

bool Imperio::verificaCapacidadeCofre(int valor)const{
	if (valor + cofre > cap_cofre)
		return false;
	else
		return true;
	
}

bool Imperio::verificaCapacidadeArmazem(int valor){
	if (valor + armazem > cap_armazem)
		return false;
	else
		return true;
}

void Imperio::aumentaCapCofre(int valor){
	this->cap_cofre = valor;

}

void Imperio::aumentaCapArmazem(int valor){
	this->cap_armazem = valor;
}

bool Imperio::aumentaCofre(int valor){
	if (valor + cofre > cap_cofre) {
		cofre = cap_cofre;
		return false;
	}
	else {
		cofre += valor;
		return true;
	}
}

bool Imperio::aumentaArmazem(int valor){
	if (valor + armazem > cap_armazem) {
		armazem = cap_armazem;
		return false;
	}
	else {
		armazem += valor;
		return true;
	}

}

bool Imperio::diminuiCofre(int valor){
	if (cofre - valor < 0)
		return false;
	else {
		cofre -= valor;
		return true;
	}
}

bool Imperio::diminuiArmazem(int valor){
	if (armazem - valor < 0)
		return false;
	else{
		armazem -= valor;
		return true;
	}
		
}

void Imperio::implementaSistemaComercial(){
	sistema_comercial = true;
}

void Imperio::setLimiteForca(int valor){
	limite_forcamilitar = valor;
}

bool Imperio::ModificaOuro(int valor){
	if (valor < 0 || valor > cap_cofre)
		return false;
	else {
		cofre = valor;
		return true;
	}
}

bool Imperio::ModificaProd(int valor) {
	if (valor < 0 || valor > cap_armazem)
		return false;
	else {
		armazem = valor;
		return true;
	}
}

bool Imperio::aumentaForcaMilitar(int valor){
	if (forca_militar + valor > limite_forcamilitar)
		return false;
	else {
		forca_militar += valor;
		return true;
	}
}

void Imperio::eliminaTerritorio(){
	territorios.pop_back();
}

bool Imperio::isSistemaComercial(){
	return sistema_comercial;
}

int Imperio::GetNterritorios(){
	int aux = 0; 
	for (int i = 0; i < territorios.size(); ++i)
		aux++;
	return aux;
}

bool Imperio::isMisseis(){
	for (int i = 0; i < tecnologia.size(); ++i) {
		if (tecnologia.at(i)->getNome() == "Misseis teleguiados")
			return true;
	}
	return false;
}

bool Imperio::isDefesasTerritoriais(){
	for (int i = 0; i < tecnologia.size(); ++i) {
		if (tecnologia.at(i)->getNome() == "Defesas territoriais")
			return true;
	}
	return false;
}

bool Imperio::verificaTecTerr(){
	if (GetNterritorios() < 5) {
		return false;
	}
	else if (isMisseis() == false)
		return false;
	return true;
}

int Imperio::getResistenciaLastTerr(){
	int res = 0;
	if (isDefesasTerritoriais() == true)
	res = territorios.back()->getResistencia();
	res++;
	return res;
}

void Imperio::Invadido(){
	territorios.back()->Invadido();
}

int Imperio::getPontosVitoria(int total_territorios){
	int aux = 0;
	for (int i = 0; i < territorios.size(); ++i) {
		aux += territorios.at(i)->getPVitorias();
	}
	if (tecnologia.size() == 5)
		aux += tecnologia.size() + 1;
	else
		aux += tecnologia.size();
	if (territorios.size() == total_territorios)
		aux += 3;

	return aux;
}

bool Imperio::maisOuro(){
	if (getArmazem() >= 2) {
		diminuiArmazem(2);
		bool aux = aumentaCofre(1);
		return aux;
	}
	return false;
}

bool Imperio::maisProd(){
	if (getCofre() >= 2) {
		diminuiCofre(2);
		bool aux = aumentaArmazem(1);
		return aux;
	}
	return false;
}

bool Imperio::maisMilitar(){
	if(forca_militar + 1 > 5)
		return false;
	else {
		if (diminuiCofre(1) == true && diminuiArmazem(1) == true) {
			forca_militar++;
			return true;
		}
		else
			return false;
	}

}

bool Imperio::adicionaTerritorio(Territorio &a){
	if (a.isConquistavel(*this) == true) {
		territorios.push_back(&a);
		return true;
	}
	return false;
}

void Imperio::adicionaTecnologia(Tecnologias* t){
	tecnologia.push_back(t);
}

std::string Imperio::getAsString() const{
	ostringstream oss;
	oss << "Capacidade de Cofre: " << cap_cofre << endl
		<< "Quantidade de ouro : " << cofre << endl
		<< "Capacidade do armazem: " << cap_armazem << endl
		<< "Quantidade de produtos: " << armazem << endl
		<< "Forca Militar Maxima: " << limite_forcamilitar << endl
		<< "Força Militar: " << forca_militar << endl
		<< "Tecnologias adquiridas: " << endl;
	for (int i = 0; i <tecnologia.size(); ++i)
		oss << tecnologia.at(i)->getAsString() << endl;

	return oss.str();
}

std::string Imperio::getConquistas() const{
	ostringstream oss;
	for (int i = 0; i < territorios.size(); ++i) {
		oss << territorios.at(i)->getAsString();
	}

	return oss.str();
}

int Imperio::getCofre() const{
	return cofre;
}

int Imperio::getArmazem() const{
	return armazem;
}

int Imperio::getForcaMilitar()const {
	return forca_militar;
}


Imperio::~Imperio(){
	delete territorios[0];
}
