package com.example.juego


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.juego.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    var preguntas = 0
    var preguntascorrectas = 0

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val client = OkHttpClient()
        val token = intent.getStringExtra("TOKEN")
        println(token)
        var respuesta:String=""
        val request = Request.Builder()
        request.url("http://10.0.2.2:8086/Pregunta/${token}")

        val call = client.newCall(request.build())
        call.enqueue( object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Usuario ${e.printStackTrace()}")

                println(e.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@MainActivity, "ERROR", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onResponse(call: Call, response: Response) {
                println("Usuario $response")

                response.body?.let { responseBody ->
                    val body = responseBody.string()
                    println(body)
                    val gson = Gson()

                    val quest = gson.fromJson(body, Pregunta::class.java)


                    CoroutineScope(Dispatchers.Main).launch {
                        binding.Preg.setText(preguntas.toString())
                        binding.Correc.setText(preguntascorrectas.toString())
                        binding.pregunta1.setText(quest.respuesta1)
                        binding.pregunta2.setText(quest.respuesta2)
                        binding.pregunta3.setText(quest.respuesta3)
                        binding.pregunta4.setText(quest.respuesta4)
                        binding.textView.setText(quest.pregunta)
                        binding.textView2.setText(quest.id.toString())
                    }
                }
            }
        })

        binding.pregunta1.setOnClickListener {
            trueClick()
            nonClick()
            binding.pregunta1.setBackgroundColor(Color.parseColor("#FFBB86FC"))
            respuesta = binding.pregunta1.text.toString()
        }
        binding.pregunta2.setOnClickListener {
            trueClick()
            nonClick()
            binding.pregunta2.setBackgroundColor(Color.parseColor("#FFBB86FC"))
            respuesta= binding.pregunta2.text.toString()
        }
        binding.pregunta3.setOnClickListener {
            trueClick()
            nonClick()
            binding.pregunta3.setBackgroundColor(Color.parseColor("#FFBB86FC"))
            respuesta= binding.pregunta3.text.toString()
        }
        binding.pregunta4.setOnClickListener {
            trueClick()
            nonClick()
            binding.pregunta4.setBackgroundColor(Color.parseColor("#FFBB86FC"))
            respuesta = binding.pregunta4.text.toString()

        }
        binding.Contestar.setOnClickListener {
            intentos()
            examinador(respuesta,binding.textView2.text.toString(),token)

        }
    }
    fun examinador(respuesta: String, id: String, token: String?){

        val client = OkHttpClient()
        val request = Request.Builder()
        request.url("http://10.0.2.2:8086/Pregunta/${id}/${respuesta}")

        val call = client.newCall(request.build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    Snackbar.make(binding.root,"ERROR", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.toString())
                response.body?.let { responseBody ->
                    val body = responseBody.string()
                    val gson = Gson()


                    CoroutineScope(Dispatchers.Main).launch {
                        Snackbar.make(binding.root,body, Snackbar.LENGTH_LONG).show()
                        print(body)
                        if (body=="\nRespuesta correcta\n"){
                            falseClick()
                            nonClick()
                            aciertos()
                            if (token != null) {
                                obtenerPregunta(token)
                            }
                        }else{
                        binding.progressBar.visibility= View.VISIBLE
                        delay(2000)
                        binding.progressBar.visibility= View.GONE
                        falseClick()
                        nonClick()
                            if (token != null) {
                                obtenerPregunta(token)
                            }
                        }
                    }
                }
            }
        })
    }
    fun obtenerPregunta(token: String) {

        val client = OkHttpClient()
        val request = Request.Builder()
        request.url("http://10.0.2.2:8086/Pregunta/${token}")


        val call = client.newCall(request.build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    Snackbar.make(binding.root,"ERROR",Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.toString())
                response.body?.let { responseBody ->
                    val body = responseBody.string()
                    println(body)
                    val gson = Gson()

                    val quest = gson.fromJson(body, Pregunta::class.java)
                    var respuesta:String=""
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.Preg.setText(preguntas.toString())
                        binding.Correc.setText(preguntascorrectas.toString())
                        binding.pregunta1.setText(quest.respuesta1)
                        binding.pregunta2.setText(quest.respuesta2)
                        binding.pregunta3.setText(quest.respuesta3)
                        binding.pregunta4.setText(quest.respuesta4)
                        binding.textView.setText(quest.pregunta)
                        binding.textView2.setText(quest.id.toString())
                    }
                    binding.pregunta1.setOnClickListener {
                        trueClick()
                        nonClick()
                        binding.pregunta1.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                        respuesta= binding.pregunta1.text.toString()
                    }
                    binding.pregunta2.setOnClickListener {
                        trueClick()
                        nonClick()
                        binding.pregunta2.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                        respuesta= binding.pregunta2.text.toString()
                    }
                    binding.pregunta3.setOnClickListener {
                        trueClick()
                        nonClick()
                        binding.pregunta3.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                        respuesta= binding.pregunta3.text.toString()
                    }
                    binding.pregunta4.setOnClickListener {
                        trueClick()
                        nonClick()
                        binding.pregunta4.setBackgroundColor(Color.parseColor("#FFBB86FC"))
                        respuesta = binding.pregunta4.text.toString()

                    }
                    binding.Contestar.setOnClickListener {
                        examinador(respuesta, binding.textView2.text.toString(), token)
                        intentos()
                        obtenerPregunta(token)




                    }
                }
            }
        })
    }
    private fun trueClick(){
        binding.Contestar.isEnabled = true
    }
    private fun aciertos(){
        preguntascorrectas+=1
    }
    private fun intentos(){
        preguntas += 1
    }
    private fun falseClick(){
        binding.Contestar.isEnabled = false
    }
    private fun nonClick(){
        binding.pregunta1.setBackgroundColor(Color.parseColor("#673AB7"))
        binding.pregunta2.setBackgroundColor(Color.parseColor("#673AB7"))
        binding.pregunta3.setBackgroundColor(Color.parseColor("#673AB7"))
        binding.pregunta4.setBackgroundColor(Color.parseColor("#673AB7"))
    }
}