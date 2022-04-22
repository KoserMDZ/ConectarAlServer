package com.example.juego

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.juego.databinding.InicioSesionBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class InicioSesionActivity :AppCompatActivity() {

    private lateinit var binding: InicioSesionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InicioSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.usertext.doAfterTextChanged {
            validateUserAndPassword()
        }

        binding.passwdtext.doAfterTextChanged {
            validateUserAndPassword()
        }
    }

    private fun validateUserAndPassword() {

        if(isUserOk() && isPasswordOk()){
            binding.seguir.visibility = View.VISIBLE
        }else{
            binding.seguir.visibility = View.GONE
        }

        binding.seguir.setOnClickListener{ val passcifrada= cifrar(binding.passwdtext.text.toString(),getToken(10))
            registerUser(binding.usertext.text.toString(),passcifrada)

        }
    }

    private fun registerUser(user: String, password: String){

        val client = OkHttpClient()
        val request = Request.Builder()
        request.url("http://10.0.2.2:8086/Users/${user}/${password}")


        val call = client.newCall(request.build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    Snackbar.make(binding.root,"No se ha podido registrar al usuario",Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.toString())
                response.body?.let { responseBody ->
                    val body = responseBody.string()

                    val gson = Gson()

                    val token = gson.fromJson(body, String::class.java)

                    CoroutineScope(Dispatchers.Main).launch {
                        Snackbar.make(binding.root,"El usuario ha sido registrado con el token $token",Snackbar.LENGTH_LONG).show()
                        delay(2000)
                        val intent = Intent(this@InicioSesionActivity, MainActivity::class.java)
                        intent.putExtra("TOKEN", token)
                        startActivity(intent)
                        }
                }
            }
        })
    }

    private fun isUserOk() : Boolean {

        return true
    }

    private fun isPasswordOk() : Boolean{

        return  true
    }

    private fun cifrar(textoEnString : String, llaveEnString : String) : String {
        println("Voy a cifrar: $textoEnString")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getKey(llaveEnString))
        val textCifrado = android.util.Base64.encodeToString(cipher.doFinal(textoEnString.toByteArray(Charsets.UTF_8)), android.util.Base64.URL_SAFE)

        println("He obtenido $textCifrado")
        return textCifrado
    }


    private fun getKey(llaveEnString : String): SecretKeySpec {
        var llaveUtf8 = llaveEnString.toByteArray(Charsets.UTF_8)
        val sha = MessageDigest.getInstance("SHA-1")
        llaveUtf8 = sha.digest(llaveUtf8)
        llaveUtf8 = llaveUtf8.copyOf(16)
        return SecretKeySpec(llaveUtf8, "AES")
    }

    fun getToken(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }


}


