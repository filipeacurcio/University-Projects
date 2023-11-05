package pt.isec.filipe.sandra.patricia

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.content.res.TypedArray
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.filipe.sandra.patricia.databinding.ActivitySinglePlayerGameBinding
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs


private const val TAG = "SingleActivity"


class SinglePlayerGameActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    lateinit var binding: ActivitySinglePlayerGameBinding

    companion object{
        var  SWIPE_THRESHOLD = 100
        var SWIPE_VELOCITY_THRESHOLD = 100
    }

    private val model: SinglePlayerViewModel by viewModels()
    private var colunaEscolhida : Int = 0
    private var linhaEscolhida : Int = 0


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePlayerGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(model.tabuleiro.isEmpty())
        model.criaTabuleiro()
        model.fillTable(binding.gdTable)
        binding.tvMyWins.text = model.scorePessoal.toString()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Log.i("TAG", "criaTabuleiro: " + model.tempoLimite)

        if(!model.timerCreated){
            createTimerNew()
        }else{
            createTimerland()
        }

        startObserver()

        Log.i(TAG, "onCreate:  " + model.tabuleiro)
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
                AlertDialog.Builder(this@SinglePlayerGameActivity).apply {
                    setTitle(R.string.dialogBackTitle)
                    setMessage(R.string.dialogDescription)

                    setPositiveButton(R.string.dialog_Yes) { _, _ ->
                        Toast.makeText(this@SinglePlayerGameActivity, R.string.Game_Closed,
                            Toast.LENGTH_LONG).show()
                        updateTopScoresFirestore()
                        finish()
                    }

                    setNegativeButton(R.string.dialog_No){_, _ ->
                        // if user press no, then return the activity
                        model.resetGame(binding.gdTable)
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
                AlertDialog.Builder(this@SinglePlayerGameActivity).apply {
                    setTitle(R.string.dialogBackTitle)
                    setMessage(R.string.dialogDescription)

                    setPositiveButton(R.string.dialog_Yes) { _, _ ->
                        Toast.makeText(this@SinglePlayerGameActivity, R.string.Game_Closed,
                            Toast.LENGTH_LONG).show()
                        updateTopScoresFirestore()
                        finish()
                    }

                    setNegativeButton(R.string.dialog_No){_, _ ->
                        // if user press no, then return the activity
                        model.resetGame(binding.gdTable)

                    }

                    setCancelable(true)
                }.create().show()

            }
        }.start()
    }

    private val gestureDetector: GestureDetector by lazy{
        GestureDetector(this,this)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(gestureDetector.onTouchEvent(event))
            return true

        return super.onTouchEvent(event)
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    override fun onDown(p0: MotionEvent): Boolean {

        val gridLayout = findViewById<GridLayout>(R.id.gdTable)
        val width = gridLayout.width / 5
        colunaEscolhida = (p0.x / width).toInt()
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

        Log.i(TAG, "onDown: " + p0.x + " "  + p0.y + "Row: " + linhaEscolhida +  " Coluna " + colunaEscolhida)

        return true   // obrigatorio return true se não os outros eventos não vão ser processados
    }

    override fun onShowPress(e: MotionEvent) {
        Log.i(TAG, "onShowPress: ")
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        Log.i(TAG, "onSingleTapUp: ")
        return false
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
       // Log.i(TAG, "onScroll: ${p0.x} ${p1.x}")
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        Log.i(TAG, "onLongPress: ")
    }



    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        val linhaOrColuna: Int
        val linhaouColSegundo : Int

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

        Log.i(TAG, "Row escolhida no fling " + verificaLinhaEscolhida(p0))
        var result = false

        if(verificaLinhaEscolhida(p0) <= 4){
            try {
                val diffY: Float = p1.y - p0.y
                val diffX: Float = p1.x - p0.x
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(p2) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight(linhaOrColuna, p0, linhaouColSegundo)
                        } else {
                            onSwipeLeft(linhaOrColuna, p0,  linhaouColSegundo)
                        }
                        result = true
                    }
                } else if (abs(diffY) > SWIPE_THRESHOLD && abs(p3) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom(linhaOrColuna, p0,  linhaouColSegundo)
                    } else {
                        onSwipeTop(linhaOrColuna, p0,  linhaouColSegundo)
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        return result
    }

    fun onSwipeRight(linhaOuCol: Int, p0: MotionEvent, linhaouColSegundo: Int) {
        Log.i(TAG, "onSwipeRight: ")
        Log.i("TAG", "onSwipeRight: $linhaouColSegundo Linha esolhida " + verificaLinhaEscolhida(p0) +
                "\nlinha maior   " + model.linhaAcertada + "    linha segundo . " + model.segundaLinhaMaior)
        if (verificaLinhaEscolhida(p0) == model.linhaAcertada) {
            if (linhaOuCol == 1) {
                val toast =
                    Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
                toast.show()
                model.updateVistaAcertada(binding.gdTable, binding.tvMyWins)
                binding.tvMyWins.text = model.scorePessoal.toString()
                if (model.mudaNivel){
                    model.yourCountDownTimer.cancel()
                    mudaNivelTimer()
                  //  resetClock()
                }else{
                    resetClock()
                }
            }
        }
        if (verificaLinhaEscolhida(p0) == model.linhaAcertada && linhaouColSegundo == 1) {

                val toast = Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
                toast.show()
                model.updateVistaUmPonto(binding.gdTable, binding.tvMyWins)

        } else {

            model.updateVista(binding.gdTable)
        }
    }

    fun onSwipeLeft(linhaOuCol: Int, p0: MotionEvent, linhaouColSegundo: Int) {
        Log.i(TAG, "onSwipeLeft: ")
        Log.i("TAG", "onSwipeRight: $linhaouColSegundo Linha esolhida " + verificaLinhaEscolhida(p0) +
                "\nlinha maior   " + model.linhaAcertada + "    linha segundo . " + model.segundaLinhaMaior)
        if (verificaLinhaEscolhida(p0) == model.linhaAcertada) {
            if (linhaOuCol == 1) {
                val toast =
                    Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
                toast.show()
                model.updateVistaAcertada(binding.gdTable, binding.tvMyWins)
                binding.tvMyWins.text = model.scorePessoal.toString()
                if (model.mudaNivel){
                    model.yourCountDownTimer.cancel()
                    mudaNivelTimer()
                    //  resetClock()
                }else{

                    resetClock()
                }
            }
        }
        if (verificaLinhaEscolhida(p0) == model.linhaAcertada && linhaouColSegundo == 1) {

            val toast = Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
            toast.show()
            model.updateVistaUmPonto(binding.gdTable, binding.tvMyWins)

        } else {
            model.updateVista(binding.gdTable)
        }
    }

    fun onSwipeTop(linhaOuCol: Int, p0: MotionEvent, linhaouColSegundo: Int) {
        Log.i(TAG, "onSwipeTop: ")
        if (verificaColunaEscolhida(p0) == model.colunaAcertada) {
            if (linhaOuCol == 0) {
                val toast =
                    Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
                toast.show()
                model.updateVistaAcertada(binding.gdTable, binding.tvMyWins)
                binding.tvMyWins.text = model.scorePessoal.toString()
                if (model.mudaNivel){
                    model.yourCountDownTimer.cancel()
                    mudaNivelTimer()
                    //  resetClock()
                }else{
                    resetClock()
                }
            }
        }
        if (verificaColunaEscolhida(p0) == model.colunaAcertada && linhaouColSegundo == 0) {
            val toast = Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
            toast.show()
            model.updateVistaUmPonto(binding.gdTable, binding.tvMyWins)

        } else {
            model.updateVista(binding.gdTable)
        }
    }

    fun onSwipeBottom(linhaOuCol: Int, p0: MotionEvent, linhaouColSegundo: Int) {
        Log.i(TAG, "onSwipeBottom: ")
        if (verificaColunaEscolhida(p0) == model.colunaAcertada) {
            if (linhaOuCol == 0) {
                val toast =
                    Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
                toast.show()
                model.updateVistaAcertada(binding.gdTable, binding.tvMyWins)
                binding.tvMyWins.text = model.scorePessoal.toString()
                if (model.mudaNivel){
                    model.yourCountDownTimer.cancel()
                    mudaNivelTimer()
                    //  resetClock()
                }else{
                    resetClock()
                }
            }
        }
        if (verificaColunaEscolhida(p0) == model.colunaAcertada && linhaouColSegundo == 0) {
            val toast = Toast.makeText(applicationContext, R.string.acertouJogo,  Toast.LENGTH_SHORT)
            toast.show()
            model.updateVistaUmPonto(binding.gdTable, binding.tvMyWins)

        } else {
            model.updateVista(binding.gdTable)
        }
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
                AlertDialog.Builder(this@SinglePlayerGameActivity).apply {
                    setTitle(R.string.dialogBackTitle)
                    setMessage(R.string.dialogDescription)

                    setPositiveButton(R.string.dialog_Yes) { _, _ ->
                        Toast.makeText(this@SinglePlayerGameActivity, R.string.Game_Closed,
                            Toast.LENGTH_LONG).show()
                        //fecha o jogo e depois adicionar pontuacao
                        updateTopScoresFirestore()
                    }

                    setNegativeButton(R.string.dialog_No){_, _ ->
                        // if user press no, then return the activity
                        model.resetGame(binding.gdTable)
                    }

                    setCancelable(true)
                }.create().show()

            }
        }.start()


    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        AlertDialog.Builder(this).apply {
            setTitle(R.string.dialogBackTitle)
            setMessage(R.string.dialogDescription)

            setPositiveButton(R.string.dialog_Yes) { _, _ ->
                Toast.makeText(this@SinglePlayerGameActivity, R.string.Game_Closed,
                    Toast.LENGTH_LONG).show()
                updateTopScoresFirestore()
                super.onBackPressed()
            }

            setNegativeButton(R.string.dialog_No){_, _ ->
                // if user press no, then return the activity
                model.resetGame(binding.gdTable)
                createTimerland()
            }

            setCancelable(true)
        }.create().show()

    }


    fun verificaColunaEscolhida(p0: MotionEvent) : Int{
        val gridLayout = findViewById<GridLayout>(R.id.gdTable)
        val width = gridLayout.width / 5
        colunaEscolhida = (p0.x / width).toInt()
        return colunaEscolhida
    }
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
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


    fun updateTopTimerFirestore(top : String){
        val db = Firebase.firestore
        val v  = db.collection("Timers").document("TopTimers")
        v.get(Source.SERVER)
            .addOnSuccessListener {
                val exists = it.exists()
                Log.i(TAG, "updateDataInFirestore: Success? $exists")
                if (!exists)
                    return@addOnSuccessListener

                when (top) {
                    "top1" -> {
                            v.update("top1", model.tempo)
                    }
                    "top2" -> {
                            v.update("top2",model.tempo)
                    }
                    "top3" -> {
                            v.update("top3", model.tempo)
                    }
                    "top4" -> {
                            v.update("top4",model.tempo)
                    }
                    "top5" -> {
                            v.update("top5",model.tempo)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "updateDataInFirestore: ${e.message}")
            }

    }

    fun updateTimerScoreEquals(top : String){
        val db = Firebase.firestore
        val v  = db.collection("Timers").document("TopTimers")
        v.get(Source.SERVER)
            .addOnSuccessListener {
                val exists = it.exists()
                Log.i(TAG, "updateDataInFirestore: Success? $exists")
                if (!exists)
                    return@addOnSuccessListener

                when (top) {
                    "top1" -> {
                        val value= it.getLong("top1") ?: 0
                        if(value < model.tempo){
                            v.update("top1", model.tempo)
                        }
                    }
                    "top2" -> {
                        val value= it.getLong("top2") ?: 0
                        if(value < model.tempo){
                            v.update("top2",model.tempo)
                        }
                    }
                    "top3" -> {
                        val value= it.getLong("top3") ?: 0
                        if(value < model.tempo){
                            v.update("top3", model.tempo)
                        }
                    }
                    "top4" -> {
                        val value= it.getLong("top4") ?: 0
                        if(value < model.tempo){
                            v.update("top4",model.tempo)
                        }
                    }
                    "top5" -> {
                        val value= it.getLong("top5") ?: 0
                        if(value < model.tempo) {
                            v.update("top5", model.tempo)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "updateDataInFirestore: ${e.message}")
            }

    }



    fun updateTopScoresFirestore(){
        val db = Firebase.firestore
        val v  = db.collection("Scores").document("TopScores")

        v.get(Source.SERVER)
            .addOnSuccessListener {
                val exists = it.exists()
                Log.i(TAG, "updateDataInFirestore: Success? $exists")
                if (!exists)
                    return@addOnSuccessListener

                var value= it.getLong("top1") ?: 0

                if(value < model.scorePessoal){
                    v.update("top1",model.scorePessoal)
                    updateTopTimerFirestore("top1")
                    return@addOnSuccessListener
                }
                if(value == model.scorePessoal.toLong()){
                    v.update("top1",model.scorePessoal)
                    updateTimerScoreEquals("top1")
                    return@addOnSuccessListener
                }

                value= it.getLong("top2") ?: 0
                if(value < model.scorePessoal){
                    v.update("top2",model.scorePessoal)
                    updateTopTimerFirestore("top2")
                    return@addOnSuccessListener
                }
                if(value == model.scorePessoal.toLong()){
                    v.update("top2",model.scorePessoal)
                    updateTimerScoreEquals("top2")
                    return@addOnSuccessListener
                }

                value= it.getLong("top3") ?: 0
                if(value < model.scorePessoal){
                    v.update("top3",model.scorePessoal)
                    updateTopTimerFirestore("top3")
                    return@addOnSuccessListener
                }

                if(value == model.scorePessoal.toLong()){
                    v.update("top3",model.scorePessoal)
                    updateTimerScoreEquals("top3")
                    return@addOnSuccessListener
                }

                value= it.getLong("top4") ?: 0
                if(value < model.scorePessoal){
                    v.update("top4",model.scorePessoal)
                    updateTopTimerFirestore("top4")
                    return@addOnSuccessListener
                }
                if(value == model.scorePessoal.toLong()){
                    v.update("top4",model.scorePessoal)
                    updateTimerScoreEquals("top4")
                    return@addOnSuccessListener
                }

                value= it.getLong("top5") ?: 0
                if(value < model.scorePessoal){
                    v.update("top5",model.scorePessoal)
                    updateTopTimerFirestore("top5")
                    return@addOnSuccessListener
                }

                if(value == model.scorePessoal.toLong()){
                    v.update("top5",model.scorePessoal)
                    updateTimerScoreEquals("top5")
                    return@addOnSuccessListener
                }
            }
        .addOnFailureListener { e ->
            Log.i(TAG, "updateDataInFirestore: ${e.message}")
        }
    }



    @SuppressLint("SetTextI18n")
    fun startObserver() {
        var top1 = " "
        var top2 = " "
        var top3 = " "
        var top4 = " "
        var top5 = " "

        val db = Firebase.firestore
        db.collection("Scores").document("TopScores")
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

        db.collection("Timers").document("TopTimers")
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

    private fun mudaNivelTimer(){

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.dialogBackTitle)
            .setMessage(R.string.dialogDescription)
            .setCancelable(false)
            .setPositiveButton(
                R.string.dialog_Yes
            ) { _, _ ->
                updateTopScoresFirestore()
                finish()
            }
            .setNegativeButton(R.string.dialog_No) { _, _ ->
                model.mudaNivel = false
                resetClock()
            }
            .setNeutralButton(R.string.pause_game){_ , _ ->
                val dialogPause = AlertDialog.Builder(this)
                    .setTitle(R.string.continue_game_title)
                    .setPositiveButton(R.string.continue_game){ _, _ ->
                        model.mudaNivel = false
                        resetClock()
                    }
                    .create()
                dialogPause.setCanceledOnTouchOutside(false)
                dialogPause.show()
            }
            .create()

        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnShowListener(object : OnShowListener {
            private val AUTO_DISMISS_MILLIS = 5000
            override fun onShow(dialog: DialogInterface) {
                val defaultButton =
                    (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
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
                            model.mudaNivel = false
                            resetClock()
                        }
                    }
                }.start()
            }
        })
        dialog.show()

    }




}


