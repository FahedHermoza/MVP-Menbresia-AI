# PLAN: Venue Detail — Detalle del Local con Geofencing

## Enfoque Técnico

### Capa de Datos (`data`)
- Reutiliza `VenueRepositoryImpl` ya creada en `venue-feed`. Se usa `getVenueById(id)`.

### Capa de Dominio (`domain`)
1. **`GetVenueByIdUseCase`**: Obtiene un venue específico por ID.
2. **`CheckProximityUseCase`**: Recibe coordenadas del usuario y del venue. Calcula distancia con la fórmula Haversine (o `Location.distanceTo()`). Retorna `ProximityResult` con `distanceMeters` y `isInRange` (< 50m).
3. **`LocationRepository`** (interfaz): Expone `getCurrentLocation(): Flow<LatLng>`.

### Capa de Datos — Location
1. **`LocationRepositoryImpl`**: Usa `FusedLocationProviderClient` para monitorear ubicación en tiempo real con `locationFlow` (updates cada 5 segundos).
2. **`LocationDataSource`**: Wrapper sobre FusedLocationProvider.

### Capa de Presentación (`feature`)
1. **`VenueDetailScreen`** (Composable):
   - Layout dividido: Top Image (40%) + Bottom Scrollable Content (60%).
   - Componentes: `VenueImageHeader`, `VenueInfoRow`, `BenefitCard`, `ActivateButton`.
   - `ActivateButton` observa `isInRange` del estado para alternar entre disabled/active.
2. **`VenueDetailViewModel`** (MVI):
   - `DetailUiState`: `Loading | Success(venue, distanceText, isInRange, userTier) | Error`.
   - `DetailIntent`: `LoadVenue(venueId) | ActivateClicked | CloseClicked`.
   - Combina `Flow<Venue>` con `Flow<Location>` para recalcular distancia en tiempo real.

---

## Flujo de Datos
```
User taps venue en Feed → navega con venueId
  → VenueDetailViewModel.init(venueId)
    → Parallel:
       1. GetVenueByIdUseCase(venueId) → Venue
       2. LocationRepository.getCurrentLocation() → Flow<LatLng> (updates cada 5s)
    → Combine: CheckProximityUseCase(userLatLng, venueLatLng)
      → ProximityResult(distance=35m, isInRange=true)
    → Emite DetailUiState.Success(venue, "35m away", isInRange=true, tier="gold")
      → UI actualiza: botón cambia a dorado

User taps "ACTIVATE PASS NOW"
  → SideEffect.NavigateToPinValidation(venueId)
```

---

## Archivos a Crear/Modificar
| Archivo | Capa | Acción |
|---------|------|--------|
| `VenueDetailScreen.kt` | feature/screen/detail | Crear |
| `VenueDetailViewModel.kt` | feature/screen/detail | Crear |
| `DetailUiState.kt` | feature/screen/detail | Crear |
| `VenueImageHeader.kt` | feature/screen/detail/component | Crear |
| `BenefitCard.kt` | feature/screen/detail/component | Crear |
| `ActivateButton.kt` | designsystem/component | Crear |
| `GetVenueByIdUseCase.kt` | domain/usecase | Crear |
| `CheckProximityUseCase.kt` | domain/usecase | Crear |
| `LocationRepository.kt` | domain/repository | Crear |
| `LocationRepositoryImpl.kt` | data/repository | Crear |
| `LocationDataSource.kt` | data/source | Crear |
