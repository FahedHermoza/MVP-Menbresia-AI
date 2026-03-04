# TASKS: Venue Detail — Detalle del Local con Geofencing

## Tareas de Implementación

### T-003.1: Crear `GetVenueByIdUseCase`
- [ ] Implementar use case que llama a `VenueRepository.getVenueById(id)`.
- **Resultado visual:** Compila correctamente.

### T-003.2: Implementar `LocationRepository` y `LocationDataSource`
- [ ] Crear `LocationRepository.kt` (interfaz) en Domain con `getCurrentLocation(): Flow<LatLng>`.
- [ ] Implementar `LocationDataSource.kt` usando `FusedLocationProviderClient` con `LocationRequest` (intervalo 5s, prioridad HIGH_ACCURACY).
- [ ] Implementar `LocationRepositoryImpl.kt` que expone el flujo de ubicaciones.
- [ ] Registrar en Hilt.
- **Resultado visual:** Log mostrando coordenadas actualizadas al moverse.

### T-003.3: Crear `CheckProximityUseCase`
- [ ] Implementar cálculo de distancia entre dos puntos (Haversine o `Location.distanceTo()`).
- [ ] Retornar `ProximityResult(distanceMeters: Float, isInRange: Boolean)` donde `isInRange = distance < 50`.
- **Resultado visual:** Compila y test unitario pasa.

### T-003.4: Crear `VenueDetailScreen` — Layout superior (imagen + tags)
- [ ] Imagen del venue con `AsyncImage` ocupando ~40% de la pantalla.
- [ ] Botón close (círculo semitransparente `#00000088`, radio 18, icono X blanco) en la esquina superior izquierda.
- [ ] Row de tags sobre la imagen: badge "LIVE" (fondo `#F44336`, texto white bold 10px) + nombre del local (white bold 13px).
- **Resultado visual:** ✅ Sección superior del detalle renderizada acorde al mockup.

### T-003.5: Crear `VenueDetailScreen` — Layout inferior (info + beneficios)
- [ ] Info row: dirección + horario (textos gris `#A0A0A0`, 12px) a la izquierda. Rating (dorado `#D4AF37`, 18px bold) a la derecha.
- [ ] Divider (`#2A2A2A`, 1px).
- [ ] Título "MEMBER BENEFITS" con barra dorada vertical (3x14px) e icono.
- [ ] 3x `BenefitCard` (fondo `#1C1C1C`, corner 4px):
    - Vibe Member: icono zap gris, texto tier gris, beneficio blanco.
    - Gold Select: icono star dorado, texto tier dorado, **borde dorado** (1px `#D4AF37`).
    - Black Founder: icono crown blanco, texto tier gris, beneficio blanco.
- **Resultado visual:** ✅ Sección de beneficios renderizada con el tier Gold resaltado.

### T-003.6: Crear `ActivateButton` (Design System)
- [ ] Componente reutilizable con dos estados:
    - **Disabled:** fondo `#282828`, borde `#3A3A3A`, texto `#666666` "Get Closer to Activate (Geofence Active)".
    - **Active:** gradiente lineal (#F5D060 → #D4AF37 → #A07820), shadow glow `#D4AF3766` blur 20, icono zap negro, texto "ACTIVATE PASS NOW" negro bold.
- [ ] Transición animada entre estados (alpha, scale).
- **Resultado visual:** ✅ Botón renderizado en ambos estados.

### T-003.7: Crear `VenueDetailViewModel`
- [ ] Combinar flujo de venue + flujo de location.
- [ ] Recalcular distancia en tiempo real al recibir actualizaciones de GPS.
- [ ] Emitir `DetailUiState.Success` con `isInRange` actualizado.
- [ ] Emitir `SideEffect.NavigateToPinValidation(venueId)` al presionar botón activo.
- **Resultado visual:** ✅ Botón cambia de gris a dorado al acercarse al local (simulable con Mock Location).

### T-003.8: Integrar texto de geofence
- [ ] Mostrar texto "GEOFENCE ACTIVE — YOU MUST BE NEARBY TO ACTIVATE" con icono radar y color `#666666` centrado sobre el botón.
- [ ] Cuando está en rango, cambiar a "✓ YOU ARE NEARBY — READY TO ACTIVATE" en dorado.
- **Resultado visual:** ✅ Texto de estado de geofence visible y actualizado.

---

## Definición de "Done"
- [ ] La pantalla muestra los datos del local seleccionado desde el Feed.
- [ ] Los 3 beneficios por nivel se muestran correctamente con Gold resaltado.
- [ ] El botón está gris cuando está lejos y se vuelve dorado activo al estar < 50m.
- [ ] Al presionar el botón activo se navega al flujo de PIN.
- [ ] El botón (X) regresa al Feed.
