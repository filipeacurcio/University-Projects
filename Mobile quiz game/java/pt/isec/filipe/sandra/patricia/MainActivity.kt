package pt.isec.filipe.sandra.patricia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val buttonCamera: Button = findViewById(R.id.btnCamera) //Botão para criar perfil
        buttonCamera.setOnClickListener {
        startActivity(ConfiguracaoPerfilActivity.getCameraIntent(this))
        }

        val buttonIniciar: Button = findViewById(R.id.btnIniciar) //Botão para ir pra o menu jogo
        buttonIniciar.setOnClickListener {
            val menu_jogo = Intent(this, MenuJogoActivity::class.java)
            startActivity(menu_jogo)
        }

        val buttonSair: Button = findViewById(R.id.btnSair)
        buttonSair.setOnClickListener {
            finish()
        }
        val buttonCreditos : Button = findViewById(R.id.btnCredits)
        buttonCreditos.setOnClickListener {
            val creditos = Intent(this, CreditsActivity::class.java)
            startActivity(creditos)
        }

    }


}