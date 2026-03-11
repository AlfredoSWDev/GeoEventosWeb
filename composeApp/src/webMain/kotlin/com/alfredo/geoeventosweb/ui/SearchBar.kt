package com.alfredo.geoeventosweb.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle



// Fuente de iconos cargada desde Google Fonts en index.html
@Composable
fun SearchBar(viewModel: EventosViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var texto   by remember { mutableStateOf(uiState.query) }

    fun ejecutarBusqueda() { viewModel.cargarEventos(texto.trim()) }
    fun limpiar()          { texto = ""; viewModel.limpiarBusqueda() }

    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Icono lupa
        Box(modifier = Modifier.padding(end = 10.dp)) {
            IconSearch(tint = MaterialTheme.colorScheme.primary, size = 20.dp)
        }

        // Campo de texto con placeholder manual
        Box(modifier = Modifier.weight(1f)) {
            if (texto.isEmpty()) {
                Text(
                    text     = "Buscar eventos…",
                    fontSize = 14.sp,
                    color    = MaterialTheme.colorScheme.outline
                )
            }
            BasicTextField(
                value           = texto,
                onValueChange   = { texto = it },
                singleLine      = true,
                textStyle       = TextStyle(fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface),
                cursorBrush     = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { ejecutarBusqueda() }),
                modifier        = Modifier.fillMaxWidth()
            )
        }

        // Botón limpiar con icono via Material Symbols
        if (texto.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    .clickable { limpiar() },
                contentAlignment = Alignment.Center
            ) {
                IconClose(
                    tint = MaterialTheme.colorScheme.outline,
                    size = 14.dp
                )
            }
        }
    }
}