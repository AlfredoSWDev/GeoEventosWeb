package com.alfredo.geoeventosweb

// App.kt — punto de entrada Compose del proyecto.
// La composición real ocurre en main.kt, que monta PanelEventos y SearchBar
// directamente en sus divs del DOM via ComposeViewport.
// Este archivo queda como raíz vacía por si en el futuro se necesita
// un contexto Compose global (tema, navegación, etc.).

import androidx.compose.runtime.Composable

@Composable
fun App() {
    // Intencionalmente vacío.
    // Los composables se montan directamente desde main.kt:
    //   ComposeViewport(panelDiv)  { PanelEventos(...) }
    //   ComposeViewport(searchDiv) { SearchBar(...)     }
}