#pragma once
#include <string>
#include "Imperio.h"

class Eventos{
	std::string nome;
public:
	Eventos(std::string nome);
	virtual int fazEvento(Imperio& imp, int ano);
	std::string getNome();
	virtual std::string getAsString();

};

