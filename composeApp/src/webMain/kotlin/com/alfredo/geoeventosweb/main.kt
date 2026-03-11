package com.alfredo.geoeventosweb

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.alfredo.geoeventosweb.ui.EventosViewModel
import com.alfredo.geoeventosweb.ui.PanelEventos
import com.alfredo.geoeventosweb.ui.SearchBar
import com.alfredo.geoeventosweb.ui.theme.AppTheme
import org.w3c.dom.HTMLElement

@JsFun("(id) => document.getElementById(id)")
private external fun getElementById(id: String): HTMLElement?

@JsFun("(lat, lng, zoom) => { if(window.initMap) window.initMap(lat, lng, zoom); }")
private external fun jsInitMap(lat: Double, lng: Double, zoom: Int)

@JsFun("() => { if(window.hidePanelLoading) window.hidePanelLoading(); }")
private external fun jsHidePanelLoading()

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    val panelDiv  = getElementById("compose-panel")
    val searchDiv = getElementById("compose-search")

    if (panelDiv == null || searchDiv == null) return

    val viewModel = EventosViewModel()

    ComposeViewport(panelDiv) {
        AppTheme {
            PanelEventos(viewModel = viewModel)
        }
    }

    ComposeViewport(searchDiv) {
        AppTheme {
            SearchBar(viewModel = viewModel)
        }
    }

    jsInitMap(-33.45, -70.65, 6)
    jsHidePanelLoading()
}