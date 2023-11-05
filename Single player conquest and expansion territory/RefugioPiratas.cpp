#include "RefugioPiratas.h"
#include "Imperio.h"
int RefugioPiratas::conta = 1;

RefugioPiratas::RefugioPiratas():Ilhas("RefugiodosPiratas", 9, 0, 1, conta++){
}
bool RefugioPiratas::isConquistavel(Imperio& impe) {
	if (impe.verificaTecTerr() == true) {
		return true;
	}
	else
		return false;
}

