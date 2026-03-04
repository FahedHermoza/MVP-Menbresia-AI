# PLAN: Vibe Feed — Lista de Locales

## Enfoque Técnico

### Capa de Datos (`data`)
1. **`VenueRemoteDataSource`**: Lee la colección `venues` de Firestore. Retorna `List<VenueDto>`.
2. **`VenueRepositoryImpl`**: Implementa `VenueRepository`. Mapea `VenueDto` → `Venue` (modelo de dominio). Expone:
   - `getVenues(): Flow<List<Venue>>`
   - `getVenueById(id: String): Flow<Venue>`
3. **`VenueDto`**: Data class con anotaciones de Firestore (`@DocumentId`, `@PropertyName`).

### Capa de Dominio (`domain`)
1. **`Venue`**: Modelo limpio con: `id`, `name`, `address`, `latitude`, `longitude`, `imageUrl`, `hours`, `days`, `rating`, `isOpen`, `pin`, `benefits: Map<String, String>`.
2. **`VenueRepository`** (interfaz).
3. **`GetVenuesUseCase`**: Retorna la lista de venues, opcionalmente ordena por distancia si se proporcionan coordenadas del usuario.

### Capa de Presentación (`feature`)
1. **`VibeFeedScreen`** (Composable):
   - `VerticalPager` (Compose Foundation) para scroll tipo TikTok con snap.
   - Cada página: imagen full-screen (Coil `AsyncImage`) + gradient negro inferior + overlay con datos.
   - Acciones laterales: Column de iconos (heart, share, bookmark).
   - Bottom Navigation Bar fija en la parte inferior.
2. **`VibeFeedViewModel`** (MVI):
   - `FeedUiState`: `Loading | Success(venues: List<VenueUiModel>) | Error(message)`.
   - `FeedIntent`: `LoadVenues | VenueClicked(venueId)`.
   - Calcula distancia en tiempo real usando coordenadas del usuario (permiso de ubicación).
3. **`VenueUiModel`**: Modelo de UI con distancia formateada, beneficio del nivel actual del usuario, etc.

### Navegación
- `FeedRoute` recibe navegación desde Auth.
- Al hacer tap en un venue → navega a `VenueDetailRoute(venueId)`.

---

## Flujo de Datos
```
App abre (sesión activa)
  → VibeFeedViewModel.init()
    → GetVenuesUseCase()
      → VenueRepository.getVenues()
        → Firestore "venues" collection → List<VenueDto>
          → Mapeado a List<Venue>
    → Solicita ubicación actual (FusedLocationProvider)
    → Combina venues + location → calcula distancia
    → Emite FeedUiState.Success(List<VenueUiModel>)
      → UI renderiza VerticalPager
```

---

## Archivos a Crear/Modificar
| Archivo | Capa | Acción |
|---------|------|--------|
| `VibeFeedScreen.kt` | feature/screen/feed | Crear |
| `VibeFeedViewModel.kt` | feature/screen/feed | Crear |
| `FeedUiState.kt` | feature/screen/feed | Crear |
| `VenueUiModel.kt` | feature/screen/feed | Crear |
| `VenueRepository.kt` | domain/repository | Crear |
| `GetVenuesUseCase.kt` | domain/usecase | Crear |
| `Venue.kt` | domain/model | Crear |
| `VenueRepositoryImpl.kt` | data/repository | Crear |
| `VenueRemoteDataSource.kt` | data/source/remote | Crear |
| `VenueDto.kt` | data/model | Crear |
| `VenueMapper.kt` | data/mapper | Crear |
| `BottomNavBar.kt` | designsystem/component | Crear |
