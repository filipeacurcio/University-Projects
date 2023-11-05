package pt.isec.filipe.sandra.patricia

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.filipe.sandra.patricia.MultiplayerViewModel.Companion.SERVER_PORT
import pt.isec.filipe.sandra.patricia.databinding.ActivityMultiplayerGameBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.abs

class MultiplayerGameActivity : AppCompatActivity(), GestureDetector.OnGestureListener  {

    companion object {
        private const val SERVER_MODE = 0
        private const val CLIENT_MODE = 1
        private const val PROFILE_DIR = "LET"
        private const val PROFILE_FILE = "Photo.img"

        fun getServerModeIntent(context : Context) : Intent {
            return Intent(context,MultiplayerGameActivity::class.java).apply {
                putExtra("mode", SERVER_MODE)
            }
        }

        fun getClientModeIntent(context : Context) : Intent {
            return Intent(context,MultiplayerGameActivity::class.java).apply {
                putExtra("mode", CLIENT_MODE)
            }
        }
    }
    private var dlg: AlertDialog? = null
    private val model: MultiplayerViewModel by viewModels()
    lateinit var binding: ActivityMultiplayerGameBinding
    private var colunaEscolhida : Int = 0
    private var linhaEscolhida : Int = 0
    private lateinit var dialogWaitPlayer: AlertDialog

    private val gestureDetector: GestureDetector by lazy{
        GestureDetector(this,this)
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

            model.fillTable(binding.gdTable)
        binding.tvMyWins.text = model.scorePessoal.toString()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        model.connectionState.observe(this) { state ->
           // updateUI()
            if (state != MultiplayerViewModel.ConnectionState.SETTING_PARAMETERS &&
                state != MultiplayerViewModel.ConnectionState.SERVER_CONNECTING &&
                dlg?.isShowing == true) {
                dlg?.dismiss()
                dlg = null
            }

            if (state == MultiplayerViewModel.ConnectionState.CONNECTION_ERROR ||
                state == MultiplayerViewModel.ConnectionState.CONNECTION_ENDED ) {

                finish()
            }
            if (model.connectionState.value == MultiplayerViewModel.ConnectionState.CONNECTION_ESTABLISHED) {
                when (intent.getIntExtra("mode", SERVER_MODE)) {
                    SERVER_MODE -> {
                        model.setBindingClient(binding)
                        startObserver()

                        if(!model.timerCreated){
                            createTimerNew()
                            createTimerNewCliente()
                        }else{
                            createTimerland()
                            createTimerNewClienteLand()
                        }
                        val path = filesDir
                        val letDirectory = File(path, PROFILE_DIR)
                        letDirectory.mkdirs()
                        val file = File(letDirectory, PROFILE_FILE)
                        Log.i("TAG", "fotos: " + file.exists())

                        if(file.exists()){
                            val byteArray = FileInputStream(file).readBytes()
                            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                            val byteArrayv2 = byteArrayOutputStream.toByteArray()
                            model.fotoServidor = android.util.Base64.encodeToString(byteArrayv2, android.util.Base64.DEFAULT)
                        }

                    }
                    CLIENT_MODE ->{
                        model.setBindingClient(binding)
                        val path = filesDir
                        val letDirectory = File(path, PROFILE_DIR)
                        letDirectory.mkdirs()
                        val file = File(letDirectory, PROFILE_FILE)
                        if(file.exists()){
                            val byteArray = FileInputStream(file).readBytes()
                            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                            val byteArrayv2 = byteArrayOutputStream.toByteArray()
                            model.fotoCliente = android.util.Base64.encodeToString(byteArrayv2, android.util.Base64.DEFAULT)

                        }
                    }
                }
            }
        }

        var aux : Boolean = false

        model.state.observe(this){state ->
            if(state == MultiplayerViewModel.State.PLAYER_CORRECT){
                    model.clienteyourCountDownTimer.cancel()
                if((model.tempo + 5000) < model.tempoLimite)
                    model.tempo += 5000
                if((model.tempo + 5000) > model.tempoLimite)
                    model.tempo = model.tempoLimite.toLong()

                model.clienteyourCountDownTimer = object : CountDownTimer(model.tempoCliente, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            model.timerCreated = true
                            model.tempoCliente = millisUntilFinished
                            model.enviaTempoCliente()
                        }

                        override fun onFinish() {
                            model.eliminado = true
                        }
                    }.start()
                }
            if(state == MultiplayerViewModel.State.ROUND_ENDED){
                model.eliminado = true
                if(aux)
                dialogWaitPlayer.cancel()

                android.app.AlertDialog.Builder(this@MultiplayerGameActivity).apply {
                    setTitle(R.string.dialogBackTitle)
                    setMessage(R.string.dialogDescription)

                    setPositiveButton(R.string.dialog_Yes) { _, _ ->
                        Toast.makeText(this@MultiplayerGameActivity, R.string.Game_Closed,
                            Toast.LENGTH_LONG).show()
                        model.updateTopScoresFirestore()
                        model.stopServer()
                        finish()
                    }

                    setNegativeButton(R.string.dialog_No){_, _ ->
                        // if user press no, then return the activity
                        //model.resetGame(binding.gdTable)
                    }

                    setCancelable(true)
                }.create().show()

            }

            if(state == MultiplayerViewModel.State.ONLY_PLAY_SERVER){
                dialogWaitPlayer.cancel()
                model.nivel++
                model.criaTabuleiro()
                model.clienteyourCountDownTimer.cancel()
                model.yourCountDownTimer.cancel()
                createTimerNew()
                model.yourCountDownTimer.cancel()

            }

            if(state == MultiplayerViewModel.State.NEXT_ROUND_OTHER){
                when (intent.getIntExtra("mode", SERVER_MODE)) {
                    SERVER_MODE -> {
                        val decodedString = android.util.Base64.decode(model.fotoCliente, android.util.Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0 , decodedString.size)
                        val imageView = ImageView(this)
                        imageView.setImageBitmap(bitmap)
                        val mBuilder = AlertDialog.Builder(this)
                            .setTitle(R.string.wait_level_tite)
                            .setMessage(R.string.wait_level_description)
                            .setCancelable(true)

                        mBuilder.setView(imageView)
                        dialogWaitPlayer = mBuilder.create()
                        dialogWaitPlayer.setCanceledOnTouchOutside(false)
                        dialogWaitPlayer.show()
                        aux = true
                    }
                    CLIENT_MODE ->{
                        val decodedString = android.util.Base64.decode(model.fotoServidor, android.util.Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0 , decodedString.size)
                        val imageView = ImageView(this)
                        imageView.setImageBitmap(bitmap)

                        val mBuilder = AlertDialog.Builder(this)
                            .setTitle(R.string.wait_level_tite)
                            .setMessage(R.string.wait_level_description)
                            .setCancelable(true)
                        mBuilder.setView(imageView)
                        dialogWaitPlayer = mBuilder.create()
                        dialogWaitPlayer.setCanceledOnTouchOutside(false)
                        dialogWaitPlayer.show()

                        aux = true
                    }
                }

            }

            if(state == MultiplayerViewModel.State.NEXT_ROUND_INIT){
                model.nivel++
                model.mudaNivel = false
                model.mudaNivelCliente = false

                when (intent.getIntExtra("mode", SERVER_MODE)) {
                    SERVER_MODE -> {
                        if(aux){
                            if(dialogWaitPlayer.isShowing)
                                dialogWaitPlayer.cancel()
                        }
                        model.todosTabuleiros = ArrayList()
                        model.clienteTentativas = 0
                        model.tentativasServidor = 0
                        model.expressoesAcertadas = 0
                        model.expressoesAcertadasCliente = 0
                        mudaNivelTimer()
                        model.criaTabuleiro()
                        model.tempoCliente = model.tempoLimite.toLong()
                       model.fillTable(binding.gdTable)
                        model.yourCountDownTimer.cancel()
                        createTimerNew()
                    }
                    CLIENT_MODE -> {
                        if(aux)
                            dialogWaitPlayer.cancel()
                        mudaNivelTimerCliente()
                    }
                }

            }
        }

        if (model.connectionState.value != MultiplayerViewModel.ConnectionState.CONNECTION_ESTABLISHED) {
            when (intent.getIntExtra("mode", SERVER_MODE)) {
                SERVER_MODE -> {
                    startObserver()
                    model.criaTabuleiro()
                    startAsServer()
                }
                CLIENT_MODE -> {
                    startAsClient()

                }
            }
        }


        Log.i("TAG", "onCreate: ")
    }

    private fun createTimerNew(){
        model.yourCountDownTimer = object : CountDownTimer(model.tempoLimite.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                model.timerCreated = true
                model.tempo = millisUntilFinished
                if(model.tempo.toString().length == 5)
                    binding.tvTimeLeft.text = model.tempo.toString().subSequence(0,2)
                else
                    binding.tvTimeLeft.text = model.tempo.toString().subSequence(0,1)

            }
            override fun onFinish() {
                model.eliminado = true
                model.nivel++
                model.enviaEliminadoCliente()
                createTimerNewCliente()

                android.app.AlertDialog.Builder(this@MultiplayerGameActivity).apply {
                    setTitle(R.string.dialogBackTitle)
                    setMessage(R.string.dialogDescription)

                    setPositiveButton(R.string.dialog_Yes) { _, _ ->
                        Toast.makeText(this@MultiplayerGameActivity, R.string.Game_Closed,
                            Toast.LENGTH_LONG).show()
                        model.updateTopScoresFirestore()
                        model.stopServer()
                        finish()
                    }

                    setNegativeButton(R.string.dialog_No){_, _ ->
                        // if user press no, then return the activity
                        //model.resetGame(binding.gdTable)
                    }

                    setCancelable(true)
                }.create().show()

            }
        }.start()
    }

    private fun createTimerland(){
        model.yourCountDownTimer = object : CountDownTimer(model.tempo, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                model.timerCreated = true
                model.tempo = millisUntilFinished
                if(model.tempo.toString().length == 5)
                    binding.tvTimeLeft.text = model.tempo.toString().subSequence(0,2)
                else
                    binding.tvTimeLeft.text = model.tempo.toString().subSequence(0,1)

            }
            override fun onFinish() {
                model.eliminado = true
                model.nivel++

                model.enviaEliminadoCliente()
                createTimerNewCliente()

                android.app.AlertDialog.Builder(this@MultiplayerGameActivity).apply {
                    setTitle(R.string.dialogBackTitle)
                    setMessage(R.string.dialogDescription)

                    setPositiveButton(R.string.dialog_Yes) { _, _ ->
                        Toast.makeText(this@MultiplayerGameActivity, R.string.Game_Closed,
                            Toast.LENGTH_LONG).show()
                        model.updateTopScoresFirestore()

                        finish()
                    }

                    setNegativeButton(R.string.dialog_No){_, _ ->
                        // if user press no, then return the activity
                      //  model.resetGame(binding.gdTable)

                    }
                    setCancelable(true)
                }.create().show()

            }
        }.start()
    }

    private fun createTimerNewCliente(){
        model.clienteyourCountDownTimer = object : CountDownTimer(model.tempoLimite.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                model.timerCreated = true
                model.tempoCliente = millisUntilFinished
                model.enviaTempoCliente()

            }
            override fun onFinish() {
                if(dialogWaitPlayer.isShowing)
                    cancel()
                model.initServerLevel()
            }
        }.start()
    }

    private fun createTimerNewClienteLand(){
        model.yourCountDownTimer = object : CountDownTimer(model.tempoCliente, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                model.timerCreated = true
                model.tempoCliente = millisUntilFinished
                model.enviaTempoCliente()

            }
            override fun onFinish() {

            }
        }.start()
    }




    private fun startAsServer() {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = wifiManager.connectionInfo.ipAddress // Deprecated in API Level 31. Suggestion NetworkCallback
        val strIPAddress = String.format("%d.%d.%d.%d",
            ip and 0xff,
            (ip shr 8) and 0xff,
            (ip shr 16) and 0xff,
            (ip shr 24) and 0xff
        )

        val ll = LinearLayout(this).apply {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            this.setPadding(50, 50, 50, 50)
            layoutParams = params
            setBackgroundColor(Color.rgb(240, 224, 208))
            orientation = LinearLayout.HORIZONTAL
            addView(ProgressBar(context).apply {
                isIndeterminate = true
                val paramsPB = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                paramsPB.gravity = Gravity.CENTER_VERTICAL
                layoutParams = paramsPB
                indeterminateTintList = ColorStateList.valueOf(Color.rgb(96, 96, 32))
            })
            addView(TextView(context).apply {
                val paramsTV = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams = paramsTV
                text = String.format(getString(R.string.msg_ip_address),strIPAddress)
                textSize = 20f
                setTextColor(Color.rgb(96, 96, 32))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            })
        }

        dlg = AlertDialog.Builder(this)
            .setTitle(R.string.server_mode)
            .setView(ll)
            .setOnCancelListener {
                model.stopServer()
                finish()
            }
            .create()

        model.startServer(binding.gdTable, binding)

        dlg?.show()
    }


    private fun startAsClient() {
        val edtBox = EditText(this).apply {
            maxLines = 1
            filters = arrayOf(object : InputFilter {
                override fun filter(
                    source: CharSequence?,
                    start: Int,
                    end: Int,
                    dest: Spanned?,
                    dstart: Int,
                    dend: Int
                ): CharSequence? {
                    source?.run {
                        var ret = ""
                        forEach {
                            if (it.isDigit() || it == '.')
                                ret += it
                        }
                        return ret
                    }
                    return null
                }

            })
        }
        val dlg = AlertDialog.Builder(this)
            .setTitle(R.string.client_mode)
            .setMessage(R.string.ask_ip)
            .setPositiveButton(R.string.button_connect) { _: DialogInterface, _: Int ->
                val strIP = edtBox.text.toString()
                if (strIP.isEmpty() || !Patterns.IP_ADDRESS.matcher(strIP).matches()) {
                    Toast.makeText(this@MultiplayerGameActivity, R.string.error_address, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    model.startClient(strIP, gridLayout = binding.gdTable, binding = binding)
                }
            }
            .setNeutralButton(R.string.btn_emulator) { _: DialogInterface, _: Int ->
                model.startClient("10.0.2.2", SERVER_PORT-1, binding.gdTable, binding)
                // Configure port redirect on the Server Emulator:
                // telnet localhost <5554|5556|5558|...>
                // auth <key>
                // redir add tcp:9998:9999
            }
            .setNegativeButton(R.string.button_cancel) { _: DialogInterface, _: Int ->
                finish()
            }
            .setCancelable(false)
            .setView(edtBox)
            .create()

        dlg.show()
    }

    override fun onDown(e: MotionEvent): Boolean {

        val gridLayout = findViewById<GridLayout>(R.id.gdTable)
        val width = gridLayout.width / 5
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        val actionBarHeight: Int
        val styledAttributes: TypedArray = this.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        actionBarHeight = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()

        val height = gridLayout.height / 5

        Log.i("TAG", "onDown: ")

        return true   // obrigatorio return true se não os outros eventos não vão ser processados
    }

    override fun onShowPress(e: MotionEvent) {
        Log.i("TAG", "onShowPress: ")
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        Log.i("TAG", "onSingleTapUp: ")
        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        Log.i("TAG", "onLongPress: ")
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onFling(
        p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {

        if(!model.eliminado){

        var linhaOrColuna = 0
        var linhaouColSegundo = 0

            when (intent.getIntExtra("mode", SERVER_MODE)) {
                SERVER_MODE -> {
                    if(model.verificaLinhaColunaMaior() == 1){
                        linhaOrColuna = 1
                        Log.i("TAG", "Linha: ")
                    }else {
                        linhaOrColuna = 0
                        Log.i("TAG", "Coluna: ")
                    }
                    if(linhaOrColuna== 1){
                        if(model.segundoResultadoMaior > model.resultadoMaiorColuna)
                            linhaouColSegundo = 1
                        else
                            linhaouColSegundo = 0
                    }else{
                        if(model.segundoResultadoMaiorColuna > model.resultadoMaior)
                            linhaouColSegundo = 0
                        else
                            linhaouColSegundo = 1
                    }
                }

            }


        Log.i("TAG", "Row escolhida no fling " + verificaLinhaEscolhida(p0))
        var result = false

        if(verificaLinhaEscolhida(p0) <= 4){
            try {
                val diffY: Float = p1.y - p0.y
                val diffX: Float = p1.x - p0.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SinglePlayerGameActivity.SWIPE_THRESHOLD && Math.abs(p2) > SinglePlayerGameActivity.SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            when (intent.getIntExtra("mode", SERVER_MODE)) {
                                SERVER_MODE -> {
                                    onSwipeRight(linhaOrColuna, p0, linhaouColSegundo)
                                    model.tentativasServidor++
                                }
                                CLIENT_MODE -> onSwipeRightClient(linhaOrColuna, p0)
                            }
                        } else {
                            when (intent.getIntExtra("mode", SERVER_MODE)) {
                                SERVER_MODE -> {
                                    onSwipeLeft(linhaOrColuna, p0, linhaouColSegundo)
                                    model.tentativasServidor++
                                }
                                CLIENT_MODE -> onSwipeLeftClient(linhaOrColuna, p0)
                            }
                        }
                        result = true
                    }
                } else if (abs(diffY) > SinglePlayerGameActivity.SWIPE_THRESHOLD && abs(p3) > SinglePlayerGameActivity.SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        when (intent.getIntExtra("mode", SERVER_MODE)) {
                            SERVER_MODE -> {
                            onSwipeBottom(linhaOrColuna, p0, linhaouColSegundo)
                                model.tentativasServidor++
                            }
                            CLIENT_MODE -> onSwipeBottomClient(linhaOrColuna,p0)
                        }
                    } else {
                        when (intent.getIntExtra("mode", SERVER_MODE)) {
                            SERVER_MODE -> {
                                onSwipeTop(linhaOrColuna, p0, linhaouColSegundo)
                                model.tentativasServidor++
                            }
                            CLIENT_MODE -> onSwipeTopClient(linhaOrColuna, p0)

                        }
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        return result
        }
        return false
    }

    fun onSwipeRightClient(linhaOuCol: Int, p0: MotionEvent){
        var linha  = verificaLinhaEscolhida(p0)
        model.enviarJogada(linhaOuCol , linha, "horizontal")
    }
    fun onSwipeLeftClient(linhaOuCol: Int, p0: MotionEvent){
        var linha  = verificaLinhaEscolhida(p0)
        model.enviarJogada(linhaOuCol, linha, "horizontal")

    }
    fun onSwipeTopClient(linhaOuCol: Int,p0: MotionEvent){
        var coluna  = verificaColunaEscolhida(p0)
        model.enviarJogada(linhaOuCol, coluna, "vertical")

    }
    fun onSwipeBottomClient( linhaOuCol: Int,p0: MotionEvent){
        var coluna  = verificaColunaEscolhida(p0)
        model.enviarJogada(linhaOuCol, coluna, "vertical")

    }


    fun onSwipeRight(linhaOuCol: Int, p0: MotionEvent, linhaouColSegundo: Int) {
        Log.i("TAG", "onSwipeRight: ")
        if (verificaLinhaEscolhida(p0) == model.linhaAcertada) {
            if (linhaOuCol == 1) {
                val toast =
                    Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
                toast.show()
                model.updateVistaAcertada(binding.gdTable, binding.tvMyWins)
                binding.tvMyWins.text = model.scorePessoal.toString()
                if (model.mudaNivel){
                    model.yourCountDownTimer.cancel()
                    //mudaNivelTimer()
                    //  resetClock()
                }else{
                    resetClock()
                }
            }
        }
        if (verificaLinhaEscolhida(p0) == model.segundaLinhaMaior  && linhaouColSegundo == 1||
            verificaLinhaEscolhida(p0) == model.linhaAcertada && linhaouColSegundo == 1) {
            val toast = Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
            toast.show()
            model.updateVistaUmPonto(binding.gdTable, binding.tvMyWins)

        } else {
            model.updateVista(binding.gdTable)
        }
    }

    fun onSwipeLeft(linhaOuCol: Int, p0: MotionEvent, linhaouColSegundo: Int) {
        Log.i("TAG", "onSwipeLeft: ")
        if (verificaLinhaEscolhida(p0) == model.linhaAcertada) {
            if (linhaOuCol == 1) {
                val toast =
                    Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
                toast.show()
                model.updateVistaAcertada(binding.gdTable, binding.tvMyWins)
                binding.tvMyWins.text = model.scorePessoal.toString()
                if (model.mudaNivel){
                    model.yourCountDownTimer.cancel()
                   // mudaNivelTimer()
                    //  resetClock()
                }else{
                    resetClock()
                }
            }
        }
        if (verificaLinhaEscolhida(p0) == model.segundaLinhaMaior  && linhaouColSegundo == 1||
            verificaLinhaEscolhida(p0) == model.linhaAcertada && linhaouColSegundo == 1) {
            val toast = Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
            toast.show()
            model.updateVistaUmPonto(binding.gdTable, binding.tvMyWins)

        } else {
            model.updateVista(binding.gdTable)
        }
    }

    fun onSwipeTop(linhaOuCol: Int, p0: MotionEvent, linhaouColSegundo: Int) {
        Log.i("TAG", "onSwipeTop: ")
        if (verificaColunaEscolhida(p0) == model.colunaAcertada) {
            if (linhaOuCol == 0) {
                val toast =
                    Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
                toast.show()
                model.updateVistaAcertada(binding.gdTable, binding.tvMyWins)
                binding.tvMyWins.text = model.scorePessoal.toString()
                if (model.mudaNivel){
                    model.yourCountDownTimer.cancel()
                  //  mudaNivelTimer()
                    //  resetClock()
                }else{
                    resetClock()
                }
            }
        }
        if (verificaColunaEscolhida(p0) == model.segundaColunaMaior  && linhaouColSegundo == 0||
            verificaColunaEscolhida(p0) == model.colunaAcertada && linhaouColSegundo == 0) {

            val toast = Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
            toast.show()
            model.updateVistaUmPonto(binding.gdTable, binding.tvMyWins)

        } else {
            model.updateVista(binding.gdTable)
        }
    }

    fun onSwipeBottom(linhaOuCol: Int, p0: MotionEvent, linhaouColSegundo: Int) {
        Log.i("TAG", "onSwipeBottom: ")
        if (verificaColunaEscolhida(p0) == model.colunaAcertada) {
            if (linhaOuCol == 0) {
                val toast =
                    Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
                toast.show()
                model.updateVistaAcertada(binding.gdTable, binding.tvMyWins)
                binding.tvMyWins.text = model.scorePessoal.toString()
                if (model.mudaNivel){
                    model.yourCountDownTimer.cancel()
                   // mudaNivelTimer()
                    //  resetClock()
                }else{
                    resetClock()
                }
            }
        }
        if (verificaColunaEscolhida(p0) == model.segundaColunaMaior  && linhaouColSegundo == 0||
            verificaColunaEscolhida(p0) == model.colunaAcertada && linhaouColSegundo == 0) {

            val toast = Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
            toast.show()
            model.updateVistaUmPonto(binding.gdTable, binding.tvMyWins)

        } else {

            model.updateVista(binding.gdTable)
        }
    }



    fun verificaLinhaEscolhida(p0 : MotionEvent): Int{
        val gridLayout = findViewById<GridLayout>(R.id.gdTable)
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }

        val actionBarHeight: Int
        val styledAttributes: TypedArray = this.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        actionBarHeight = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()

        val height = gridLayout.height / 5
        linhaEscolhida = ((p0.y - (actionBarHeight + statusBarHeight)) / height).toInt()
        return linhaEscolhida
    }


    fun verificaColunaEscolhida(p0: MotionEvent) : Int{
        val gridLayout = findViewById<GridLayout>(R.id.gdTable)
        val width = gridLayout.width / 5
        colunaEscolhida = (p0.x / width).toInt()
        return colunaEscolhida
    }

    private fun mudaNivelTimerCliente(){

        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle(R.string.next_level_tite)
            .setMessage(R.string.next_level_description)
            .setCancelable(false)
            .setNeutralButton(R.string.pause_game){_ , _ ->

            }
            .create()

        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnShowListener(object : DialogInterface.OnShowListener {
            private val AUTO_DISMISS_MILLIS = 5000
            override fun onShow(dialog: DialogInterface) {
                val defaultButton =
                    (dialog as android.app.AlertDialog).getButton(android.app.AlertDialog.BUTTON_NEUTRAL)
                val negativeButtonText = defaultButton.text
                object : CountDownTimer(AUTO_DISMISS_MILLIS.toLong(), 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        defaultButton.text = java.lang.String.format(
                            Locale.getDefault(), "%s (%d)",
                            negativeButtonText,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                        )
                    }

                    override fun onFinish() {
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                    }
                }.start()
            }
        })
        dialog.show()

    }



    private fun mudaNivelTimer(){
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle(R.string.next_level_tite)
            .setMessage(R.string.next_level_description)
            .setCancelable(false)
            .setNeutralButton(R.string.pause_game){_ , _ ->
            }
            .create()

        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnShowListener(object : DialogInterface.OnShowListener {
            private val AUTO_DISMISS_MILLIS = 5000
            override fun onShow(dialog: DialogInterface) {
                val defaultButton =
                    (dialog as android.app.AlertDialog).getButton(android.app.AlertDialog.BUTTON_NEUTRAL)
                val negativeButtonText = defaultButton.text
                object : CountDownTimer(AUTO_DISMISS_MILLIS.toLong(), 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        defaultButton.text = java.lang.String.format(
                            Locale.getDefault(), "%s (%d)",
                            negativeButtonText,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                        )
                    }

                    override fun onFinish() {
                        if (dialog.isShowing) {
                            dialog.dismiss()
                            resetClock()
                            model.clienteMudaNivel()

                        }
                    }
                }.start()
            }
        })
        dialog.show()

    }

    @SuppressLint("SuspiciousIndentation")
    private fun resetClock(){

        model.yourCountDownTimer.cancel()

        if((model.tempo + 5000) < model.tempoLimite)
            model.tempo += 5000
        if((model.tempo + 5000) > model.tempoLimite)
            model.tempo = model.tempoLimite.toLong()


        Log.i("TAG", "resetClock: ")
        model.yourCountDownTimer = object : CountDownTimer(model.tempo, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                model.tempo = millisUntilFinished
                if(model.tempo.toString().length == 5)
                    binding.tvTimeLeft.text = model.tempo.toString().subSequence(0,2)
                else
                    binding.tvTimeLeft.text = model.tempo.toString().subSequence(0,1)

            }
            override fun onFinish() {
                model.eliminado = true
                model.nivel++

                model.enviaEliminadoCliente()
                createTimerNewCliente()
                android.app.AlertDialog.Builder(this@MultiplayerGameActivity).apply {
                    setTitle(R.string.dialogBackTitle)
                    setMessage(R.string.dialogDescription)

                    setPositiveButton(R.string.dialog_Yes) { _, _ ->
                        Toast.makeText(this@MultiplayerGameActivity, R.string.Game_Closed,
                            Toast.LENGTH_LONG).show()
                        //fecha o jogo e depois adicionar pontuacao
                        model.updateTopScoresFirestore()

                        finish()
                    }

                    setNegativeButton(R.string.dialog_No){_, _ ->
                        // if user press no, then return the activity
                       // model.resetGame(binding.gdTable)
                    }

                    setCancelable(true)
                }.create().show()

            }
        }.start()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(gestureDetector.onTouchEvent(event))
            return true

        return super.onTouchEvent(event)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        model.stopGame()
    }


    @SuppressLint("SetTextI18n")
    fun startObserver() {
        var top1 = " "
        var top2 = " "
        var top3 = " "
        var top4 = " "
        var top5 = " "

        val db = Firebase.firestore
        db.collection("Scores").document("TopScoresMP")
            .addSnapshotListener { docSS, e ->
                if (e!=null) {
                    return@addSnapshotListener
                }
                if (docSS!=null && docSS.exists()) {
                    top1 = docSS.getLong("top1").toString()
                    top2 = docSS.getLong("top2").toString()
                    top3 = docSS.getLong("top3").toString()
                    top4 = docSS.getLong("top4").toString()
                    top5 = docSS.getLong("top5").toString()
                }
            }

        db.collection("Timers").document("TopTimersMP")
            .addSnapshotListener { docSS, e ->
                if (e!=null) {
                    return@addSnapshotListener
                }
                if (docSS!=null && docSS.exists()) {
                    binding.tvTop1.text = top1 + " : " + docSS.getLong("top1").toString() + " ms"
                    binding.tvTop2.text = top2 + " : " + docSS.getLong("top2").toString() + " ms"
                    binding.tvTop3.text = top3 + " : " + docSS.getLong("top3").toString() + " ms"
                    binding.tvTop4.text = top4 + " : " + docSS.getLong("top4").toString() + " ms"
                    binding.tvTop5.text = top5 + " : " + docSS.getLong("top5").toString() + " ms"
                }
            }
    }



}