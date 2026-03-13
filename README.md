# GeoEventosWeb 🌐

Frontend web del ecosistema **GeoEventos** — plataforma B2B de gestión de eventos con geolocalización.

Construido con **Kotlin Multiplatform + Compose for Web (Wasm)**, desplegado en GitHub Pages y conectado a una API REST en Render.

🔗 **[Ver app en producción](https://alfredoswdev.github.io/GeoEventosWeb/)**

---

## Stack

| Capa | Tecnología |
|------|-----------|
| UI | Compose Multiplatform 1.10.0 (Wasm) |
| HTTP | Ktor Client 3.1.3 |
| Serialización | kotlinx.serialization 1.8.1 |
| Mapa | Leaflet.js + OpenStreetMap |
| Build | Gradle 9.3.1 + BuildKonfig 0.15.2 |
| CI/CD | GitHub Actions → GitHub Pages |
| API | Spring Boot (GeoEventosAPI en Render) |

---

## Arquitectura

```
GeoEventosWeb/
├── composeApp/
│   └── src/
│       └── webMain/kotlin/
│           ├── api/          # ApiClient (Ktor)
│           ├── map/          # MapBridge (JS interop con Leaflet)
│           ├── model/        # Evento, EventoDTO, ImagenResponse
│           ├── ui/           # PanelEventos, SearchBar, ViewModel
│           │   └── theme/    # Material 3 (light/dark)
│           └── main.kt       # ComposeViewport entry point
├── .env                      # Variables locales (no se commitea)
├── .env.example              # Plantilla de variables
└── .github/workflows/
    └── deploy.yml            # Build + deploy automático
```

**Comunicación Kotlin/Wasm ↔ JS:** El mapa Leaflet corre en JS puro. Kotlin se comunica via `@JsFun` — los clicks en pines se leen con polling desde `window.__lastPinClick` porque WASM no puede pasar lambdas a JS directamente.

---

## Variables de entorno

El proyecto usa **BuildKonfig** para inyectar variables del `.env` en tiempo de build.

Crea un archivo `.env` en la raíz del proyecto:

```env
API_BASE_URL=https://tu-api.onrender.com
```

Las variables quedan disponibles en Kotlin como:

```kotlin
import com.geoEventos.BuildKonfig

val url = BuildKonfig.API_BASE_URL
```

Para producción (GitHub Actions), configura el secret `API_BASE_URL` en:
**Settings → Secrets and variables → Actions**

---

## Correr localmente

```bash
# Clonar
git clone git@github.com:AlfredoSWDev/GeoEventosWeb.git
cd GeoEventosWeb

# Crear .env
echo "API_BASE_URL=http://localhost:8080" > .env

# Dev server con hot reload
./gradlew wasmJsBrowserDevelopmentRun
```

La app abre en `http://localhost:8080`.

---

## Build de producción

```bash
./gradlew composeApp:wasmJsBrowserDistribution
```

Los archivos se generan en:
```
composeApp/build/dist/wasmJs/productionExecutable/
```

---

## Deploy

Cada push a `main` dispara el workflow de GitHub Actions que:

1. Crea el `.env` desde el secret `API_BASE_URL`
2. Ejecuta `wasmJsBrowserDistribution`
3. Publica en la rama `gh-pages`

---

## Parte del ecosistema GeoEventos

| Repositorio | Descripción |
|-------------|-------------|
| [GeoEventosAPI](https://github.com/AlfredoSWDev/GeoEventosAPI) | Spring Boot 4 + PostgreSQL |
| [GeoEventosAndroid](https://github.com/AlfredoSWDev/GeoEventosAndroid) | Kotlin + Jetpack Compose + OSMDroid |
| [GeoEventosGUI](https://github.com/AlfredoSWDev/GeoEventosGUI) | Java + Swing + JavaFX |
| **GeoEventosWeb** | Kotlin/Wasm + Compose for Web ← estás aquí |

---

## Autor

**Alfredo** — [@AlfredoSWDev](https://github.com/AlfredoSWDev)

📺 Stream de desarrollo en [Twitch](https://twitch.tv/AlfredoSWDev) · [YouTube](https://youtube.com/@AlfredoSWDev)