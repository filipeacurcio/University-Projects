#pragma once
#include "Territorio.h"
#include "Tecnologias.h"
#include <string>
#include <sstream>
#include <vector>
class Imperio{
	int cofre, armazem, cap_cofre, cap_armazem, forca_militar, limite_forcamilitar;
	bool sistema_comercial;
	std::vector<Tecnologias*> tecnologia;
	std::vector<Territorio*> territorios; // vetor dinamico como no mundo  

public:
	Imperio();
	// Recolha de material
	int recolheOuro()const;
	int recolheProdutos()const;
	// Verificar capacidades
	bool verificaCapacidadeCofre(int valor)const;
	bool verificaCapacidadeArmazem(int valor);
	// Alterar quantidade de material
	void aumentaCapCofre(int valor);
	void aumentaCapArmazem(int valor);
	bool aumentaCofre(int valor);
	bool aumentaArmazem(int valor);
	bool diminuiCofre(int valor);
	bool diminuiArmazem(int valor);
	void diminuiForca();
	void implementaSistemaComercial();
	void setLimiteForca(int valor);
	bool ModificaOuro(int valor);
	bool ModificaProd(int valor);
	bool aumentaForcaMilitar(int valor);
	void eliminaTerritorio();

	bool adicionaTerritorio(Territorio &a);
	void adicionaTecnologia(Tecnologias* t);
	// Funções gets
	std::string getAsString()const;
	std::string getConquistas()const;
	int getCofre()const;
	int getArmazem()const;
	int getForcaMilitar()const;
	bool isSistemaComercial();
	int GetNterritorios();
	bool isMisseis();
	bool isDefesasTerritoriais();
	bool verificaTecTerr();
	int getResistenciaLastTerr();
	void Invadido();
	int getPontosVitoria(int total_territorios);
	// comandos utilizador
	bool maisOuro();
	bool maisProd();
	bool maisMilitar();

	~Imperio();
	
};

