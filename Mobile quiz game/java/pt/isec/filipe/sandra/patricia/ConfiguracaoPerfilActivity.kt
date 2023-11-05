package pt.isec.filipe.sandra.patricia

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import pt.isec.filipe.sandra.patricia.databinding.ActivityConfiguracaoPerfilBinding
import java.io.*


class ConfiguracaoPerfilActivity : AppCompatActivity() {
    companion object {
        private const val PROFILE_DIR = "LET"
        private const val PROFILE_FILE = "Photo.img"
        private const val TAG = "ConfigImageActivity"
        private const val ACTIVITY_REQUEST_CODE_CAMERA  = 20
        private const val PERMISSIONS_REQUEST_CODE = 1

        fun getCameraIntent(context : Context) : Intent {
            val intent = Intent(context, ConfiguracaoPerfilActivity::class.java)
            return intent
        }
    }

    private lateinit var binding : ActivityConfiguracaoPerfilBinding

    private var imagePath : String? = null

    private var permissionsGranted = false
        set(value) {
            field = value
            binding.btnFoto.isEnabled = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracaoPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnFoto.apply {
            setOnClickListener { takePhoto() }
        }
        binding.btnCriaPerfil.apply {
            setOnClickListener {
                savephotov1()
                finish()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        verifyPermissions()
        uploadPhoto()
        updatePreview()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, info: Intent?) {
        Log.i(TAG, "onActivityResult: ")
        if (requestCode == ACTIVITY_REQUEST_CODE_CAMERA) {
            if (resultCode != RESULT_OK)
                imagePath = null
            updatePreview()
            return
        }
        super.onActivityResult(requestCode, resultCode, info)

    }

    fun takePhoto() {
        imagePath = getTempFilename(this)
        Log.i(TAG, "takePhoto: $imagePath")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            val fileUri = FileProvider.getUriForFile( this,
                "pt.isec.filipe.sandra.patricia.fileprovider", File(imagePath)
            )
            it.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        }
        startActivityForResult(intent, 20)
    }


    fun updatePreview() {
        if (imagePath != null)
            setPic(binding.pic, imagePath!!)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            permissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun verifyPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = false
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        } else
            permissionsGranted = true
    }

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionsGranted = isGranted
    }

    fun uploadPhoto(){
        val path = filesDir
        val letDirectory = File(path, PROFILE_DIR)
        letDirectory.mkdirs()
        val file = File(letDirectory, PROFILE_FILE)
        if(file.exists()){
            val byteArray = FileInputStream(file).readBytes()
            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.pic.setImageBitmap(Bitmap.createBitmap(bmp))
        }
    }


    fun savephotov1(){
        var success = false
        val path = filesDir
        val letDirectory = File(path, PROFILE_DIR)
        letDirectory.mkdirs()
        val file = File(letDirectory, PROFILE_FILE)
        if(file.exists()){
            file.delete()
        }

        try {
            val imageView: ImageView = binding.pic
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageInByte: ByteArray = baos.toByteArray()
           file.appendBytes(imageInByte)
            success = true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (success) {
            Toast.makeText(
                applicationContext, R.string.Perfil_Sucess_saving,
                Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                applicationContext,
                R.string.Perfil_Error_saving, Toast.LENGTH_LONG).show();
        }

    }


}