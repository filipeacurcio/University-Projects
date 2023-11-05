#pragma once
#include "TerritorioInicial.h"
#include "Imperio.h"
#include "Tecnologias.h"
#include "Montanha.h"
#include "Planicie.h"
#include "Fortaleza.h"
#include "Mina.h"
#include "Duna.h"
#include "Castelo.h"
#include "DronesMilitares.h"
#include "MisseisTeleguiados.h"
#include "DefesasTerritoriais.h"
#include "BolsaValores.h"
#include "BancoCentral.h"
#include "RefugioPiratas.h"
#include"Pescaria.h"
#include "Eventos.h"
#include "AliancaDiplomatica.h"
#include "Invasao.h"
#include "SemEvento.h"
#include "RecursoAbandonado.h"
#include <string>
#include <vector>
#include <sstream>
#include <fstream>
#include <time.h> 


class Mundo{
	std::vector<Territorio*> territorios;
	Imperio imp;
	std::vector<Tecnologias*> tecnologias;
	std::vector<Eventos*>evento;
	
public:
	Mundo();
	// clase robusta
	Mundo(const Mundo& ob);
	Mundo& operator=(const Mundo& ob);

	void avancaTerritorio(int ano, int turnos);
	int recolheProdutos()const;
	int recolheOuro()const;
	void carregaOuro(int valor);
	void carregaProduto(int valor);
	std::string getTerritorio(std::string territorio);
	bool CarregaTerritorios(std::string fich);
	bool acrescentaTerritorio(std::string nome);
	void adicionaImperio(Territorio&a);
	std::string mostraTerritorios();
	bool verificaTerritorio(std::string nome, int * f_sorte);
	int getPVitorias();
	void addTerrInicial();

	//Imperio
	void dimunuiForca();
	bool maisouro();
	bool maisprod();
	bool maismilitar();
	bool ModificaOuro(int valor);
	bool ModificoProd(int valor);
	bool isSistemaComercial();
	//Tecologias
	bool verificaTec(std::string nome);
	int getPosEvento(std::string nome);
	//Eventos
	int fazEvento(int valor, int anos);
	std::string getEvento(int valor);
	//Debug
	bool TomaTerritorio(std::string nome);
	bool TomaTecnologia(std::string nome);


	~Mundo();


};

