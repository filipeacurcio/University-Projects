#pragma once
#include "Mundo.h"
#include "TerritorioInicial.h"
#include <string>
#include <iostream>




class Comandos{
	Mundo world;
public:

	void leComando();
	std::string getEvento(int turnos)const;
};

