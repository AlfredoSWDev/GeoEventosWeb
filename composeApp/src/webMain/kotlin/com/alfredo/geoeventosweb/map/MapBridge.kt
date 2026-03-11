package com.alfredo.geoeventosweb.map

// ── Interop JS → Kotlin/WASM ─────────────────────────────────────────────────
// El click en un pin NO se pasa como callback (WASM no puede pasar lambdas a JS).
// En su lugar JS escribe el id en window.__lastPinClick y Kotlin lo consume
// con polling via consumePinClick().

@JsFun("(id, lat, lng, title) => { if(window.addMarker) window.addMarker(id, lat, lng, title); }")
private external fun jsAddMarker(id: Int, lat: Double, lng: Double, title: String)

@JsFun("(id) => { if(window.removeMarker) window.removeMarker(id); }")
private external fun jsRemoveMarker(id: Int)

@JsFun("() => { if(window.clearMarkers) window.clearMarkers(); }")
private external fun jsClearMarkers()

@JsFun("(lat, lng) => { if(window.flyToMarker) window.flyToMarker(lat, lng); }")
private external fun jsFlyTo(lat: Double, lng: Double)

/**
 * Lee y resetea el último pin clickeado.
 * Devuelve -1 si no hubo click desde la última llamada.
 */
@JsFun("() => (window.consumePinClick ? window.consumePinClick() : -1)")
external fun consumePinClick(): Int

// ─────────────────────────────────────────────────────────────────────────────

object MapBridge {

    fun addMarker(id: Int, lat: Double, lng: Double, title: String) =
        jsAddMarker(id, lat, lng, title)

    fun removeMarker(id: Int) = jsRemoveMarker(id)

    fun clearMarkers() = jsClearMarkers()

    fun flyTo(lat: Double, lng: Double) = jsFlyTo(lat, lng)

    fun refreshMarkers(eventos: List<MarkerData>) {
        clearMarkers()
        eventos.forEach { data ->
            if (data.lat != null && data.lng != null) {
                addMarker(data.id, data.lat, data.lng, data.title)
            }
        }
    }
}

data class MarkerData(
    val id:    Int,
    val lat:   Double?,
    val lng:   Double?,
    val title: String
)