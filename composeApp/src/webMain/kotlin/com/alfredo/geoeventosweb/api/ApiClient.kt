package com.alfredo.geoeventosweb.api

import com.alfredo.geoeventosweb.model.Evento
import com.alfredo.geoeventosweb.model.EventoDTO
import com.alfredo.geoeventosweb.model.ImagenResponse
import com.geoEventos.BuildKonfig.API_BASE_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {

    // Cambia esta URL si tu API corre en otro puerto
    private val BASE_URL = API_BASE_URL


    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }

    // ── Eventos ───────────────────────────────────────────────────────────

    suspend fun getEventos(): List<Evento> {
        return try {
            client.get("$BASE_URL/api/eventos").body()
        } catch (e: Exception) {
            println("Error al obtener eventos: ${e.message}")
            emptyList()
        }
    }

    suspend fun buscarEventos(query: String): List<Evento> {
        return try {
            client.get("$BASE_URL/api/eventos") {
                parameter("q", query)
            }.body()
        } catch (e: Exception) {
            println("Error al buscar eventos: ${e.message}")
            emptyList()
        }
    }

    suspend fun getEvento(id: Int): Evento? {
        return try {
            client.get("$BASE_URL/api/eventos/$id").body()
        } catch (e: Exception) {
            println("Error al obtener evento $id: ${e.message}")
            null
        }
    }

    suspend fun crearEvento(dto: EventoDTO): Boolean {
        return try {
            val response = client.post("$BASE_URL/api/eventos") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            println("Error al crear evento: ${e.message}")
            false
        }
    }

    suspend fun actualizarEvento(id: Int, dto: EventoDTO): Boolean {
        return try {
            val response = client.put("$BASE_URL/api/eventos/$id") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            println("Error al actualizar evento: ${e.message}")
            false
        }
    }

    suspend fun borrarEvento(id: Int): Boolean {
        return try {
            val response = client.delete("$BASE_URL/api/eventos/$id")
            response.status.isSuccess()
        } catch (e: Exception) {
            println("Error al borrar evento: ${e.message}")
            false
        }
    }

    // ── Imágenes ──────────────────────────────────────────────────────────

    suspend fun subirImagen(fileData: ByteArray, fileName: String): ImagenResponse? {
        return try {
            val response = client.post("$BASE_URL/api/imagenes/subir") {
                setBody(MultiPartFormDataContent(
                    formData {
                        append("archivo", fileData, Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                            append(HttpHeaders.ContentType, "application/octet-stream")
                        })
                    }
                ))
            }
            response.body()
        } catch (e: Exception) {
            println("Error al subir imagen: ${e.message}")
            null
        }
    }
}