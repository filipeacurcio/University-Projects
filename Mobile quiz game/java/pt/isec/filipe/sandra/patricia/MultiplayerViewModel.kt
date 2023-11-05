package pt.isec.filipe.sandra.patricia

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.util.Log
import android.widget.GridLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import pt.isec.filipe.sandra.patricia.databinding.ActivityMultiplayerGameBinding
import java.io.*
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.math.log

class MultiplayerViewModel : ViewModel() {

    companion object{
        const val SERVER_PORT = 9999
            var TALEBULEIRO_TAM = 25
            var NUMBER_OF_OPERATORS  = 4
            var MAX_RANDOM_NUMBER1: Int  = 9
            var MAX_RANDOM_NUMBER2: Int  = 99
            var MAX_RANDOM_NUMBER3: Int  = 999
            var MAX_TIME1 : Int = 90000
            var MAX_TIME2 : Int = 60000
            var MAX_TIME3 : Int = 30000

    }

    var tabuleiro : ArrayList<String> = ArrayList()

    var todosTabuleiros: ArrayList<ArrayList<String>> = ArrayList()

    //var clientesTentativas : HashMap<Int, Int> = HashMap()

    var clienteTentativas : Int = 0
    var pontuacaoCliente : Int = 0


    enum class ConnectionState {
        SETTING_PARAMETERS, SERVER_CONNECTING, CLIENT_CONNECTING, CONNECTION_ESTABLISHED,
        CONNECTION_ERROR, CONNECTION_ENDED
    }

    enum class State {
        STARTING, PLAYER_CORRECT, ONLY_PLAY_SERVER,PLAYING_PLAYER, NEXT_ROUND_ME, NEXT_ROUND_OTHER, NEXT_ROUND_INIT, ROUND_ENDED, GAME_OVER
    }

    private val _connectionState = MutableLiveData(ConnectionState.SETTING_PARAMETERS)
    val connectionState : LiveData<ConnectionState>
        get() = _connectionState

    private val _state = MutableLiveData(State.STARTING)
    val state : LiveData<State>
        get() = _state


    private var socket: Socket? = null
    private val socketI: InputStream?
        get() = socket?.getInputStream()
    private val socketO: OutputStream?
        get() = socket?.getOutputStream()

    private var serverSocket: ServerSocket? = null

    private var threadComm: Thread? = null
    var servidorPerdeu  = false
    var clientePerdeu  = false
     var fotoCliente : String = ""
     var fotoServidor : String = ""
    var tempoLimite = 0
    var expressoesAcertadas = 0
    var expressoesAcertadasCliente = 0
    var nivel = 1
    private lateinit var gridLayout : GridLayout
    lateinit var binding: ActivityMultiplayerGameBinding
    var linhaAcertada = 0
    var segundaLinhaMaior = 0
    var colunaAcertada = 0
    var segundaColunaMaior = 0
    var scorePessoal = 0
    var tentativasServidor = 0
    var timerCreated : Boolean = false
    var tempo : Long = 0
    var tempoCliente : Long = 0
    lateinit var yourCountDownTimer : CountDownTimer
    lateinit var clienteyourCountDownTimer : CountDownTimer
    var mudaNivel: Boolean = false
    var mudaNivelCliente: Boolean = false
    var CORRECT_EXPRESSIONS = 5
    var resultadoMaiorColuna = 0
    var segundoResultadoMaiorColuna = 0
    var segundoResultadoMaior = 0
    var resultadoMaior = 0
    var eliminado = false



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
        todosTabuleiros.add(tabuleiro)
    }

    fun setBindingClient(novoBinding: ActivityMultiplayerGameBinding){
        binding = novoBinding
        gridLayout = binding.gdTable
    }


    fun startClient(serverIP: String,serverPort: Int = SERVER_PORT, gridLayout: GridLayout, binding: ActivityMultiplayerGameBinding) {
        if (socket != null || _connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return
        this.gridLayout = gridLayout
        this.binding = binding

        thread {
            _connectionState.postValue(ConnectionState.CLIENT_CONNECTING)
            try {
                //val newsocket = Socket(serverIP, serverPort)
                val newsocket = Socket()
                newsocket.connect(InetSocketAddress(serverIP,serverPort),5000)
                startPlayingClient(newsocket)
            } catch (_: Exception) {
                _connectionState.postValue(ConnectionState.CONNECTION_ERROR)
               stopGame()
            }
        }
    }

    fun enviarJogada(linhaOuCol: Int, linhaOuColEscolhida: Int,  orientacao : String) {

        thread {
            var jsonObjectClient= JSONObject()
            jsonObjectClient.put("orientacao", orientacao)
            jsonObjectClient.put("linhaOuCol", linhaOuCol)
            jsonObjectClient.put("linhaOuColEscolhida",linhaOuColEscolhida)
            jsonObjectClient.put("tabuleiro",JSONArray(tabuleiro))
            jsonObjectClient.put("fotoCliente", fotoCliente)
            socketO.run {
                val printStream = PrintStream(this)
                printStream.println(jsonObjectClient)
                printStream.flush()
                Log.i("TAG", "eviaJogada: $jsonObjectClient")
            }
        }
    }


    fun enviarJogada() {

        thread {
            var jsonObjectClient= JSONObject()
            jsonObjectClient.put("eliminado", eliminado)

            socketO.run {
                val printStream = PrintStream(this)
                printStream.println(jsonObjectClient)
                printStream.flush()
                Log.i("TAG", "eviaJogada: $jsonObjectClient")
            }
        }
    }



    fun startServer(gridLayout: GridLayout, binding: ActivityMultiplayerGameBinding) {

        if (serverSocket != null || socket != null ||
            _connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return
        _connectionState.postValue(ConnectionState.SERVER_CONNECTING)
        this.gridLayout = gridLayout
        this.binding = binding
        thread {
            serverSocket = ServerSocket(SERVER_PORT)
            serverSocket?.run {
                try {
                    val socketClient = serverSocket!!.accept()
                    //clientesTentativas[socketClient.localPort] = 0
                    startPlayingServer(socketClient)
                } catch (_: Exception) {
                    _connectionState.postValue(ConnectionState.CONNECTION_ERROR)
                } finally {
                    serverSocket?.close()
                    serverSocket = null
                }
            }
        }
    }

    fun stopServer() {
          serverSocket?.close()
        _connectionState.postValue(ConnectionState.CONNECTION_ENDED)
        serverSocket = null
    }

    @SuppressLint("SuspiciousIndentation")
    private fun startPlayingClient(newSocket :Socket){
        if (threadComm != null)
            return

        socket = newSocket

        threadComm = thread {
            try {
                    if (socketI == null)
                        return@thread

                while (_state.value != State.GAME_OVER){

                    _connectionState.postValue(ConnectionState.CONNECTION_ESTABLISHED)
                    val br = BufferedReader(InputStreamReader(socketI))
                    val jsonObjectClient = JSONTokener(br.readLine()).nextValue() as JSONObject

                    val tab = jsonObjectClient.optJSONArray("tabuleiro")
                    val terminouTempo = jsonObjectClient.optString("terminou")
                    tempoCliente = jsonObjectClient.optLong("tempo")
                    var scoreaux = jsonObjectClient.optInt("pontuacao")
                    var fimnivel = jsonObjectClient.optBoolean("fim")
                    var troca = jsonObjectClient.optString("trocanivel")
                    var top1 =  jsonObjectClient.optString("top1")
                    var top2 =  jsonObjectClient.optString("top2")
                    var top3 =  jsonObjectClient.optString("top3")
                    var top4 =  jsonObjectClient.optString("top4")
                    var top5 =  jsonObjectClient.optString("top5")
                    var servidorEliminado = jsonObjectClient.optBoolean("eliminado")
                    var foto = jsonObjectClient.optString("fotoServidor")

                    if(servidorEliminado){
                        servidorPerdeu = servidorEliminado
                    }

                    if(foto.isNotEmpty()){
                        fotoServidor = foto
                    }
                    if(top1.isNotEmpty())
                        binding.tvTop1.text = top1
                    if(top2.isNotEmpty())
                        binding.tvTop2.text = top2
                    if(top3.isNotEmpty())
                        binding.tvTop3.text = top3
                    if(top3.isNotEmpty())
                        binding.tvTop4.text = top4
                    if(top4.isNotEmpty())
                        binding.tvTop5.text = top5

                    Log.i("TAG", "VALORES: " + troca)

                    if(fimnivel)
                        _state.postValue(State.NEXT_ROUND_OTHER)

                    if(servidorPerdeu && troca == "muda")
                        _state.postValue(State.NEXT_ROUND_INIT)


                    if(troca == "muda" || servidorEliminado){
                        _state.postValue(State.NEXT_ROUND_INIT)
                    }

                    if(scoreaux != 0)
                    binding.tvMyWins.text = scoreaux.toString()

                    if(terminouTempo.isNotEmpty() && terminouTempo.equals("Acabou o Tempo")){
                        //_state.postValue(State.ROUND_ENDED)
                        enviarJogada()
                    }
                    if(terminouTempo.isNotEmpty() && terminouTempo.equals("nao muda")){
                        _state.postValue(State.NEXT_ROUND_ME)
                    }

                    if(tempoCliente.toString().length == 5){
                        binding.tvTimeLeft.text = tempoCliente.toString().subSequence(0,2)
                    }
                    else
                        binding.tvTimeLeft.text = tempoCliente.toString().subSequence(0,1)

                    if(tab != null){
                        tabuleiro = ArrayList()
                        for(i in 0 until tab.length()){
                            tabuleiro.add(tab.getString(i))
                        }
                        fillTable(gridLayout)
                    }


                }


            } catch (e: Exception) {
                Log.i("TAG", "startPlayingClient: EXCEPTCO $e")
            } finally {
                stopGame()
            }
        }
    }

    private fun startPlayingServer(newSocket :Socket){
        if (threadComm != null)
            return

        socket = newSocket

        fillTable(gridLayout)
        threadComm = thread {
            try {
                if (socketI == null)
                    return@thread

                var jsonObjectServidor = JSONObject()
                tempoCliente = tempoLimite.toLong()
                jsonObjectServidor.put("tabuleiro", JSONArray(tabuleiro))
                jsonObjectServidor.put("tempo",tempoCliente)
                jsonObjectServidor.put("top1", binding.tvTop1.text)
                jsonObjectServidor.put("top2", binding.tvTop2.text)
                jsonObjectServidor.put("top3", binding.tvTop3.text)
                jsonObjectServidor.put("top4", binding.tvTop4.text)
                jsonObjectServidor.put("top5", binding.tvTop5.text)
                jsonObjectServidor.put("fotoServidor",fotoServidor)
               // tempoClienteCount()


                _connectionState.postValue(ConnectionState.CONNECTION_ESTABLISHED)
                socketO.run {
                        val printStream = PrintStream(this)
                        Log.i("TAG", "startPlayingServer: $jsonObjectServidor")
                        printStream.println(jsonObjectServidor)
                        printStream.flush()
                    }

                Log.i("TAG", "fim output: ")
                    while(_state.value != State.GAME_OVER){
                        Log.i("TAG", "inicio input: ")
                        val br = BufferedReader(InputStreamReader(socketI))

                        jsonObjectServidor = JSONTokener(br.readLine()).nextValue() as JSONObject
                        val linhaOuCol = jsonObjectServidor.getInt("linhaOuCol")
                        val linhaOuColEscolhida = jsonObjectServidor.getInt("linhaOuColEscolhida")
                        val orientacao = jsonObjectServidor.getString("orientacao")
                        val tab = jsonObjectServidor.optJSONArray("tabuleiro")
                        val eliminadoClient = jsonObjectServidor.optBoolean("eliminado")
                        var foto = jsonObjectServidor.optString("fotoCliente")

                        if(eliminadoClient)
                            clientePerdeu = eliminadoClient

                        if(foto.isNotEmpty())
                            fotoCliente = foto

                        jsonObjectServidor = JSONObject()
                        clienteTentativas++
                        Log.i("TAG", "startPlayingServer: $tempoCliente")
                        //enviar resposta
                        var tabuleiroCliente : ArrayList<String> = ArrayList()

                        if (tab != null) {
                            for(i in 0 until tab.length()){
                                tabuleiroCliente.add(tab.getString(i))
                            }
                        }
                        verificaJogadaCliente(linhaOuCol , linhaOuColEscolhida, orientacao,tabuleiroCliente)
                        getTabuleiroCliente()
                        jsonObjectServidor.put("fim", mudaNivelCliente)
                        Log.i("TAG", "MUDA DE NOVEL: $mudaNivel + mudanivel Clinte : $mudaNivelCliente\n" +
                                "elminado: " + eliminado)

                        if(mudaNivelCliente && eliminado){
                            jsonObjectServidor.put("trocanivel", "muda")
                            mudaNivelCliente = false
                            _state.postValue(State.PLAYER_CORRECT)
                            nivel++
                        }

                        else if(mudaNivelCliente && mudaNivel){
                            Log.i("TAG", "MUDA DE NIVEL: ")
                            jsonObjectServidor.put("trocanivel", "muda")
                            mudaNivelCliente = false
                            mudaNivel = false
                        }
                        else{
                            jsonObjectServidor.put("trocanivel", "nao muda")
                        }

                        jsonObjectServidor.put("tabuleiro", JSONArray(tabuleiro))
                        jsonObjectServidor.put("tempo",tempoCliente)
                        jsonObjectServidor.put("pontuacao", pontuacaoCliente)

                        socketO.run {
                            val printStream = PrintStream(this)
                            Log.i("TAG", "Enviou para o cliente: $tabuleiro")
                            printStream.println(jsonObjectServidor)
                            printStream.flush()
                        }

                    }


            } catch (_: Exception) {

            } finally {
                stopGame()
                updateTopScoresFirestore()
            }
        }


    }


    fun stopGame() {
        try {
            _state.postValue(State.GAME_OVER)
            _connectionState.postValue(ConnectionState.CONNECTION_ERROR)
            socket?.close()
            socket = null
            threadComm?.interrupt()
            threadComm = null
            Log.i("TAG", "ACABOU O JOGO: ")
        } catch (_: Exception) { }
    }


     fun fillTable(mLayout: GridLayout){
         Log.i("TAG", "fillTable: ")
        for(i in 0 until tabuleiro.size) run {
            val child: TextView = mLayout.getChildAt(i) as TextView
            child.text = tabuleiro[i]
        }
    }

    fun verificaJogadaCliente(
        linhaOuCol: Int,
        linhaOuColEscolhida: Int,
        orientacao: String,
        tabuleiroCliente: ArrayList<String>,
    ) {
        _state.postValue(State.PLAYING_PLAYER)
        Log.i("TAG", "verificaJogadaCliente: $orientacao")
        val linhaOrColuna: Int = if(verificaLinhaColunaMaiorCliente(tabuleiroCliente) == 1){
            1
        }else {
            0
        }
        val linhaouColSegundo : Int = if(linhaOrColuna== 1){
            if(segundoResultadoMaior > resultadoMaiorColuna)
                1
            else
                0
        }else{
            if(segundoResultadoMaiorColuna > resultadoMaior)
                0
            else
                1
        }

        if(orientacao == "vertical"){
            if (linhaOuColEscolhida == colunaAcertada) {
                if (linhaOrColuna == 0) {
                    pontuacaoCliente+= 2
                    if((tempoCliente + 5000) < tempoLimite)
                        tempoCliente += 5000
                    if((tempoCliente + 5000) > tempoLimite)
                        tempoCliente = tempoLimite.toLong()
                    expressoesAcertadasCliente++
                    mudancaNivelCliente()
                }
            }
            if (linhaOuColEscolhida == colunaAcertada  && linhaouColSegundo == 0) {
                pontuacaoCliente++
            }
        }

        if(orientacao == "horizontal"){
            if (linhaOuColEscolhida == linhaAcertada) {
                if (linhaOrColuna == 1) {
                    pontuacaoCliente+= 2
                    if((tempoCliente + 5000) < tempoLimite)
                        tempoCliente += 5000
                    if((tempoCliente + 5000) > tempoLimite)
                        tempoCliente = tempoLimite.toLong()

                    expressoesAcertadasCliente++
                    mudancaNivelCliente()
                }
            }
            if (linhaOuColEscolhida == linhaAcertada  && linhaouColSegundo == 1) {
                pontuacaoCliente++
            }
        }
    }






    //1 se for a linha maior 0 se for a coluna
    fun verificaLinhaColunaMaior() : Int{
        Log.i("TAG", "COLUNA MAIOR LINHA SERVIDOR: " + todosTabuleiros[tentativasServidor])
        return if(verificaLinhaMaiorCliente(todosTabuleiros[tentativasServidor]) > verificaColunaMaiorCliente(todosTabuleiros[tentativasServidor]) )
            1
        else if (verificaLinhaMaiorCliente(todosTabuleiros[tentativasServidor])  < verificaColunaMaiorCliente(todosTabuleiros[tentativasServidor]) )
            0
        else
            1

    }


    //1 se for a linha maior 0 se for a coluna
    fun verificaLinhaColunaMaiorCliente(tabuleiroCliente: ArrayList<String>): Int{
        return if(verificaLinhaMaiorCliente(tabuleiroCliente) > verificaColunaMaiorCliente(tabuleiroCliente))
            1
        else if (verificaLinhaMaiorCliente(tabuleiroCliente) < verificaColunaMaiorCliente(
                tabuleiroCliente
            )
        )
            0
        else
            1

    }


    private fun verificaLinhaMaiorCliente(tabuleiroCliente: ArrayList<String>): Int{
        var resultado: Int
        var pos : Int
        resultadoMaior = 0
        segundoResultadoMaior = 0


        for(i in 0 until 5){
            pos = i * 5
            resultado = 0
            if(i != 1 && i != 3){

                if((tabuleiroCliente[pos + 1] == "+" || tabuleiroCliente[pos + 1] == "-" ) && (tabuleiroCliente[pos + 3] == "*" || tabuleiroCliente[pos + 3] == "/")){

                    when (tabuleiroCliente[pos + 3]){
                        "*" -> resultado = tabuleiroCliente[pos + 2].toInt() * tabuleiroCliente[pos + 4].toInt()
                        "/" -> resultado = tabuleiroCliente[pos + 2].toInt() / tabuleiroCliente[pos + 4].toInt()
                    }

                    when(tabuleiroCliente[pos + 1]){
                        "+" -> resultado += tabuleiroCliente[pos].toInt()
                        "-" -> resultado = tabuleiroCliente[pos].toInt() - resultado
                    }

                }else if( (tabuleiroCliente[pos + 1] == "*" || tabuleiroCliente[pos + 1] == "/") &&
                    (tabuleiroCliente[pos + 3] == "+" || tabuleiroCliente[pos + 3] == "-")){

                    when (tabuleiroCliente[pos + 1]){
                        "*" -> resultado = tabuleiroCliente[pos + 0].toInt() * tabuleiroCliente[pos + 2].toInt()
                        "/" -> resultado = tabuleiroCliente[pos + 0].toInt() / tabuleiroCliente[pos + 2].toInt()
                    }

                    when(tabuleiroCliente[pos + 3]){
                        "+" -> resultado += tabuleiroCliente[pos + 4].toInt()
                        "-" -> resultado  -= tabuleiroCliente[pos + 4].toInt()
                    }

                }
                else if(tabuleiroCliente[pos + 1] == "+" && tabuleiroCliente[pos + 3] == "+"){
                    resultado = tabuleiroCliente[pos + 0].toInt() + tabuleiroCliente[pos + 2].toInt() + tabuleiroCliente[pos + 4].toInt()

                }
                else if(tabuleiroCliente[pos + 1] == "-" && tabuleiroCliente[pos + 3] == "-"){
                    resultado = tabuleiroCliente[pos + 0].toInt() - tabuleiroCliente[pos + 2].toInt() - tabuleiroCliente[pos + 4].toInt()

                }
                else if(tabuleiroCliente[pos + 1] == "+" && tabuleiroCliente[pos + 3] == "-"){
                    resultado = tabuleiroCliente[pos + 0].toInt() + tabuleiroCliente[pos + 2].toInt() - tabuleiroCliente[pos + 4].toInt()

                }
                else if(tabuleiroCliente[pos + 1] == "-" && tabuleiroCliente[pos + 3] == "+"){
                    resultado = tabuleiroCliente[pos + 0].toInt() - tabuleiroCliente[pos + 2].toInt() + tabuleiroCliente[pos + 4].toInt()

                }
                else if(tabuleiroCliente[pos + 1] == "*" && tabuleiroCliente[pos + 3] == "*"){
                    resultado = tabuleiroCliente[pos + 0].toInt() * tabuleiroCliente[pos + 2].toInt() * tabuleiroCliente[pos + 4].toInt()

                }
                else if(tabuleiroCliente[pos + 1] == "*" && tabuleiroCliente[pos + 3] == "/"){
                    resultado = tabuleiroCliente[pos + 0].toInt() * tabuleiroCliente[pos + 2].toInt() / tabuleiroCliente[pos + 4].toInt()

                }
                else if(tabuleiroCliente[pos + 1] == "/" && tabuleiroCliente[pos + 3] == "*"){
                    resultado = tabuleiroCliente[pos + 0].toInt() / tabuleiroCliente[pos + 2].toInt() * tabuleiroCliente[pos + 4].toInt()

                }
                else if(tabuleiroCliente[pos + 1] == "/" && tabuleiroCliente[pos + 3] == "/"){
                    resultado = tabuleiroCliente[pos + 0].toInt() / tabuleiroCliente[pos + 2].toInt() / tabuleiroCliente[pos + 4].toInt()

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



    private fun verificaColunaMaiorCliente(tabuleiroCliente: ArrayList<String>): Int{
        var resultado: Int

        var pos : Int
        resultadoMaiorColuna = 0
        segundoResultadoMaiorColuna = 0

        for(i in 0 until 5){
            pos = i
            resultado = 0
            if(i != 1 && i != 3){

                if((tabuleiroCliente[pos + 1 * 5] == "+" || tabuleiroCliente[pos + 1 * 5] == "-" ) && (tabuleiroCliente[pos + 3 * 5] == "*" || tabuleiroCliente[pos + 3 * 5] == "/")){

                    when (tabuleiroCliente[pos + 3 * 5]){
                        "*" -> resultado = tabuleiroCliente[pos + 2 * 5].toInt() * tabuleiroCliente[pos + 4 * 5].toInt()
                        "/" -> resultado = tabuleiroCliente[pos + 2 * 5].toInt() / tabuleiroCliente[pos + 4 * 5].toInt()
                    }

                    when(tabuleiroCliente[pos + 1 * 5]){
                        "+" -> resultado += tabuleiroCliente[pos].toInt()
                        "-" -> resultado = tabuleiroCliente[pos].toInt() - resultado
                    }

                }else if( (tabuleiroCliente[pos + 1 * 5] == "*" || tabuleiroCliente[pos + 1 * 5] == "/") &&
                    (tabuleiroCliente[pos + 3 * 5] == "+" || tabuleiroCliente[pos + 3 * 5] == "-")){

                    when (tabuleiroCliente[pos + 1 * 5]){
                        "*" -> resultado = tabuleiroCliente[pos + 0 * 5].toInt() * tabuleiroCliente[pos + 2 * 5].toInt()
                        "/" -> resultado = tabuleiroCliente[pos + 0 * 5].toInt() / tabuleiroCliente[pos + 2 * 5].toInt()
                    }

                    when(tabuleiroCliente[pos + 3 * 5]){
                        "+" -> resultado += tabuleiroCliente[pos + 4 * 5].toInt()
                        "-" -> resultado  -= tabuleiroCliente[pos + 4 * 5].toInt()
                    }

                }
                else if(tabuleiroCliente[pos + 1 * 5] == "+" && tabuleiroCliente[pos + 3 * 5] == "+"){
                    resultado = tabuleiroCliente[pos + 0 * 5].toInt() + tabuleiroCliente[pos + 2 * 5].toInt() + tabuleiroCliente[pos + 4 * 5].toInt()

                }
                else if(tabuleiroCliente[pos + 1 * 5] == "-" && tabuleiroCliente[pos + 3 * 5] == "-"){
                    resultado = tabuleiroCliente[pos + 0 * 5].toInt() - tabuleiroCliente[pos + 2 * 5].toInt() - tabuleiroCliente[pos + 4 * 5].toInt()

                }
                else if(tabuleiroCliente[pos + 1 * 5] == "+" && tabuleiroCliente[pos + 3 * 5] == "-"){
                    resultado = tabuleiroCliente[pos + 0 * 5].toInt() + tabuleiroCliente[pos + 2 * 5].toInt() - tabuleiroCliente[pos + 4 * 5].toInt()

                }
                else if(tabuleiroCliente[pos + 1 * 5] == "-" && tabuleiroCliente[pos + 3 * 5] == "+"){
                    resultado = tabuleiroCliente[pos + 0 * 5].toInt() - tabuleiroCliente[pos + 2 * 5].toInt() + tabuleiroCliente[pos + 4 * 5].toInt()

                }
                else if(tabuleiroCliente[pos + 1 * 5] == "*" && tabuleiroCliente[pos + 3 * 5] == "*"){
                    resultado = tabuleiroCliente[pos + 0 * 5].toInt() * tabuleiroCliente[pos + 2 * 5].toInt() * tabuleiroCliente[pos + 4 * 5].toInt()

                }
                else if(tabuleiroCliente[pos + 1 * 5] == "*" && tabuleiroCliente[pos + 3 * 5] == "/"){
                    resultado = tabuleiroCliente[pos + 0 * 5].toInt() * tabuleiroCliente[pos + 2 * 5].toInt() / tabuleiroCliente[pos + 4 * 5].toInt()

                }
                else if(tabuleiroCliente[pos + 1 * 5] == "/" && tabuleiroCliente[pos + 3 * 5] == "*"){
                    resultado = tabuleiroCliente[pos + 0 * 5].toInt() / tabuleiroCliente[pos + 2 * 5].toInt() * tabuleiroCliente[pos + 4 * 5].toInt()

                }
                else if(tabuleiroCliente[pos + 1 * 5] == "/" && tabuleiroCliente[pos + 3 * 5] == "/"){
                    resultado = tabuleiroCliente[pos + 0 * 5].toInt() / tabuleiroCliente[pos + 2 * 5].toInt() / tabuleiroCliente[pos + 4 * 5].toInt()

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


    fun updateVistaAcertada(mLayout: GridLayout, tvMyWins: TextView?){
        scorePessoal += 2
        tvMyWins!!.text = scorePessoal.toString()
        mudancaNivel()
        getTabuleiro()

        fillTable(mLayout)

    }


    fun mudancaNivelCliente(){
        if(expressoesAcertadasCliente == CORRECT_EXPRESSIONS  && nivel < 4 && !mudaNivelCliente && !mudaNivel){
            pontuacaoCliente+=5
            mudaNivelCliente = true
            clienteyourCountDownTimer.cancel()
            _state.postValue(State.NEXT_ROUND_ME)
        }
        if(expressoesAcertadasCliente == CORRECT_EXPRESSIONS  && nivel < 4 && !mudaNivelCliente){
            mudaNivelCliente = true
            clienteyourCountDownTimer.cancel()
        }
        if(expressoesAcertadasCliente == CORRECT_EXPRESSIONS  && nivel < 4){
            expressoesAcertadasCliente = 0
            _state.postValue(State.NEXT_ROUND_ME)


        }
        if(mudaNivelCliente && mudaNivel){
            Log.i("TAG", "mudancaNivelCliente: ")
            tempo = tempoLimite.toLong()
            _state.postValue(State.NEXT_ROUND_INIT)
        }
    }

    fun mudancaNivel() {
        Log.i("BANAN", "expressoes Acertadas: $expressoesAcertadas\n" +
                "nivel : $nivel\nMuda Nivel Server $mudaNivel\nMuda Nivel Client $mudaNivelCliente\ncliente perderu : $clientePerdeu" )
        if(expressoesAcertadas == CORRECT_EXPRESSIONS - 1 && nivel < 4 && !mudaNivel && clientePerdeu){
            scorePessoal += 5
            binding.tvMyWins.text = scorePessoal.toString()
            mudaNivel = true
            _state.postValue(State.NEXT_ROUND_INIT)
        }
        else if(expressoesAcertadas == CORRECT_EXPRESSIONS - 1 && nivel < 4 && !mudaNivel && mudaNivelCliente){
            binding.tvMyWins.text = scorePessoal.toString()
            enviaFimNivelCliente()
            _state.postValue(State.NEXT_ROUND_INIT)
        }
        else if(expressoesAcertadas == CORRECT_EXPRESSIONS - 1 && nivel < 4 && !mudaNivel && !mudaNivelCliente ){
            scorePessoal += 5
            binding.tvMyWins.text = scorePessoal.toString()
            mudaNivel = true
            _state.postValue(State.NEXT_ROUND_OTHER)
        }
        else if(expressoesAcertadas == CORRECT_EXPRESSIONS - 1 && nivel < 4 && !mudaNivel){
            binding.tvMyWins.text = scorePessoal.toString()
            mudaNivel = true
            //_state.postValue(State.NEXT_ROUND_OTHER)
        }

        else if(expressoesAcertadas == CORRECT_EXPRESSIONS - 1 && nivel < 4){
            expressoesAcertadas = 0
            tempo = tempoLimite.toLong()
        }
        else if(mudaNivelCliente && mudaNivel){
            Log.i("banana", "mudancaNivel: ")
            tempo = tempoLimite.toLong()
            _state.postValue(State.NEXT_ROUND_INIT)
        }

        expressoesAcertadas++
    }

    fun updateVista(mLayout: GridLayout){
        getTabuleiro()
        fillTable(mLayout)
    }

    fun updateVistaUmPonto(mLayout: GridLayout, tvMyWins: TextView?){
        scorePessoal += 1
        tvMyWins!!.text = scorePessoal.toString()
        getTabuleiro()
        fillTable(mLayout)
    }

    fun resetGame(mLayout: GridLayout){
        nivel = 1
        criaTabuleiro()
        fillTable(mLayout)
    }

    fun getTabuleiro(){
        if(todosTabuleiros.size  - 1 == tentativasServidor){
            criaTabuleiro()
        }
        else if(todosTabuleiros.size  - 1 > tentativasServidor){
            tabuleiro = todosTabuleiros[tentativasServidor + 1]
        }
    }

    fun getTabuleiroCliente() {
        Log.i("TAG", "TENTATIAS CLIENTE $clienteTentativas E  SIZE" + todosTabuleiros.size)
        if(todosTabuleiros.size == clienteTentativas){
            criaTabuleiro()
        }
        else if(todosTabuleiros.size  > clienteTentativas){
            tabuleiro = todosTabuleiros[clienteTentativas]

        }
    }


    fun enviaFimNivelCliente(){
        Log.i("TAG", "enviafinivel: ")
        val jsonObjectServidor = JSONObject()
        jsonObjectServidor.put("trocanivel", "muda")


        thread {
            socketO.run {
                val printStream = PrintStream(this)
                printStream.println(jsonObjectServidor)
                printStream.flush()
            }
        }
    }
    fun enviaTempoCliente(){
        val jsonObjectServidor = JSONObject()
        jsonObjectServidor.put("tempo",tempoCliente)
        jsonObjectServidor.put("top1", binding.tvTop1.text)
        jsonObjectServidor.put("top2", binding.tvTop2.text)
        jsonObjectServidor.put("top3", binding.tvTop3.text)
        jsonObjectServidor.put("top4", binding.tvTop4.text)
        jsonObjectServidor.put("top5", binding.tvTop5.text)
        jsonObjectServidor.put("pontuacao", pontuacaoCliente)
        jsonObjectServidor.put("fotoServidor",fotoServidor)

        if(tempoCliente < 1000){
            jsonObjectServidor.put("terminou", "Acabou o Tempo")
            if(mudaNivel && !mudaNivelCliente){
                _state.postValue(State.ONLY_PLAY_SERVER)
            }
        }
        thread {
            socketO.run {
                val printStream = PrintStream(this)
                printStream.println(jsonObjectServidor)
                printStream.flush()
            }
        }
    }

    fun enviaEliminadoCliente(){
        val jsonObjectServidor = JSONObject()
        clienteTentativas++
        getTabuleiroCliente()
        jsonObjectServidor.put("tabuleiro",JSONArray(tabuleiro))
        jsonObjectServidor.put("eliminado",true)
        clienteTentativas--
        thread {
            socketO.run {
                val printStream = PrintStream(this)
                printStream.println(jsonObjectServidor)
                printStream.flush()
            }
        }
    }

    fun clienteMudaNivel(){
        _state.postValue(State.PLAYER_CORRECT)
        val jsonObjectServidor = JSONObject()
        jsonObjectServidor.put("tabuleiro",JSONArray(tabuleiro))
        thread {
            socketO.run {
                val printStream = PrintStream(this)
                printStream.println(jsonObjectServidor)
                printStream.flush()
            }
        }
    }



    fun updateTopTimerFirestore(top : String, tempoTop: Long){
        val db = Firebase.firestore
        val v  = db.collection("Timers").document("TopTimersMP")
        v.get(Source.SERVER)
            .addOnSuccessListener {
                val exists = it.exists()
                Log.i("TAG", "updateDataInFirestore: Success? $exists")
                if (!exists)
                    return@addOnSuccessListener

                when (top) {
                    "top1" -> {
                        v.update("top1", tempoTop)
                    }
                    "top2" -> {
                        v.update("top2",tempoTop)
                    }
                    "top3" -> {
                        v.update("top3", tempoTop)
                    }
                    "top4" -> {
                        v.update("top4",tempoTop)
                    }
                    "top5" -> {
                        v.update("top5",tempoTop)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.i("TAG", "updateDataInFirestore: ${e.message}")
            }

    }



    fun updateTopScoresFirestore(){
        val db = Firebase.firestore
        val v  = db.collection("Scores").document("TopScoresMP")

        v.get(Source.SERVER)
            .addOnSuccessListener {
                val exists = it.exists()
                Log.i("TAG", "updateDataInFirestore: Success? $exists")
                if (!exists)
                    return@addOnSuccessListener

                var value= it.getLong("top1") ?: 0

                if(value < scorePessoal && scorePessoal > pontuacaoCliente){
                    v.update("top1",scorePessoal)
                    updateTopTimerFirestore("top1", tempo)
                    return@addOnSuccessListener
                }else if(pontuacaoCliente > value){
                    v.update("top1",pontuacaoCliente)
                    updateTopTimerFirestore("top1",tempoCliente)
                    return@addOnSuccessListener
                }
                if(value == scorePessoal.toLong()){
                    v.update("top1",scorePessoal)
                    updateTimerScoreEquals("top1")
                    return@addOnSuccessListener
                }
                if(value == pontuacaoCliente.toLong()){
                    v.update("top1",pontuacaoCliente)
                    updateTimerScoreEquals("top1")
                    return@addOnSuccessListener
                }

                value= it.getLong("top2") ?: 0

                if(value < scorePessoal && scorePessoal > pontuacaoCliente){
                    v.update("top2",scorePessoal)
                    updateTopTimerFirestore("top2", tempo)
                    return@addOnSuccessListener
                }else if(pontuacaoCliente > value){
                    v.update("top2",pontuacaoCliente)
                    updateTopTimerFirestore("top2",tempoCliente)
                    return@addOnSuccessListener
                }
                if(value == scorePessoal.toLong()){
                    v.update("top2",scorePessoal)
                    updateTimerScoreEquals("top2")
                    return@addOnSuccessListener
                }
                if(value == pontuacaoCliente.toLong()){
                    v.update("top2",pontuacaoCliente)
                    updateTimerScoreEquals("top2")
                    return@addOnSuccessListener
                }

                value= it.getLong("top3") ?: 0

                if(value < scorePessoal && scorePessoal > pontuacaoCliente){
                    v.update("top3",scorePessoal)
                    updateTopTimerFirestore("top3",tempo)
                    return@addOnSuccessListener
                }else if(pontuacaoCliente > value){
                    v.update("top3",pontuacaoCliente)
                    updateTopTimerFirestore("top3",tempoCliente)
                    return@addOnSuccessListener
                }
                if(value == scorePessoal.toLong()){
                    v.update("top3",scorePessoal)
                    updateTimerScoreEquals("top3")
                    return@addOnSuccessListener
                }
                if(value == pontuacaoCliente.toLong()){
                    v.update("top3",pontuacaoCliente)
                    updateTimerScoreEquals("top3")
                    return@addOnSuccessListener
                }

                value= it.getLong("top4") ?: 0

                if(value < scorePessoal && scorePessoal > pontuacaoCliente){
                    v.update("top4",scorePessoal)
                    updateTopTimerFirestore("top4", tempo)
                    return@addOnSuccessListener
                }else if(pontuacaoCliente > value){
                    v.update("top4",pontuacaoCliente)
                    updateTopTimerFirestore("top4", tempoCliente)
                    return@addOnSuccessListener
                }
                if(value == scorePessoal.toLong()){
                    v.update("top4",scorePessoal)
                    updateTimerScoreEquals("top4")
                    return@addOnSuccessListener
                }
                if(value == pontuacaoCliente.toLong()){
                    v.update("top4",pontuacaoCliente)
                    updateTimerScoreEquals("top4")
                    return@addOnSuccessListener
                }

                value= it.getLong("top5") ?: 0

                if(value < scorePessoal && scorePessoal > pontuacaoCliente){
                    v.update("top5",scorePessoal)
                    updateTopTimerFirestore("top5",tempo)
                    return@addOnSuccessListener
                }else if(pontuacaoCliente > value){
                    v.update("top5",pontuacaoCliente)
                    updateTopTimerFirestore("top5",tempoCliente)
                    return@addOnSuccessListener
                }
                if(value == scorePessoal.toLong()){
                    v.update("top5",scorePessoal)
                    updateTimerScoreEquals("top5")
                    return@addOnSuccessListener
                }
                if(value == pontuacaoCliente.toLong()){
                    v.update("top5",pontuacaoCliente)
                    updateTimerScoreEquals("top5")
                    return@addOnSuccessListener
                }


            }
            .addOnFailureListener { e ->
                Log.i("TAG", "updateDataInFirestore: ${e.message}")
            }
    }



    fun updateTimerScoreEquals(top : String){
        val db = Firebase.firestore
        val v  = db.collection("Timers").document("TopTimers")
        v.get(Source.SERVER)
            .addOnSuccessListener {
                val exists = it.exists()
                Log.i("TAG", "updateDataInFirestore: Success? $exists")
                if (!exists)
                    return@addOnSuccessListener

                when (top) {
                    "top1" -> {
                        val value= it.getLong("top1") ?: 0
                        if(value < tempo){
                            v.update("top1", tempo)
                        }
                        if(value < tempoCliente){
                            v.update("top1", tempoCliente)
                        }
                    }
                    "top2" -> {
                        val value= it.getLong("top2") ?: 0
                        if(value < tempo){
                            v.update("top2",tempo)
                        }
                        if(value < tempoCliente){
                            v.update("top2", tempoCliente)
                        }
                    }
                    "top3" -> {
                        val value= it.getLong("top3") ?: 0
                        if(value < tempo){
                            v.update("top3", tempo)
                        }
                        if(value < tempoCliente){
                            v.update("top3", tempoCliente)
                        }
                    }
                    "top4" -> {
                        val value= it.getLong("top4") ?: 0
                        if(value < tempo){
                            v.update("top4", tempo)
                        }
                        if(value < tempoCliente){
                            v.update("top4", tempoCliente)
                        }
                    }
                    "top5" -> {
                        val value= it.getLong("top5") ?: 0
                        if(value < tempo) {
                            v.update("top5", tempo)
                        }
                        if(value < tempoCliente){
                            v.update("top5", tempoCliente)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.i("TAG", "updateDataInFirestore: ${e.message}")
            }

    }

    fun initServerLevel(){
        _state.postValue(State.NEXT_ROUND_INIT)
    }



}