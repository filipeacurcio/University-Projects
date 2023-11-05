package pt.isec.filipe.sandra.patricia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

private const val TAG = "CreditsActivity"


class CreditsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

