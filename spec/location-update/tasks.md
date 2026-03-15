# TASKS: Actualización Manual de Ubicación en el Feed

> Las tareas T-006.1, T-006.2 y T-006.3 son independientes entre sí y pueden desarrollarse en paralelo.
> T-006.4 (wiring final) requiere que las tres anteriores estén completas.

---

## T-006.1: Location Chip Tappable en VenueOverlay
**Historia:** US-006-01
**Archivos:** `VenueOverlay.kt`, `VenueCard.kt`

- [ ] Añadir parámetro `onLocationChipClicked: () -> Unit` a `VenueOverlay`.
- [ ] Aplicar `Modifier.clickable` con `indication` (ripple) al `Row` de distancia.
- [ ] Añadir semántica accesible: `Role.Button` + `contentDescription = "Actualizar ubicación"`.
- [ ] Propagar `onLocationChipClicked` desde `VenueCard` hacia `VenueOverlay`.
- [ ] Añadir parámetro `onLocationChipClicked` a `VenueCard` y propagarlo.

**Resultado visual:** Al tocar el indicador de distancia (`📍 80m away`), se aprecia efecto ripple. El chip luce interactivo.

---

## T-006.2: Componente LocationUpdateBottomSheet
**Historia:** US-006-02
**Archivos:** `LocationUpdateBottomSheet.kt` (nuevo)

- [ ] Crear `LocationUpdateBottomSheet.kt` en `feature/screen/feed/component/`.
- [ ] Usar `ModalBottomSheet` de Material 3 con `containerColor = Color(0xFF1A1A1A)`.
- [ ] Implementar handle bar superior (pill oscura centrada).
- [ ] Implementar header: título "Actualización de Ubicación" (Bold 18sp, blanco) + `IconButton` con icono X alineado a la derecha.
- [ ] Implementar tarjeta de estado:
  - Fondo `Color(0xFF2A2A2A)`, `RoundedCornerShape(12.dp)`.
  - Icono `Icons.Outlined.LocationOn` en `MenbresiaColors.Success`.
  - Texto principal "Actualización automática" (SemiBold 14sp, blanco).
  - Subtexto "Cada 5 minutos" (Regular 12sp, `TextSecondary`).
- [ ] Implementar párrafo descriptivo (Regular 13sp, `TextSecondary`).
- [ ] Implementar botón primario:
  - Estado normal: fondo `MenbresiaColors.Primary`, icono refresh + texto "Actualizar Ahora".
  - Estado loading: `CircularProgressIndicator` pequeño + texto "Actualizando...".
- [ ] Implementar texto de error condicional (Regular 12sp, `MenbresiaColors.Error`) bajo el botón.
- [ ] El parámetro de firma del composable:
  ```kotlin
  @Composable
  fun LocationUpdateBottomSheet(
      isRefreshing: Boolean,
      errorMessage: String?,
      onDismiss: () -> Unit,
      onRefreshRequested: () -> Unit
  )
  ```
- [ ] Agregar `@Preview` con estado normal y estado loading.

**Resultado visual:** El bottom sheet renderiza correctamente con todos sus elementos según el mockup `Mobile - Vibe Feed - Location Bottom Sheet.png`. Preview funcional en Android Studio.

---

## T-006.3: Lógica de Actualización Manual en ViewModel
**Historia:** US-006-03
**Archivos:** `State.kt`, `Event.kt`, `VibeFeedViewModel.kt`

- [ ] Añadir a `State.kt`:
  ```kotlin
  val showLocationSheet: Boolean = false,
  val isRefreshingLocation: Boolean = false,
  val locationRefreshError: String? = null
  ```
- [ ] Añadir a `Event.kt`:
  ```kotlin
  data object LocationChipClicked : Event()
  data object LocationBottomSheetDismissed : Event()
  data object RefreshLocationRequested : Event()
  ```
- [ ] Manejar `LocationChipClicked` en `onEvent`: `_state.update { it.copy(showLocationSheet = true) }`.
- [ ] Manejar `LocationBottomSheetDismissed` en `onEvent`: resetear `showLocationSheet = false`, `locationRefreshError = null`.
- [ ] Implementar función `refreshLocationNow()`:
  - Usar `fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token).await()`.
  - En éxito: recalcular distancias con `HaversineHelper`, actualizar `venues`, cerrar sheet.
  - En error: setear `locationRefreshError`.
- [ ] Manejar `RefreshLocationRequested` en `onEvent` → llamar `refreshLocationNow()`.

**Resultado visual:** Al tocar "Actualizar Ahora" el botón muestra spinner. Tras la actualización, el sheet se cierra y los textos de distancia en el feed muestran valores frescos. Si falla, aparece texto de error en rojo dentro del sheet.

---

## T-006.4: Wiring en VibeFeedScreen
**Historia:** US-006-01 + US-006-02 + US-006-03 (integración final)
**Archivos:** `VibeFeedScreen.kt`
**Bloquea:** Requiere T-006.1, T-006.2 y T-006.3 completadas.

- [ ] Leer `state.showLocationSheet`, `state.isRefreshingLocation`, `state.locationRefreshError` del ViewModel.
- [ ] Mostrar `LocationUpdateBottomSheet` condicionalmente cuando `showLocationSheet == true`.
- [ ] Pasar los callbacks:
  - `onDismiss` → `viewModel.onEvent(Event.LocationBottomSheetDismissed)`
  - `onRefreshRequested` → `viewModel.onEvent(Event.RefreshLocationRequested)`
- [ ] Propagar `onLocationChipClicked` desde `VibeFeedScreen` → `VibeFeedContent` → `VenueCard`:
  - `{ viewModel.onEvent(Event.LocationChipClicked) }`

**Resultado visual:** Flujo completo funcional — tap en distancia → bottom sheet → tap "Actualizar Ahora" → spinner → sheet se cierra → distancias actualizadas.

---

## Definición de "Done"

- [ ] Tocar el chip de distancia en cualquier tarjeta del feed abre el bottom sheet.
- [ ] El bottom sheet muestra todos los elementos del diseño: handle, título, X, tarjeta de estado, descripción y botón.
- [ ] Al cerrar con X o swipe down, el sheet desaparece correctamente.
- [ ] Al tocar "Actualizar Ahora", el botón muestra estado de carga.
- [ ] Tras la actualización, el sheet se cierra y las distancias del feed se actualizan.
- [ ] Si la ubicación falla, el sheet muestra el error y permite reintentar.
- [ ] No hay regresión en la funcionalidad existente del feed (scroll, navegación a detalle).
