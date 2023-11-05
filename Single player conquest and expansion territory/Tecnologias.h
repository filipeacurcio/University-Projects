#pragma once
#include <string>
#include <sstream>
#include <vector>

class Imperio;

class Tecnologias{
	std::string nome;
	int custo;
	bool adquirido;
public:
	Tecnologias(std::string nome, int custo);
	int getCusto()const;
	std::string getNome()const;
	void Adquire();
	bool isAdquirido()const;
	virtual std::string getAsString()const;
	virtual bool AplicaTec(Imperio& impe);
	virtual bool AplicaTecDebug(Imperio& impe);
	~Tecnologias();
};


