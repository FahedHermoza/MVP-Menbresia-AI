# PLAN: Actualización Manual de Ubicación en el Feed

## Enfoque Técnico

### Principio de Diseño
Cada historia de usuario (US-006-01, US-006-02, US-006-03) toca una capa diferente, lo que permite desarrollo en paralelo:
- **US-006-01** → solo UI: `VenueOverlay.kt`
- **US-006-02** → solo UI: nuevo `LocationUpdateBottomSheet.kt`
- **US-006-03** → ViewModel + wiring: `VibeFeedViewModel.kt`, `State.kt`, `Event.kt`

---

### Cambios por Capa

#### Capa de Presentación — Estado (State.kt)
Añadir dos campos al estado existente:
```kotlin
data class State(
    // ... campos existentes ...
    val showLocationSheet: Boolean = false,        // US-006-01 / 02
    val isRefreshingLocation: Boolean = false,     // US-006-03
    val locationRefreshError: String? = null       // US-006-03
)
```

#### Capa de Presentación — Eventos (Event.kt)
Añadir tres eventos nuevos:
```kotlin
sealed class Event {
    // ... eventos existentes ...
    data object LocationChipClicked : Event()          // US-006-01
    data object LocationBottomSheetDismissed : Event() // US-006-02
    data object RefreshLocationRequested : Event()     // US-006-03
}
```

#### Capa de Presentación — ViewModel (VibeFeedViewModel.kt)
Manejar los tres nuevos eventos:
- `LocationChipClicked` → `_state.update { it.copy(showLocationSheet = true) }`
- `LocationBottomSheetDismissed` → `_state.update { it.copy(showLocationSheet = false, locationRefreshError = null) }`
- `RefreshLocationRequested` → Solicitar ubicación fresca, recalcular distancias, cerrar sheet

Para `RefreshLocationRequested` se usa `fusedLocationClient.getCurrentLocation()` (ubicación fresca) en lugar de `lastLocation`:
```kotlin
private fun refreshLocationNow() {
    launch {
        _state.update { it.copy(isRefreshingLocation = true, locationRefreshError = null) }
        runCatching {
            val cts = CancellationTokenSource()
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, cts.token
            ).await()
            requireNotNull(location) { "No se pudo obtener la ubicación" }
            // recalcular distancias (lógica existente de refreshDistances)
            val updatedVenues = _state.value.domainVenues.map { venue ->
                val dist = HaversineHelper.distanceMeters(location.latitude, location.longitude, venue.latitude, venue.longitude)
                venue.toUiModel(dist)
            }
            _state.update { it.copy(venues = updatedVenues, isRefreshingLocation = false, showLocationSheet = false) }
        }.onFailure { error ->
            _state.update { it.copy(isRefreshingLocation = false, locationRefreshError = error.message ?: "Error al obtener ubicación") }
        }
    }
}
```

#### Capa de Presentación — Componentes UI

**`VenueOverlay.kt`** (modificar):
- Añadir parámetro `onLocationChipClicked: () -> Unit` al composable.
- Envolver el `Row` de distancia con `Modifier.clickable { onLocationChipClicked() }` e indicación semántica.

**`LocationUpdateBottomSheet.kt`** (crear nuevo):
- Usar `ModalBottomSheet` de Material 3.
- Estructura interna:
  ```
  Column {
    HandleBar
    Row { Title + CloseButton }
    AutoUpdateCard { GreenPinIcon + "Actualización automática" + "Cada 5 minutos" }
    DescriptionText
    PrimaryButton("Actualizar Ahora" | loading state)
    ErrorText (condicional)
  }
  ```

**`VibeFeedScreen.kt`** (modificar):
- Leer `state.showLocationSheet` y mostrar `LocationUpdateBottomSheet` condicionalmente.
- Pasar `onLocationChipClicked` a `VenueCard` → `VenueOverlay`.

**`VenueCard.kt`** (modificar):
- Añadir parámetro `onLocationChipClicked: () -> Unit` y propagarlo a `VenueOverlay`.

---

### Flujo de Datos (US-006-03)

```
Usuario toca "Actualizar Ahora"
  → Event.RefreshLocationRequested
    → ViewModel: isRefreshingLocation = true
      → fusedLocationClient.getCurrentLocation(HIGH_ACCURACY)
        → onSuccess: recalcular distancias → venues actualizado
          → showLocationSheet = false, isRefreshingLocation = false
        → onError: locationRefreshError = message
          → isRefreshingLocation = false (sheet permanece abierto)
```

---

## Archivos a Crear/Modificar

| Archivo | Capa | Acción | Historia |
|---------|------|--------|----------|
| `LocationUpdateBottomSheet.kt` | feature/screen/feed/component | **Crear** | US-006-02 |
| `VenueOverlay.kt` | feature/screen/feed/component | **Modificar** | US-006-01 |
| `VenueCard.kt` | feature/screen/feed/component | **Modificar** | US-006-01 |
| `VibeFeedScreen.kt` | feature/screen/feed | **Modificar** | US-006-01/02 |
| `State.kt` | feature/screen/feed | **Modificar** | US-006-01/03 |
| `Event.kt` | feature/screen/feed | **Modificar** | US-006-01/02/03 |
| `VibeFeedViewModel.kt` | feature/screen/feed | **Modificar** | US-006-03 |

> No se necesitan cambios en capa de datos ni dominio — toda la lógica es de presentación y usa `FusedLocationProviderClient` ya inyectado.

---

## Consideraciones

- **No crear nueva capa de datos**: `FusedLocationProviderClient` ya está inyectado en el ViewModel.
- **Sin nuevos permisos**: `ACCESS_FINE_LOCATION` ya se solicita al inicio del `VibeFeedScreen`.
- **`getCurrentLocation` vs `lastLocation`**: Para la actualización manual se debe usar `getCurrentLocation` con `Priority.PRIORITY_HIGH_ACCURACY` y un `CancellationTokenSource`, ya que `lastLocation` puede devolver una posición desactualizada.
- **Timeout**: Si el GPS tarda más de 10 segundos, considerar cancelar y mostrar error.
