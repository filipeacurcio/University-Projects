package pt.isec.filipe.sandra.patricia

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.util.Log
import android.widget.GridLayout
import android.widget.TextView
import androidx.lifecycle.ViewModel
import java.util.*


class SinglePlayerViewModel : ViewModel() {


    var tabuleiro : ArrayList<String> = ArrayList()

    //MAX LEVEL 4
    var nivel = 1
    var CORRECT_EXPRESSIONS = 1
    var tempoLimite = 0
    var expressoesAcertadas = 0
    var linhaAcertada = 0
    var segundaLinhaMaior = 0
    var colunaAcertada = 0
    var segundaColunaMaior = 0
    var scorePessoal = 0
    var resultadoMaiorColuna = 0
    var segundoResultadoMaiorColuna = 0
    var segundoResultadoMaior = 0
    var resultadoMaior = 0

    var tempo : Long = 0
    var timerCreated : Boolean = false
    var mudaNivel: Boolean = false
    lateinit var yourCountDownTimer : CountDownTimer

    companion object{
        var TALEBULEIRO_TAM = 25
        var NUMBER_OF_OPERATORS  = 4
        var MAX_RANDOM_NUMBER1: Int  = 9
        var MAX_RANDOM_NUMBER2: Int  = 99
        var MAX_RANDOM_NUMBER3: Int  = 999
        var MAX_TIME1 : Int = 90000
        var MAX_TIME2 : Int = 60000
        var MAX_TIME3 : Int = 30000

    }



    @SuppressLint("SuspiciousIndentation")
    fun criaTabuleiro(){
        tabuleiro = ArrayList()
       val random = Random()
        for(i in 0 until TALEBULEIRO_TAM){
            if(i % 2 == 0){
                if(i == 6 || i == 8 || i == 16 || i == 18)
                    tabuleiro.add(" ")
                else{
                    when(nivel){
                        1 -> tabuleiro.add((random.nextInt(MAX_RANDOM_NUMBER1 - 1) + 1).toString())
                        2 -> tabuleiro.add((random.nextInt(MAX_RANDOM_NUMBER2 - 1) + 1).toString())
                        3 -> tabuleiro.add((random.nextInt(MAX_RANDOM_NUMBER3 - 1) + 1).toString())
                        4 -> tabuleiro.add((random.nextInt(MAX_RANDOM_NUMBER3 - 1) + 1).toString())
                    }
                }
            }else{
                when(nivel){
                    1 -> {
                        tabuleiro.add("+")
                    }
                    2 ->{
                        when(random.nextInt(NUMBER_OF_OPERATORS - 2)){
                            0 -> tabuleiro.add("+")
                            1 -> tabuleiro.add("-")
                        }
                    }
                    3 ->{
                        when(random.nextInt(NUMBER_OF_OPERATORS - 1)){
                            0 -> tabuleiro.add("+")
                            1 -> tabuleiro.add("-")
                            2 -> tabuleiro.add("*")
                        }
                    }
                    4->{
                        when(random.nextInt(NUMBER_OF_OPERATORS)){
                            0 -> tabuleiro.add("+")
                            1 -> tabuleiro.add("-")
                            2 -> tabuleiro.add("*")
                            3 ->tabuleiro.add("/")
                        }
                    }
                }
            }
        }

        when(nivel){
            1 -> tempoLimite = MAX_TIME1
            2 -> tempoLimite = MAX_TIME2
            3 -> tempoLimite = MAX_TIME3
            4 -> tempoLimite = MAX_TIME3
        }
    }


    //1 se for a linha maior 0 se for a coluna
    fun verificaLinhaColunaMaior() : Int{
        return if(verificaLinhaMaior() > verificaColunaMaior())
            1
        else
            0
    }




    private fun verificaLinhaMaior() : Int{
        var resultado: Int
        var pos : Int
        resultadoMaior = 0
        segundoResultadoMaior = 0


        for(i in 0 until 5){
                pos = i * 5
                resultado = 0
                if(i != 1 && i != 3){

            if((tabuleiro[pos + 1] == "+" || tabuleiro[pos + 1] == "-" ) && (tabuleiro[pos + 3] == "*" || tabuleiro[pos + 3] == "/")){

                when (tabuleiro[pos + 3]){
                    "*" -> resultado = tabuleiro[pos + 2].toInt() * tabuleiro[pos + 4].toInt()
                    "/" -> resultado = tabuleiro[pos + 2].toInt() / tabuleiro[pos + 4].toInt()
                }

                when(tabuleiro[pos + 1]){
                    "+" -> resultado += tabuleiro[pos].toInt()
                    "-" -> resultado = tabuleiro[pos].toInt() - resultado
                }

            }else if( (tabuleiro[pos + 1] == "*" || tabuleiro[pos + 1] == "/") &&
                (tabuleiro[pos + 3] == "+" || tabuleiro[pos + 3] == "-")){

                when (tabuleiro[pos + 1]){
                    "*" -> resultado = tabuleiro[pos + 0].toInt() * tabuleiro[pos + 2].toInt()
                    "/" -> resultado = tabuleiro[pos + 0].toInt() / tabuleiro[pos + 2].toInt()
                }

                when(tabuleiro[pos + 3]){
                    "+" -> resultado += tabuleiro[pos + 4].toInt()
                    "-" -> resultado  -= tabuleiro[pos + 4].toInt()
                }

            }
            else if(tabuleiro[pos + 1] == "+" && tabuleiro[pos + 3] == "+"){
                    resultado = tabuleiro[pos + 0].toInt() + tabuleiro[pos + 2].toInt() + tabuleiro[pos + 4].toInt()

                }
                 else if(tabuleiro[pos + 1] == "-" && tabuleiro[pos + 3] == "-"){
                    resultado = tabuleiro[pos + 0].toInt() - tabuleiro[pos + 2].toInt() - tabuleiro[pos + 4].toInt()

                }
                else if(tabuleiro[pos + 1] == "+" && tabuleiro[pos + 3] == "-"){
                    resultado = tabuleiro[pos + 0].toInt() + tabuleiro[pos + 2].toInt() - tabuleiro[pos + 4].toInt()

                }
                else if(tabuleiro[pos + 1] == "-" && tabuleiro[pos + 3] == "+"){
                    resultado = tabuleiro[pos + 0].toInt() - tabuleiro[pos + 2].toInt() + tabuleiro[pos + 4].toInt()

                }
                else if(tabuleiro[pos + 1] == "*" && tabuleiro[pos + 3] == "*"){
                    resultado = tabuleiro[pos + 0].toInt() * tabuleiro[pos + 2].toInt() * tabuleiro[pos + 4].toInt()

                }
                else if(tabuleiro[pos + 1] == "*" && tabuleiro[pos + 3] == "/"){
                    resultado = tabuleiro[pos + 0].toInt() * tabuleiro[pos + 2].toInt() / tabuleiro[pos + 4].toInt()

                }
                else if(tabuleiro[pos + 1] == "/" && tabuleiro[pos + 3] == "*"){
                    resultado = tabuleiro[pos + 0].toInt() / tabuleiro[pos + 2].toInt() * tabuleiro[pos + 4].toInt()

                }
                else if(tabuleiro[pos + 1] == "/" && tabuleiro[pos + 3] == "/"){
                    resultado = tabuleiro[pos + 0].toInt() / tabuleiro[pos + 2].toInt() / tabuleiro[pos + 4].toInt()

                }
                if(resultado > resultadoMaior){
                    segundoResultadoMaior = resultadoMaior
                    resultadoMaior = resultado
                    linhaAcertada = i
                }else if (resultado > segundoResultadoMaior){
                    segundoResultadoMaior  = resultado
                    segundaLinhaMaior = i
                }

            }

            }


        Log.i("TAG", "verificaLinhaVencedora: $linhaAcertada Resultado : $resultadoMaior")
        Log.i("TAG", "Primeira linha vencedora  $resultadoMaior Segundo linha _: $segundoResultadoMaior")

        return resultadoMaior
    }

    private fun verificaColunaMaior() : Int{
        var resultado: Int

        var pos : Int
        resultadoMaiorColuna = 0
        segundoResultadoMaiorColuna = 0

        for(i in 0 until 5){
            pos = i
            resultado = 0
            if(i != 1 && i != 3){

                if((tabuleiro[pos + 1 * 5] == "+" || tabuleiro[pos + 1 * 5] == "-" ) && (tabuleiro[pos + 3 * 5] == "*" || tabuleiro[pos + 3 * 5] == "/")){

                    when (tabuleiro[pos + 3 * 5]){
                        "*" -> resultado = tabuleiro[pos + 2 * 5].toInt() * tabuleiro[pos + 4 * 5].toInt()
                        "/" -> resultado = tabuleiro[pos + 2 * 5].toInt() / tabuleiro[pos + 4 * 5].toInt()
                    }

                    when(tabuleiro[pos + 1 * 5]){
                        "+" -> resultado += tabuleiro[pos].toInt()
                        "-" -> resultado = tabuleiro[pos].toInt() - resultado
                    }

                }else if( (tabuleiro[pos + 1 * 5] == "*" || tabuleiro[pos + 1 * 5] == "/") &&
                    (tabuleiro[pos + 3 * 5] == "+" || tabuleiro[pos + 3 * 5] == "-")){

                    when (tabuleiro[pos + 1 * 5]){
                        "*" -> resultado = tabuleiro[pos + 0 * 5].toInt() * tabuleiro[pos + 2 * 5].toInt()
                        "/" -> resultado = tabuleiro[pos + 0 * 5].toInt() / tabuleiro[pos + 2 * 5].toInt()
                    }

                    when(tabuleiro[pos + 3 * 5]){
                        "+" -> resultado += tabuleiro[pos + 4 * 5].toInt()
                        "-" -> resultado  -= tabuleiro[pos + 4 * 5].toInt()
                    }

                }
                else if(tabuleiro[pos + 1 * 5] == "+" && tabuleiro[pos + 3 * 5] == "+"){
                    resultado = tabuleiro[pos + 0 * 5].toInt() + tabuleiro[pos + 2 * 5].toInt() + tabuleiro[pos + 4 * 5].toInt()

                }
                else if(tabuleiro[pos + 1 * 5] == "-" && tabuleiro[pos + 3 * 5] == "-"){
                    resultado = tabuleiro[pos + 0 * 5].toInt() - tabuleiro[pos + 2 * 5].toInt() - tabuleiro[pos + 4 * 5].toInt()

                }
                else if(tabuleiro[pos + 1 * 5] == "+" && tabuleiro[pos + 3 * 5] == "-"){
                    resultado = tabuleiro[pos + 0 * 5].toInt() + tabuleiro[pos + 2 * 5].toInt() - tabuleiro[pos + 4 * 5].toInt()

                }
                else if(tabuleiro[pos + 1 * 5] == "-" && tabuleiro[pos + 3 * 5] == "+"){
                    resultado = tabuleiro[pos + 0 * 5].toInt() - tabuleiro[pos + 2 * 5].toInt() + tabuleiro[pos + 4 * 5].toInt()

                }
                else if(tabuleiro[pos + 1 * 5] == "*" && tabuleiro[pos + 3 * 5] == "*"){
                    resultado = tabuleiro[pos + 0 * 5].toInt() * tabuleiro[pos + 2 * 5].toInt() * tabuleiro[pos + 4 * 5].toInt()

                }
                else if(tabuleiro[pos + 1 * 5] == "*" && tabuleiro[pos + 3 * 5] == "/"){
                    resultado = tabuleiro[pos + 0 * 5].toInt() * tabuleiro[pos + 2 * 5].toInt() / tabuleiro[pos + 4 * 5].toInt()

                }
                else if(tabuleiro[pos + 1 * 5] == "/" && tabuleiro[pos + 3 * 5] == "*"){
                    resultado = tabuleiro[pos + 0 * 5].toInt() / tabuleiro[pos + 2 * 5].toInt() * tabuleiro[pos + 4 * 5].toInt()

                }
                else if(tabuleiro[pos + 1 * 5] == "/" && tabuleiro[pos + 3 * 5] == "/"){
                    resultado = tabuleiro[pos + 0 * 5].toInt() / tabuleiro[pos + 2 * 5].toInt() / tabuleiro[pos + 4 * 5].toInt()

                }
                if(resultado > resultadoMaiorColuna){
                    segundoResultadoMaiorColuna = resultadoMaiorColuna
                    resultadoMaiorColuna = resultado
                    colunaAcertada = i
                } else if (resultado > segundoResultadoMaiorColuna){
                    segundoResultadoMaiorColuna  = resultado
                    segundaColunaMaior = i
                }
            }
        }


        Log.i("TAG", "verificaColunaVencedora: $colunaAcertada  Resultado : $resultadoMaiorColuna")
        Log.i("TAG", "Primeira Coluna vencedora  " + resultadoMaiorColuna + " Segunda Coluna _: " + segundoResultadoMaiorColuna)
        return resultadoMaiorColuna
    }

    fun resetGame(mLayout: GridLayout){
        nivel = 1
        criaTabuleiro()
        fillTable(mLayout)
    }

    fun updateVista(mLayout: GridLayout){
        criaTabuleiro()
        fillTable(mLayout)
    }

    fun updateVistaUmPonto(mLayout: GridLayout, tvMyWins: TextView?){
        scorePessoal += 1
        tvMyWins!!.text = scorePessoal.toString()
        criaTabuleiro()
        fillTable(mLayout)
    }


    fun updateVistaAcertada(mLayout: GridLayout, tvMyWins: TextView?){
        scorePessoal += 2
        tvMyWins!!.text = scorePessoal.toString()
        mudancaNivel()
        criaTabuleiro()
        fillTable(mLayout)

    }


    fun mudancaNivel() {
        if(expressoesAcertadas == CORRECT_EXPRESSIONS - 1 && nivel < 4){
            nivel++
            expressoesAcertadas = 0
            mudaNivel = true
            tempo = tempoLimite.toLong()

        }
    }


    fun fillTable(mLayout: GridLayout){
        verificaLinhaColunaMaior()

        for(i in 0 until tabuleiro.size) run {
            val child: TextView = mLayout.getChildAt(i) as TextView
            child.text = tabuleiro[i]
        }
    }




}