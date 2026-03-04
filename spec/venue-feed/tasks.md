# TASKS: Vibe Feed — Lista de Locales

## Tareas de Implementación

### T-002.1: Seed de datos en Firestore
- [ ] Crear colección `venues` en Firebase Console con 3-5 documentos de locales de Cusco (nombres, coordenadas reales, URLs de imágenes de Unsplash, beneficios).
- [ ] Documentar la estructura del documento para referencia del equipo.
- **Resultado visual:** Datos visibles en Firebase Console.

### T-002.2: Crear modelo de dominio `Venue` e interfaz `VenueRepository`
- [ ] Crear `Venue.kt` en `domain/model`.
- [ ] Crear `VenueRepository.kt` en `domain/repository` con `getVenues()` y `getVenueById()`.
- **Resultado visual:** Compila correctamente.

### T-002.3: Implementar capa de datos (DTO, DataSource, Repository)
- [ ] Crear `VenueDto.kt` con campos mapeados a Firestore.
- [ ] Crear `VenueRemoteDataSource.kt` que lee la colección `venues`.
- [ ] Crear `VenueMapper.kt` (VenueDto → Venue).
- [ ] Crear `VenueRepositoryImpl.kt`.
- [ ] Registrar en módulo Hilt.
- **Resultado visual:** Compila y se puede verificar lectura con un log.

### T-002.4: Crear `GetVenuesUseCase`
- [ ] Implementar use case que obtiene la lista y opcionalmente ordena por distancia.
- **Resultado visual:** Compila correctamente.

### T-002.5: Crear `VibeFeedScreen` UI
- [ ] Implementar `VerticalPager` con snap scrolling.
- [ ] Para cada página: `AsyncImage` (Coil) full-screen + `Box` con gradient negro inferior.
- [ ] Overlay inferior: nombre del local (Space Grotesk Bold), distancia (ej. "80m away"), badge "OPEN NOW" verde, beneficio con icono dorado.
- [ ] Columna lateral derecha: iconos heart (con "2.4K"), share, save.
- [ ] Aplicar paleta dark + dorado del design system.
- **Resultado visual:** ✅ Feed renderizado con imágenes a pantalla completa y overlay de datos.

### T-002.6: Crear `BottomNavBar` componente
- [ ] Implementar `BottomNavigation` de Compose con 4 tabs: FEED, EXPLORE, PASSES, PROFILE.
- [ ] Iconos estilizados (Lucide-like). Tab FEED activo con indicador visual.
- [ ] Los tabs no activos navegan a pantallas placeholder ("Coming Soon").
- **Resultado visual:** ✅ Barra de navegación inferior visible y estilizada.

### T-002.7: Crear `VibeFeedViewModel` con cálculo de distancia
- [ ] Implementar ViewModel con MVI.
- [ ] Solicitar permisos de ubicación (ACCESS_FINE_LOCATION).
- [ ] Usar `FusedLocationProviderClient` para obtener última ubicación conocida.
- [ ] Calcular distancia a cada venue y formatear (ej. "80m away", "1.2km away").
- [ ] Emitir Side Effect al hacer tap en un venue: `NavigateToDetail(venueId)`.
- **Resultado visual:** Distancias actualizadas según ubicación real del dispositivo.

### T-002.8: Conectar navegación Feed → Venue Detail
- [ ] Configurar ruta `VenueDetailRoute(venueId: String)` en el NavGraph.
- [ ] Al hacer tap en overlay → navegar pasando el `venueId`.
- **Resultado visual:** ✅ Tap en un local navega a su pantalla de detalle.

---

## Definición de "Done"
- [ ] El feed muestra 3-5 locales con imágenes full-screen y scroll vertical con snap.
- [ ] Cada local muestra nombre, distancia GPS, estado y beneficio.
- [ ] El Bottom Nav se muestra con FEED como tab activo.
- [ ] Al hacer tap en un local se navega al detalle.
- [ ] Los datos vienen de Firestore (no hardcodeados en la UI).
