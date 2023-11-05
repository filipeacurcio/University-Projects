#pragma once
#include <iostream>
#include <sstream>
#include <string>

class Imperio;

static int contador = 1;

class Territorio{
	std::string nome;
	int resistencia, cria_prod, cria_ouro, p_vitoria;
	bool conquistado;
public:
	Territorio(std::string nome, int resistencia = 0, int cria_prod = 0, int cria_ouro = 0, int p_vitoria = 0);
	Territorio(std::string nome, int resistencia, int cria_prod, int cria_ouro, int conta,int p_vitoria);
	virtual std::string getAsString()const;
	std::string getNome()const;
	int getResistencia() const;
	bool isConquistado()const;
	int getCriaOuro()const;
	int getCriaProd()const;
	int getPVitorias();
	void setCriaProd(int valor);
	void setCriaOuro(int valor);
	void Conquistado();
	void Invadido();
	void aumentaResistencia(int valor);
	virtual void alteraProd(int ano, int turnos);
	virtual bool isConquistavel(Imperio& impe);
	~Territorio();
};

