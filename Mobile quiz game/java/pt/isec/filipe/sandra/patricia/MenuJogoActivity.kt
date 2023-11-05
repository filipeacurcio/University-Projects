package pt.isec.filipe.sandra.patricia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuJogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_jogo)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val btnSair: Button = findViewById(R.id.btnSairMenuJogo)
         btnSair.setOnClickListener {
             finish()
         }

         val button: Button = findViewById(R.id.btnSinglePlayer)
         button.setOnClickListener {
             val singlePlayerGameActivity = Intent(this, SinglePlayerGameActivity::class.java)
             startActivity(singlePlayerGameActivity)
         }

        val buttonMj : Button = findViewById(R.id.btnMultiPlayer)
        buttonMj.setOnClickListener {
            val mutliplayerGameActivity = Intent(this, MultiPlayerServerOrClientActivity::class.java)
            startActivity(mutliplayerGameActivity)
        }

    }



}