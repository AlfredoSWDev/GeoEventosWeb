# GeoEventos Web

Cliente web moderno para la plataforma **GeoEventos**, construido con **Kotlin Multiplatform** y **Compose Multiplatform**, compilado a **WebAssembly** (Wasm) para máximo rendimiento en navegadores modernos.

---

## ¿Qué es GeoEventos Web?

GeoEventos Web es el cliente web de la plataforma GeoEventos. Permite a **empresas locales gestionar y promocionar sus eventos** a través de un panel interactivo con mapa integrado, accesible desde cualquier navegador moderno. Consume la **API REST de GeoEventos** para sincronizar datos en tiempo real.

### Modelo de Negocio — B2B

| Actor | Rol | Acceso |
|-------|-----|--------|
| **Empresas / Clientes** | Pagan por publicar y promocionar eventos | Panel web (este proyecto) |
| **Usuarios finales** | Descubren eventos en el mapa | App Android + Web |

---

## 📋 Stack Tecnológico

| Tecnología | Versión | Propósito |
|-----------|---------|----------|
| **Kotlin** | 2.3.0 | Lenguaje base multiplataforma |
| **Compose Multiplatform** | 1.10.0 | Framework declarativo de UI |
| **Kotlin/Wasm** | Latest | Compilación a WebAssembly |
| **Ktor Client** | 3.1.3 | Cliente HTTP para API REST |
| **kotlinx.serialization** | Latest | Serialización JSON nativa |
| **Material Design 3** | 1.10.0-alpha05 | Componentes UI modernos |
| **Gradle** | Latest | Build system |

---

## 🏗️ Arquitectura

```
┌──────────────────────────────────────────┐
│       GeoEventos Web (este proyecto)     │
│   Kotlin/Wasm + Compose Multiplatform    │
│                                          │
│  ▲ Mapa Interactivo + Panel de Gestión   │
│  │ Material Design 3                     │
└──┼───────────────────────────────────────┘
   │
   │  HTTP REST (JSON)
   │  Ktor Client
   │
   ▼
┌──────────────────────────────────────────┐
│       GeoEventosAPI                      │
│    Spring Boot - puerto 8080             │
│                                          │
│  /api/eventos (CRUD)                     │
│  /api/eventos/buscar                     │
└──────────────────────────────────────────┘
```

---

## 📁 Estructura del Proyecto

```
GeoEventosWeb/
├── composeApp/                 # Aplicación Compose Multiplatform
│   ├── src/
│   │   ├── wasmJsMain/         # Código específico para WebAssembly
│   │   ├── commonMain/         # Código compartido (lógica, modelos, ViewModels)
│   │   ├── webMain/            # Recursos web (HTML, CSS, SVG)
│   │   └── webTest/            # Tests para web
│   ├── build.gradle.kts        # Configuración de build
│   └── build/                  # Output compilado
├── gradle/
│   └── libs.versions.toml      # Versiones y dependencias centralizadas
├── build.gradle.kts            # Configuración raíz del proyecto
├── settings.gradle.kts         # Configuración de submódulos
└── README.md                   # Documentación del proyecto
```

### Directorios principales

- **commonMain**: Lógica compartida, modelos DTO, ViewModels, servicios HTTP
- **wasmJsMain**: Punto de entrada específico para WebAssembly
- **webMain**: Recursos estáticos (index.html, estilos, imágenes)
- **webTest**: Tests unitarios e integración

---

## ⚙️ Requisitos Previos

- **Java Development Kit (JDK)** 23+
- **Git** (para clonar/versionar)
- **Navegador moderno** con soporte WebAssembly:
  - Chrome 74+
  - Firefox 79+
  - Safari 14.1+
  - Edge 79+

---

## 🚀 Cómo Levantar el Proyecto

### 1️⃣ Levantar la API (requerida)

La aplicación web consume datos de **GeoEventosAPI**. Debes tenerla corriendo primero.

```bash
cd ../GeoEventosAPI
./gradlew bootRun
# La API estará en http://localhost:8080
```

### 2️⃣ Ejecutar GeoEventos Web en desarrollo (Wasm - Recomendado)

```bash
cd GeoEventosWeb
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

La aplicación se abrirá automáticamente en `http://localhost:8080`.

**Características del modo desarrollo:**
- Hot reload en tiempo real
- Debugging en navegador
- Mejor rendimiento que JavaScript puro

### 3️⃣ Alternativa: Ejecutar con JavaScript (navegadores antiguos)

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

---

## 🔨 Compilación para Producción

### Compilación Wasm (Recomendada)

```bash
./gradlew :composeApp:wasmJsBrowserDistribution
```

Salida: `composeApp/build/dist/wasmJs/productionExecutable/`

### Compilación JavaScript

```bash
./gradlew :composeApp:jsBrowserDistribution
```

Salida: `composeApp/build/dist/js/productionExecutable/`

---

## 🛠️ Desarrollo

### Agregar Dependencias

Las dependencias se gestionan en `gradle/libs.versions.toml`:

```toml
[versions]
miLibreria = "1.0.0"

[libraries]
mi-libreria = { module = "com.ejemplo:mi-libreria", version.ref = "miLibreria" }
```

Luego actualiza `composeApp/build.gradle.kts` y sincroniza.

### Ejecutar Tests

```bash
./gradlew :composeApp:wasmJsTest
```

### Estructura de Código Recomendada

```
commonMain/
├── kotlin/com/geoEventos/
│   ├── ui/                    # Composables y screens
│   ├── viewmodel/             # ViewModels y estado
│   ├── data/
│   │   ├── api/               # Servicios HTTP (Ktor)
│   │   ├── model/             # DTOs y respuestas
│   │   └── repository/        # Lógica de datos
│   └── utils/                 # Utilidades compartidas
```

---

## 🔌 Integración con GeoEventosAPI

La aplicación se conecta a `/api/eventos` de la API REST:

```kotlin
// Ejemplo de cliente HTTP
val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

val eventos = client.get("http://localhost:8080/api/eventos")
```

**Endpoints utilizados:**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/eventos` | Listar todos los eventos |
| `GET` | `/api/eventos/{id}` | Obtener evento por ID |
| `POST` | `/api/eventos` | Crear nuevo evento |
| `PUT` | `/api/eventos/{id}` | Actualizar evento |
| `DELETE` | `/api/eventos/{id}` | Eliminar evento |
| `GET` | `/api/eventos/buscar?q=...` | Buscar por nombre |

---

## 📚 Funcionalidades

- ✅ Mapa interactivo con marcadores de eventos
- ✅ Click en mapa para asignar ubicación a nuevo evento
- ✅ CRUD completo de eventos (crear, leer, actualizar, borrar)
- ✅ Búsqueda de eventos por nombre
- ✅ Panel de gestión con Material Design 3
- ✅ Compilación a WebAssembly para máximo rendimiento
- ✅ Responsive design para desktop/tablet
- ⏳ Autenticación JWT (roadmap)
- ⏳ Filtros por categoría, fecha, distancia (roadmap)

---

## 🌐 Navegadores Soportados

| Navegador | Versión Mínima | WebAssembly |
|-----------|----------------|------------|
| Chrome | 74+ | ✅ |
| Firefox | 79+ | ✅ |
| Safari | 14.1+ | ✅ |
| Edge | 79+ | ✅ |

---

## 📦 Dependencias Principales

- **Compose Runtime & UI**: Construcción declarativa de UI
- **Ktor Client**: HTTP client multiplataforma
- **kotlinx.serialization**: Serialización JSON
- **Material 3**: Componentes de UI modernos
- **Lifecycle**: ViewModel y estado

---

## 🔧 Troubleshooting

### La aplicación no inicia

Verifica que **GeoEventosAPI está corriendo**:
```bash
# En otra terminal
cd ../GeoEventosAPI
./gradlew bootRun
```

### WebAssembly no se compila

Asegúrate de tener JDK 23+:
```bash
java -version
```

Si sigue fallando, usa el target JavaScript:
```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

### Puerto 8080 ya está en uso

Si la API también usa 8080, Gradle usará automáticamente otro puerto. Revisa la salida de consola.

---

## 📚 Recursos Útiles

- [Kotlin Multiplatform Docs](https://www.jetbrains.com/help/kotlin-multiplatform-dev/)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
- [Kotlin/Wasm Guide](https://kotlinlang.org/docs/wasm-get-started.html)
- [Ktor Client Docs](https://ktor.io/docs/client.html)
- [Material Design 3](https://m3.material.io/)

---

## Proyecto Padre

Este proyecto es parte de **GeoEventos** — una plataforma completa de eventos geolocalizados.

**Repositorios relacionados:**

| Proyecto | Tecnología | Rol |
|----------|-----------|-----|
| [GeoEventos](https://github.com/AlfredoSWDev/GeoEventos) | Monorepo | Coordinador principal |
| [GeoEventosAPI](https://github.com/AlfredoSWDev/GeoEventosAPI) | Spring Boot | Backend REST |
| [GeoEventosGUI](https://github.com/AlfredoSWDev/GeoEventosGUI) | Java Swing | Cliente desktop |
| [GeoEventosAndroid](https://github.com/AlfredoSWDev/GeoEventosAndroid) | Kotlin Compose | App móvil |
| **GeoEventosWeb** (este) | Kotlin/Wasm | Cliente web |

---

## 🚀 Roadmap

- [ ] Autenticación JWT con GeoEventosAPI
- [ ] Filtros por categoría, fecha y proximidad
- [ ] Notificaciones en tiempo real (WebSocket)
- [ ] Estadísticas y análisis de eventos
- [ ] Integración PWA (instalable en home)
- [ ] Modo offline con sincronización
- [ ] Despliegue en Vercel / AWS

---

## Desarrollado por

**Alfredo** — [github.com/AlfredoSWDev](https://github.com/AlfredoSWDev)

**Última actualización:** Marzo 2026
