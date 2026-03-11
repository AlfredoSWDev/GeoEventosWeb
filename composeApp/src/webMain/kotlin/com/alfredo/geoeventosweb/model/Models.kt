package com.alfredo.geoeventosweb.model

import kotlinx.serialization.Serializable

@Serializable
data class Evento(
    val eventId: Int = 0,
    val nombreEvento: String = "",
    val fotosEvento: String? = null,
    val descripcionEvento: String? = null,
    val vigenciaEvento: String? = null,
    val valorEvento: String? = null,
    val lugarEvento: String = "",
    val latitud: Double? = null,
    val longitud: Double? = null
)

@Serializable
data class EventoDTO(
    val nombreEvento: String,
    val fotosEvento: String? = null,
    val descripcionEvento: String? = null,
    val vigenciaEvento: String? = null,
    val valorEvento: String? = null,
    val lugarEvento: String,
    val latitud: Double? = null,
    val longitud: Double? = null
)

@Serializable
data class ImagenResponse(
    val success: Boolean,
    val url: String? = null,
    val mensaje: String? = null
)