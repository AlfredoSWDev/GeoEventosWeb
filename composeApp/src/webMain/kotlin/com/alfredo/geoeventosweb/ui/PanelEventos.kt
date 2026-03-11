package com.alfredo.geoeventosweb.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alfredo.geoeventosweb.map.MapBridge
import com.alfredo.geoeventosweb.map.MarkerData
import com.alfredo.geoeventosweb.map.consumePinClick
import com.alfredo.geoeventosweb.model.Evento
import kotlinx.coroutines.delay

// ── Colores semánticos fijos (vigencia) ──────────────────────────────────────
private val ColorVigente    = Color(0xFF22C55E)
private val ColorNoVigente  = Color(0xFFEF4444)

// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun PanelEventos(viewModel: EventosViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var eventoSeleccionado by remember { mutableStateOf<Evento?>(null) }

    // Refrescar pines cada vez que cambia la lista de eventos
    LaunchedEffect(uiState.eventos) {
        MapBridge.refreshMarkers(
            eventos = uiState.eventos.map { e ->
                MarkerData(
                    id    = e.eventId,
                    lat   = e.latitud,
                    lng   = e.longitud,
                    title = e.nombreEvento
                )
            }
        )
    }

    // Polling: detectar clicks en pines del mapa cada 300ms
    LaunchedEffect(uiState.eventos) {
        while (true) {
            delay(300)
            val pinId = consumePinClick()
            if (pinId != -1) {
                val evento = uiState.eventos.find { it.eventId == pinId }
                if (evento != null) {
                    eventoSeleccionado = evento
                    if (evento.latitud != null && evento.longitud != null) {
                        MapBridge.flyTo(evento.latitud, evento.longitud)
                    }
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = MaterialTheme.colorScheme.surface
    ) {
        AnimatedContent(
            targetState = eventoSeleccionado,
            transitionSpec = {
                if (targetState != null) {
                    // Lista → Detalle: entra por la derecha
                    (slideInHorizontally { it } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it } + fadeOut())
                } else {
                    // Detalle → Lista: vuelve por la izquierda
                    (slideInHorizontally { -it } + fadeIn()) togetherWith
                            (slideOutHorizontally { it } + fadeOut())
                }
            },
            label = "panel_transition"
        ) { seleccionado ->
            if (seleccionado == null) {
                PanelLista(
                    uiState   = uiState,
                    onClickEvento = { evento ->
                        eventoSeleccionado = evento
                        // Volar el mapa al pin seleccionado
                        if (evento.latitud != null && evento.longitud != null) {
                            MapBridge.flyTo(evento.latitud, evento.longitud)
                        }
                    }
                )
            } else {
                PanelDetalle(
                    evento  = seleccionado,
                    onVolver = { eventoSeleccionado = null }
                )
            }
        }
    }
}

// ── Vista de lista ────────────────────────────────────────────────────────────

@Composable
private fun PanelLista(
    uiState: EventosUiState,
    onClickEvento: (Evento) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Cabecera
        PanelHeader(titulo = "Eventos", cantidad = uiState.eventos.size)

        when {
            uiState.cargando -> EstadoCargando()
            uiState.error != null -> EstadoError(mensaje = uiState.error)
            uiState.eventos.isEmpty() -> EstadoVacio(query = uiState.query)
            else -> {
                LazyColumn(
                    modifier            = Modifier.fillMaxSize(),
                    contentPadding      = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.eventos,
                        key   = { it.eventId }
                    ) { evento ->
                        TarjetaEvento(evento = evento, onClick = { onClickEvento(evento) })
                    }
                }
            }
        }
    }
}

// ── Tarjeta individual en la lista ────────────────────────────────────────────

@Composable
private fun TarjetaEvento(evento: Evento, onClick: () -> Unit) {
    val esVigente = evento.vigenciaEvento
        ?.trim()
        ?.lowercase()
        ?.contains("vigente") == true

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        color  = MaterialTheme.colorScheme.surfaceContainerLow,
        shape  = RoundedCornerShape(10.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Nombre + vigencia
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = evento.nombreEvento,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onSurface,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment    = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Pill de vigencia
                    if (!evento.vigenciaEvento.isNullOrBlank()) {
                        ChipVigencia(texto = evento.vigenciaEvento, vigente = esVigente)
                    }
                    // Valor
                    if (!evento.valorEvento.isNullOrBlank()) {
                        Text(
                            text     = evento.valorEvento,
                            fontSize = 12.sp,
                            color    = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }

            // Flecha indicadora
            Spacer(modifier = Modifier.width(8.dp))
            IconChevronRight(tint = MaterialTheme.colorScheme.outline, size = 16.dp)
        }
    }
}

// ── Pill de vigencia ──────────────────────────────────────────────────────────

@Composable
private fun ChipVigencia(texto: String, vigente: Boolean) {
    val bg    = if (vigente) ColorVigente.copy(alpha = 0.12f) else ColorNoVigente.copy(alpha = 0.12f)
    val color = if (vigente) ColorVigente                      else ColorNoVigente

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text       = texto,
            fontSize   = 11.sp,
            fontWeight = FontWeight.Medium,
            color      = color,
            maxLines   = 1
        )
    }
}

// ── Vista de detalle ──────────────────────────────────────────────────────────

@Composable
private fun PanelDetalle(evento: Evento, onVolver: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Cabecera con botón volver
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick        = onVolver,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    IconBack(tint = MaterialTheme.colorScheme.primary, size = 16.dp)
                    Text(text = "Volver", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

        // Contenido del detalle
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                // Nombre
                Text(
                    text       = evento.nombreEvento,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onSurface
                )
            }

            // Imagen si existe
            if (!evento.fotosEvento.isNullOrBlank()) {
                item {
                    // Placeholder visual hasta que se implemente carga de imagen real
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text     = "Sin imagen",
                            color    = MaterialTheme.colorScheme.outline,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Campos de información
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilaDetalle(etiqueta = "Lugar",       valor = evento.lugarEvento)
                    FilaDetalle(etiqueta = "Vigencia",    valor = evento.vigenciaEvento)
                    FilaDetalle(etiqueta = "Valor",       valor = evento.valorEvento)
                    FilaDetalle(etiqueta = "Descripción", valor = evento.descripcionEvento)
                    if (evento.latitud != null && evento.longitud != null) {
                        FilaDetalle(
                            etiqueta = "Coordenadas",
                            valor    = "${truncar5(evento.latitud)}, ${truncar5(evento.longitud)}"
                        )
                    }
                }
            }
        }
    }
}

// ── Fila etiqueta + valor en el detalle ───────────────────────────────────────

@Composable
private fun FilaDetalle(etiqueta: String, valor: String?) {
    if (valor.isNullOrBlank()) return
    Column {
        Text(
            text       = etiqueta,
            fontSize   = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color      = MaterialTheme.colorScheme.outline,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text     = valor,
            fontSize = 14.sp,
            color    = MaterialTheme.colorScheme.onSurface
        )
    }
}

// ── Cabecera del panel ────────────────────────────────────────────────────────

@Composable
private fun PanelHeader(titulo: String, cantidad: Int) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text       = titulo,
            fontSize   = 17.sp,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onSurface
        )
        if (cantidad > 0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text       = "$cantidad",
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
}

// ── Estados vacío / cargando / error ─────────────────────────────────────────

@Composable
private fun EstadoCargando() {
    Box(
        modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color       = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.5.dp,
            modifier    = Modifier.size(36.dp)
        )
    }
}

@Composable
private fun EstadoError(mensaje: String) {
    Box(
        modifier         = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "warning", fontSize = 32.sp)
            Text(
                text      = mensaje,
                fontSize  = 13.sp,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun EstadoVacio(query: String) {
    Box(
        modifier         = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "location_on", fontSize = 32.sp)
            Text(
                text      = if (query.isBlank()) "No hay eventos disponibles"
                else "Sin resultados para \"$query\"",
                fontSize  = 13.sp,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// ── Utilidad: trunca un Double a 5 decimales sin String.format (no disponible en WASM) ──
private fun truncar5(valor: Double): String {
    val factor   = 100_000.0
    val truncado = kotlin.math.round(valor * factor) / factor
    val entero   = truncado.toLong()
    val decPart  = kotlin.math.abs((truncado - entero) * factor).toLong()
    val decStr   = decPart.toString().padStart(5, '0')
    return if (valor < 0 && entero == 0L) "-0.$decStr" else "$entero.$decStr"
}