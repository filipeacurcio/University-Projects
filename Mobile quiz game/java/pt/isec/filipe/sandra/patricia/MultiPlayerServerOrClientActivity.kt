package pt.isec.filipe.sandra.patricia

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels

class MultiPlayerServerOrClientActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_player_server_or_client)

        findViewById<Button>(R.id.btnServer).setOnClickListener {
            startActivity(MultiplayerGameActivity.getServerModeIntent(this))
        }
        findViewById<Button>(R.id.btnClient).setOnClickListener {
            startActivity(MultiplayerGameActivity.getClientModeIntent(this))
        }


        if(!checkForInternet(this)){
            Toast.makeText(this@MultiPlayerServerOrClientActivity, R.string.No_internet,
                Toast.LENGTH_LONG).show()
            finish()
        }



    }


    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {

            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}