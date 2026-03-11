package com.alfredo.geoeventosweb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfredo.geoeventosweb.api.ApiClient
import com.alfredo.geoeventosweb.model.Evento
import com.alfredo.geoeventosweb.model.EventoDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ── Estado de la pantalla principal ──────────────────────────────────────

data class EventosUiState(
    val eventos: List<Evento> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null,
    val query: String = ""
)

// ── ViewModel ─────────────────────────────────────────────────────────────

class EventosViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EventosUiState())
    val uiState: StateFlow<EventosUiState> = _uiState.asStateFlow()

    init {
        cargarEventos()
    }

    // ── Carga / búsqueda ──────────────────────────────────────────────────

    fun cargarEventos(query: String = _uiState.value.query) {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null, query = query) }
            try {
                val lista = if (query.isBlank()) {
                    ApiClient.getEventos()
                } else {
                    ApiClient.buscarEventos(query)
                }
                _uiState.update { it.copy(eventos = lista, cargando = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(cargando = false, error = "Error al cargar eventos: ${e.message}")
                }
            }
        }
    }

    fun limpiarBusqueda() {
        cargarEventos("")
    }

    // ── CRUD ──────────────────────────────────────────────────────────────

    fun crearEvento(dto: EventoDTO, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val ok = ApiClient.crearEvento(dto)
            if (ok) cargarEventos()
            else _uiState.update { it.copy(cargando = false) }
            onResult(ok)
        }
    }

    fun actualizarEvento(id: Int, dto: EventoDTO, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val ok = ApiClient.actualizarEvento(id, dto)
            if (ok) cargarEventos()
            else _uiState.update { it.copy(cargando = false) }
            onResult(ok)
        }
    }

    fun borrarEvento(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val ok = ApiClient.borrarEvento(id)
            if (ok) cargarEventos()
            else _uiState.update { it.copy(cargando = false) }
            onResult(ok)
        }
    }

    fun limpiarError() {
        _uiState.update { it.copy(error = null) }
    }
}