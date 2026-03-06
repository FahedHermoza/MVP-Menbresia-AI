# TASKS: Validación Merchant PIN + Pass Active

## Tareas de Implementación

### T-004.1: Crear `ValidationRepository` y capa de datos
- [x] Crear `ValidationRepository.kt` (interfaz) con `validatePin(venueId, pin)` y `logValidation(...)`.
- [x] Implementar `ValidationRemoteDataSource.kt`: leer campo `pin` de `venues/{venueId}`, escribir en `validations_log`.
- [x] Implementar `ValidationRepositoryImpl.kt`.
- [x] Registrar en Hilt.
- **Resultado visual:** Compila y lectura del PIN verificable con log.

### T-004.2: Crear `ValidatePinUseCase` con lógica de intentos
- [x] Implementar comparación de PIN.
- [x] Mantener contador de intentos en memoria (no persistido). Resetea tras éxito o tras expirar bloqueo.
- [x] Retornar `ValidationResult` sealed class: `Success`, `WrongPin(attemptsRemaining)`, `Blocked(unblockAt)`.
- **Resultado visual:** Test unitario pasa con los 3 escenarios.

### T-004.3: Crear `PinKeyboard` componente
- [x] Grid de 3 columnas x 4 filas con botones estilizados.
- [x] Botones 1-9: fondo `#2A2A2A`, corner 12px, número blanco Space Grotesk 24px.
- [x] Fila inferior: espacio vacío | 0 | backspace (icono ⌫).
- [x] Cada botón emite su valor vía callback `onDigitPressed(Int)` / `onBackspacePressed()`.
- **Resultado visual:** ✅ Teclado numérico renderizado con estilo premium oscuro.

### T-004.4: Crear `PinIndicatorRow` componente
- [x] Row de 4 círculos (12x12dp).
- [x] Estado vacío: `#444444`. Estado lleno: `#D4AF37` (dorado).
- [x] Animación de shake (offset horizontal con spring) para error.
- [x] Animación scale-up sutil al llenar cada dígito.
- **Resultado visual:** ✅ Indicadores se llenan progresivamente y hacen shake en error.

### T-004.5: Crear `PinValidationScreen` (BottomSheet completo)
- [x] ModalBottomSheet con handle gris superior.
- [x] Contenido:
    - Icono escudo dorado (ShieldCheck).
    - Título "ENTER MERCHANT PIN" — Bold, blanco.
    - Subtítulo "Show this screen to the venue staff" — Regular, gris.
    - Badge de beneficio con borde dorado e icono star.
    - `PinIndicatorRow`.
    - `PinKeyboard`.
    - Texto "Cancel" (gris, clickable).
- [x] Estado de bloqueo: overlay semitransparente con mensaje "Demasiados intentos" + countdown "Espera 28s...".
- **Resultado visual:** ✅ BottomSheet completo renderizado acorde al mockup.

### T-004.6: Crear `PassActiveScreen`
- [x] Fondo negro con efecto blur del contenido de atrás (si posible, sino negro sólido).
- [x] Círculo verde `#4CAF50` con borde + icono check animado (scale 0 → 1 con spring, fade-in).
- [x] Título "PASS ACTIVE!" — Space Grotesk Bold 32px, blanco.
- [x] "Benefit Confirmed:" — Regular 14px, gris.
- [x] Nombre del beneficio (ej. "2x1 Gin Tonic") — Bold 20px, verde.
- [x] Barra inferior: "VALID FOR" label + `LinearProgressIndicator` verde que decrece + "4:52 remaining" texto verde.
- [x] Timer de 5 minutos: actualiza cada segundo, al llegar a 0 navega de vuelta.
- **Resultado visual:** ✅ Pantalla de éxito con animación de check y countdown activo.

### T-004.7: Crear `PinValidationViewModel`
- [x] Gestionar acumulación de dígitos (max 4).
- [x] Auto-validar al ingresar el 4to dígito.
- [x] Manejar estados: Entering → Validating → Success/Error/Blocked.
- [x] Timer de 5min (Success) con emissions cada segundo (para actualizar barra + texto).
- [x] Timer de 30s (Blocked) con countdown.
- [x] Side effects: `NavigateBack`, `NavigateToFeed`.
- **Resultado visual:** ✅ Flujo completo funcional: ingresar PIN → éxito o error → bloqueo tras 3 fallos.

### T-004.8: Registrar validación en Firestore
- [x] Al lograr `Success`, escribir documento en `validations_log` con: `userId`, `venueId`, `venueName`, `benefit`, `timestamp`, `status: "success"`.
- **Resultado visual:** Documento visible en Firebase Console tras una validación exitosa.

---

## Definición de "Done"
- [x] El teclado numérico se muestra correctamente con el diseño del mockup.
- [x] Los indicadores se llenan y hacen shake en error.
- [x] PIN correcto → pantalla "PASS ACTIVE!" con animación y timer.
- [x] 3 intentos fallidos → bloqueo visible con countdown de 30s.
- [x] La validación queda registrada en Firestore.
- [x] El timer de éxito llega a 0 y regresa al Feed.
