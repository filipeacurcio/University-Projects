#include "Comandos.h"

using namespace std;

void Comandos::leComando(){
	string comando, ficheiro, s, nome, aux;
	int n, flag = 0, ouro = 0, produtos = 0, flag_troca = 0, flag_recolhe = 0, flag_conquista = 0, flag_evento = 0, flag_fase3 = 0;
	 int turnos = 1, fase = 1, ano = 1, f_sorte = 0, evento = 0, pontuacao = 0;

	Territorio* a = new TerritorioInicial;
	world.adicionaImperio(*a);
	world.addTerrInicial();
	cout << "Fase de Configuracao." << endl;
	do {
		getline(std::cin, s);
		istringstream iss(s);
		iss >> comando >> std::ws;

		if (comando == "carrega") {
			iss >> ficheiro;
			world.CarregaTerritorios(ficheiro);
		}
		if (comando == "cria") {
			iss >> ficheiro;
			iss >> n;
			for (int i = 0; i < n; ++i)
				world.acrescentaTerritorio(ficheiro);
		}
	} while (comando != "avanca");

	cout << "Fim da fase de configuracao.\nInicio do jogo" << endl;

	do {
		cout << "Fase n: " << fase << endl;
		cout << ">";

		getline(std::cin, s);
		istringstream iss(s);
		iss >> comando >> std::ws;
		if (comando == "avanca" || flag_evento == 2) {
			if (fase >= 4) {
				fase = 1;
				turnos++;
				flag_troca = 0;
				flag_recolhe = 0;
				flag_conquista = 0;
				flag = 0;
				flag_evento = 0;
				flag_fase3 = 0;
				world.avancaTerritorio(ano, turnos);
				if (turnos == 7) {
					ano ++;
					turnos = 1;
				}
			}
			else
				fase++;
		}

		if(comando == "clear")
			system("CLS");


		if (comando == "modifica") {
			iss >> ficheiro;
			if (ficheiro == "ouro") {
				iss >> ouro;
				world.ModificaOuro(ouro);
			}
			if (ficheiro == "prod") {
				iss >> produtos;
				world.ModificoProd(produtos);
			}
		}
		if (comando == "toma") {
			iss >> ficheiro;
			if (ficheiro == "terr"){
				iss >> nome;
				if (world.TomaTerritorio(nome) == true) {
					cout << "Comando Debug toma realizado com sucesso." << endl;
				}
				else
					cout << "Comando Debug falhou." << endl;

			}
		if (ficheiro == "tec") {
			aux = s.substr(s.find_first_of(" \n") + 1);
			aux = aux.substr(aux.find_first_of(" \n") + 1);
			cout << "Tecnologia : " << aux << endl;
			if (world.TomaTecnologia(aux) == true) {
				cout << "Comando Debug toma realizado com sucesso." << endl;
			}
			else
				cout << "Comando Debug falhou." << endl;
			}
		}
		if (comando == "fevento") {
			aux = s.substr(s.find_first_of(" \n") + 1);
			evento = world.getPosEvento(aux);
			if (evento != -1)
				flag_evento = world.fazEvento(evento, ano);
			else
				cout << "Evento nao foi realizado." << endl;

		}


		if (comando == "lista") {
			if (iss >> ficheiro)
				cout << world.getTerritorio(ficheiro);
			else {
				cout << world.mostraTerritorios();
				cout << "Producao no turno atual de ouro: " << ouro << endl
					 << "Producao no turno atual de produtos: " << produtos << endl
 					 << "Ano: " << ano << endl
					 << "Turno: " << turnos << endl
					 << "Fase do turno " << getEvento(fase) << endl	
					 << "Ultimo fator sorte: " << f_sorte << endl;
			}
		}

			if (fase == 1) {
				if (comando == "conquista" && flag_conquista == 0) {
					flag_conquista = 1;
					s = s.substr(s.find_first_of(" \n") + 1);
					if (world.verificaTerritorio(s, &f_sorte) == false) {
						cout << "Impossivel conquistar o territorio" << endl;
						world.dimunuiForca();
					}else
						cout << "Territorio com nome: " << s << " foi conquistado." << endl;
				}
				if (comando == "passa") {
					fase++;
				}
			}

			if (fase == 2) {
				if (flag_recolhe == 0) {
					flag_recolhe = 1;
					ouro = world.recolheOuro();
					produtos = world.recolheProdutos();
					world.carregaOuro(ouro);
					world.carregaProduto(produtos);
				}
				if (world.isSistemaComercial() == true) {
					if (flag_troca == 0) {
						if (comando == "maisouro") {
							flag_troca = 1;
							if (world.maisouro() == true)
								cout << "Adicionado 1 de ouro com sucesso." << endl;
							else
								cout << "Impossivel adicionar 1 de ouro." << endl;
						}
						if (comando == "maisprod") {
							flag_troca = 1;
							if (world.maisprod() == true)
								cout << "Adicionado 1 de produto com sucesso." << endl;
							else
								cout << "Impossivel adicionar 1 de produto." << endl;
						}
					}
				}
			}
			if (fase == 3) {
				if (flag_fase3 == 0) {
					if (comando == "maismilitar") {
						flag_fase3 = 1;
						if (world.maismilitar() == true)
							cout << "Adicionado uma unidade militar com sucesso." << endl;
						else
							cout << "Impossivel adicionar uma unidade militar." << endl;
					}
					if (comando == "adquire") {
						flag_fase3 = 1;
						s = s.substr(s.find_first_of(" \n") + 1);
						if (world.verificaTec(s) == true) {
							cout << "Tecnologia " << s << " comprada com sucesso." << endl;
						}
						else
							cout << "Impossivel de adquirir a tecnologia: " << s << endl;
					}
				}
			}

			if (fase == 4 && flag == 0) {
				flag = 1;
				srand(time(NULL));
				f_sorte = rand() % (3 + 1);
				cout << "Evento aleatorio; " << f_sorte << endl;
				cout << world.getEvento(f_sorte);
				flag_evento = world.fazEvento(f_sorte, ano);
				if (flag_evento == 3)
					cout << "Invasao concluida.\t Ultimo terrtiorio conquistado foi perdido." << endl;
			}
		
	} while (ano != 3 && turnos != 7 && flag_evento != 1);
	// Fim do jogo
	pontuacao = world.getPVitorias();
	cout << "Pontuacao Final: " << pontuacao << endl;

}




string Comandos::getEvento(int turnos) const{
	ostringstream oss;
	if (turnos == 1 || turnos == 5 || turnos == 9) 
		 oss << "Conquistar/Passar";
	if (turnos == 2 || turnos == 6 || turnos == 10) 
		oss << "Recolha de produtos e ouro";

	if (turnos == 3 || turnos == 7 || turnos == 11)
		oss << "Compra de unidades militares e tecnologia";

	if (turnos == 4 || turnos == 9 || turnos == 12)
		oss << "Fase de eventos";
	
	return oss.str();
}
