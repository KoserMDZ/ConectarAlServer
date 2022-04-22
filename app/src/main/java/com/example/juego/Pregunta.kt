package com.example.juego

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pregunta(
                    var id: Int,
                    var pregunta: String,
                    var respuesta1: String,
                    var respuesta2: String,
                    var respuesta3: String,
                    var respuesta4: String,
                    var respuestaCorrecta: String,

                    ):Parcelable
