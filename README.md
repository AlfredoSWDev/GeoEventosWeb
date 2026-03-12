# GeoEventosWeb

**Módulo de Web para GeoEventos**

**Introducción**
================

Este proyecto es un módulo de web para GeoEventos, una plataforma de eventos geolocalizados. El objetivo es crear un cliente web para eventos, que permita a los usuarios explorar, crear, leer, actualizar y eliminar eventos.

**Características**
----------------

* Mapa interactivo con marcadores de eventos
* Click en mapa para asignar ubicación a nuevo evento
* CRUD completo de eventos (crear, leer, actualizar, borrar)
* Búsqueda de eventos por nombre
* Panel de gestión con Material Design 3
* Compilación a WebAssembly para máximo rendimiento
* Responsive design para desktop/tablet

**Dependencias**
--------------

* Compose Runtime & UI
* Ktor Client
* Kotlinx.serialization
* Material 3
* Lifecycle

**Requisitos**
------------

* Java Development Kit (JDK) 23+
* Git (para clonar/versionar)
* Navegador moderno con soporte WebAssembly:
  - Chrome 74+
  - Firefox 79+
  - Safari 14.1+
  - Edge 79+

**Cómo levantar el proyecto**
---------------------------

### 1️⃣ Levantar la API (requerida)

La aplicación web consume datos de GeoEventosAPI. Debes tenerla corriendo primero.

```bash
# En otra terminal
cd ../GeoEventosAPI
./gradlew bootRun
```

### 2️⃣ Ejecutar GeoEventos Web en desarrollo (Wasm - Recomendado)

```bash
cd GeoEventosWeb
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

La aplicación se abrirá automáticamente en `http://localhost:8080`.

**Cómo compilar para producción**
--------------------------------

### Compilación Wasm (Recomendada)

```bash
./gradlew :composeApp:wasmJsBrowserDistribution
```

Salida: `composeApp/build/dist/wasmJs/productionExecutable/`

**Troubleshooting**
--------------------

### La aplicación no inicia

Verifica que GeoEventosAPI está corriendo:

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

**Repositorios relacionados**
---------------------------

| Proyecto | Tecnología | Rol |
|----------|-----------|-----|
| [GeoEventos](https://github.com/AlfredoSWDev/GeoEventos) | Monorepo | Coordinador principal |
| [GeoEventosAPI](https://github.com/AlfredoSWDev/GeoEventosAPI) | Spring Boot | Backend REST |
| [GeoEventosGUI](https://github.com/AlfredoSWDev/GeoEventosGUI) | Java Swing | Cliente desktop |
| [GeoEventosAndroid](https://github.com/AlfredoSWDev/GeoEventosAndroid) | Kotlin Compose | App móvil |
| **GeoEventosWeb** (este) | Kotlin/Wasm | Cliente web |

**Roadmap**
----------

* [ ] Autenticación JWT con GeoEventosAPI
* [ ] Filtros por categoría, fecha y proximidad
* [ ] Notificaciones en tiempo real (WebSocket)
* [ ] Estadísticas y análisis de eventos
* [ ] Integración PWA (instalable en home)
* [ ] Modo offline con sincronización
* [ ] Despliegue en Vercel / AWS

**Desarrollado por**
-------------------

**Alfredo** — [github.com/AlfredoSWDev](https://github.com/AlfredoSWDev)

**Última actualización:** Marzo 2026

