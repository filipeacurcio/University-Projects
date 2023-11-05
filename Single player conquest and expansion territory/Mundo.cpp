#include "Mundo.h"

using namespace std;


Mundo::Mundo(){
	tecnologias.push_back(new DronesMilitares);
	tecnologias.push_back(new MisseisTeleguiados);
	tecnologias.push_back(new DefesasTerritoriais);
	tecnologias.push_back(new BolsaValores);
	tecnologias.push_back(new BancoCentral);
	evento.push_back(new RecursoAbandonado);
	evento.push_back(new Invasao);
	evento.push_back(new AliancaDiplomatica);
	evento.push_back(new SemEvento);

	
}

Mundo::Mundo(const Mundo& ob){
	(*this) = ob;
}

Mundo& Mundo::operator=(const Mundo& ob){
	if (this != &ob) {
		for (unsigned int i = 0; i < territorios.size(); ++i)
			delete territorios.at(i);
		for (unsigned int i = 0; i < ob.territorios.size(); ++i)
			territorios.push_back(new Territorio(ob.territorios[i]->getNome(), ob.territorios[i]->getResistencia(), ob.territorios[i]->getCriaProd(), ob.territorios[i]->getCriaOuro()));
		for (unsigned int i = 0; i < tecnologias.size(); ++i)
			delete tecnologias.at(i);
		for (unsigned int i = 0; i < ob.tecnologias.size(); ++i)
			tecnologias.push_back(new Tecnologias(ob.tecnologias[i]->getNome(), ob.tecnologias[i]->getCusto()));
		for (unsigned int i = 0; i < evento.size(); ++i)
			delete evento.at(i);
		for (unsigned int i = 0; i < ob.evento.size(); ++i)
			evento.push_back(ob.evento.at(i));

		imp = ob.imp;
	}
	return *this;
}

void Mundo::avancaTerritorio(int ano, int turnos){
	for (int i = 0; i < territorios.size(); ++i) {
		territorios.at(i)->alteraProd(ano, turnos);
	}
}

int Mundo::recolheProdutos() const{
	return imp.recolheProdutos();
}

int Mundo::recolheOuro() const{
	return imp.recolheOuro();
}

void Mundo::carregaOuro(int valor){
	imp.aumentaCofre(valor);
}

void Mundo::carregaProduto(int valor){
	imp.aumentaArmazem(valor);
}

std::string Mundo::getTerritorio(string territorio){
	ostringstream oss;
	for (int i = 0; i < territorios.size(); ++i) {
		if (territorios.at(i)->getNome() == territorio)
			oss << territorios.at(i)->getAsString();
	}
	return oss.str();
}
bool Mundo::CarregaTerritorios(string fich){
	ifstream dados(fich);
	string nome, s, comando;
	int quantidade;
	if (!dados.is_open()) {
		return false;
	}	
	while (!dados.eof()) {
		getline(dados, s);
		istringstream iss(s);
		iss>> comando >> std::ws >> nome >> std::ws >>  quantidade;
			//se correu bem a leitura
		if (iss && comando == "cria") {
			for(int i = 0; i < quantidade; ++i)
			acrescentaTerritorio(nome);
		}
	}
	dados.close();
	return true;
}

bool Mundo::acrescentaTerritorio(string nome){
	if(nome == "Planicie")
		territorios.push_back(new Planicie);
	if(nome == "Montanha")
		territorios.push_back(new Montanha);
	if(nome == "Fortaleza")
		territorios.push_back(new Fortaleza);
	if (nome == "Mina")
		territorios.push_back(new Mina);
	if (nome == "Duna")
		territorios.push_back(new Duna);
	if (nome == "Castelo")
		territorios.push_back(new Castelo);
	if (nome == "RefugiodosPiratas")
		territorios.push_back(new RefugioPiratas);
	if (nome == "Pescaria")
		territorios.push_back(new Pescaria);
	return true;
}

void Mundo::adicionaImperio(Territorio& a){
	imp.adicionaTerritorio(a);
}

std::string Mundo::mostraTerritorios(){
	ostringstream oss;
	oss << "Territorios do Mundo: " << endl;
	for (int i = 0; i < territorios.size(); ++i)
		oss << territorios.at(i)->getAsString();
	oss << "Territorios conquistados pelo Imperio: " << endl
		<< imp.getConquistas()
		<< imp.getAsString()
		<< "Tecnologias disponiveis: " << endl;
	for (int i = 0; i < tecnologias.size(); ++i)
		oss << tecnologias.at(i)->getAsString();

	return oss.str();
}

bool Mundo::verificaTerritorio(string nome, int *f_sorte){
	srand(time(NULL));
	*f_sorte = rand() % (6 - 1 + 1) + 1;

	for (int i = 0; i < territorios.size(); ++i) {
			if (territorios.at(i)->getNome() == nome) {
				if (territorios.at(i)->isConquistado() == false) {
					f_sorte += imp.getForcaMilitar();
					if (*f_sorte >= territorios.at(i)->getResistencia()) {
						if (imp.adicionaTerritorio(*territorios.at(i)) == true) {
							territorios.at(i)->Conquistado();
							return true;
						}
					}
				}
		}
	}
	return false;
}

int Mundo::getPVitorias(){

	int aux = imp.getPontosVitoria(territorios.size());
	return aux;
}

void Mundo::addTerrInicial(){
	territorios.push_back(new TerritorioInicial);
}

void Mundo::dimunuiForca(){
	imp.diminuiForca();
}

bool Mundo::maisouro(){
	bool aux = imp.maisOuro();
	return aux;
}

bool Mundo::maisprod(){
	bool aux = imp.maisProd();
	return aux;
}

bool Mundo::maismilitar(){
	bool aux = imp.maisMilitar();
	return aux;
}

bool Mundo::ModificaOuro(int valor){
	if (imp.ModificaOuro(valor) == false)
		return false;
	else
		return true;
}

bool Mundo::ModificoProd(int valor){
	if (imp.ModificaProd(valor) == false)
		return false;
	else
		return true;
}

bool Mundo::isSistemaComercial(){
	if (imp.isSistemaComercial() == false)
		return false;
	else
		return true;
}



bool Mundo::verificaTec(std::string nome) {
	for (int i = 0; i < tecnologias.size(); ++i) {
		if (nome == tecnologias.at(i)->getNome()){
			if (tecnologias.at(i)->AplicaTec(imp) == true) {
				tecnologias.at(i)->Adquire();
				return true;
			}
		}
	}
	return false;
}

int Mundo::getPosEvento(std::string nome){
	for (int i = 0; i < evento.size(); ++i) {
		if (nome == evento.at(i)->getNome()) {
				return i;
		}
	}
	return -1;
}

int Mundo::fazEvento(int valor, int ano){
	int aux = 0;
	aux = evento.at(valor)->fazEvento(imp, ano);
	return aux;
}

std::string Mundo::getEvento(int valor){
	ostringstream os;
	os << evento.at(valor)->getAsString();
	return os.str();
}

bool Mundo::TomaTerritorio(std::string nome){
	for (int i = 0; i < territorios.size(); ++i) {
		if (territorios.at(i)->getNome() == nome && territorios.at(i)->isConquistado() == false) {
			imp.adicionaTerritorio(*territorios.at(i));
			territorios.at(i)->Conquistado();
			return true;
		}
	}
	return false;
}

bool Mundo::TomaTecnologia(std::string nome){
	for (int i = 0; i < tecnologias.size(); ++i) {
		if (nome == tecnologias.at(i)->getNome()) {
			tecnologias.at(i)->AplicaTecDebug(imp);
			tecnologias.at(i)->Adquire();
			return true;
		}
	}
}

Mundo::~Mundo(){
	for (int i = 0; i < territorios.size(); ++i)
		delete territorios.at(i);
	for (int i = 0; i < tecnologias.size(); ++i)
		delete tecnologias.at(i);
	for (int i = 0; i < evento.size(); ++i)
		delete evento.at(i);
}



