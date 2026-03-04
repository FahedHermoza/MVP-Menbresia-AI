# TASKS: Validación Merchant PIN + Pass Active

## Tareas de Implementación

### T-004.1: Crear `ValidationRepository` y capa de datos
- [ ] Crear `ValidationRepository.kt` (interfaz) con `validatePin(venueId, pin)` y `logValidation(...)`.
- [ ] Implementar `ValidationRemoteDataSource.kt`: leer campo `pin` de `venues/{venueId}`, escribir en `validations_log`.
- [ ] Implementar `ValidationRepositoryImpl.kt`.
- [ ] Registrar en Hilt.
- **Resultado visual:** Compila y lectura del PIN verificable con log.

### T-004.2: Crear `ValidatePinUseCase` con lógica de intentos
- [ ] Implementar comparación de PIN.
- [ ] Mantener contador de intentos en memoria (no persistido). Resetea tras éxito o tras expirar bloqueo.
- [ ] Retornar `ValidationResult` sealed class: `Success`, `WrongPin(attemptsRemaining)`, `Blocked(unblockAt)`.
- **Resultado visual:** Test unitario pasa con los 3 escenarios.

### T-004.3: Crear `PinKeyboard` componente
- [ ] Grid de 3 columnas x 4 filas con botones estilizados.
- [ ] Botones 1-9: fondo `#2A2A2A`, corner 12px, número blanco Space Grotesk 24px.
- [ ] Fila inferior: espacio vacío | 0 | backspace (icono ⌫).
- [ ] Cada botón emite su valor vía callback `onDigitPressed(Int)` / `onBackspacePressed()`.
- **Resultado visual:** ✅ Teclado numérico renderizado con estilo premium oscuro.

### T-004.4: Crear `PinIndicatorRow` componente
- [ ] Row de 4 círculos (12x12dp).
- [ ] Estado vacío: `#444444`. Estado lleno: `#D4AF37` (dorado).
- [ ] Animación de shake (offset horizontal con spring) para error.
- [ ] Animación scale-up sutil al llenar cada dígito.
- **Resultado visual:** ✅ Indicadores se llenan progresivamente y hacen shake en error.

### T-004.5: Crear `PinValidationScreen` (BottomSheet completo)
- [ ] ModalBottomSheet con handle gris superior.
- [ ] Contenido:
    - Icono escudo dorado (ShieldCheck).
    - Título "ENTER MERCHANT PIN" — Bold, blanco.
    - Subtítulo "Show this screen to the venue staff" — Regular, gris.
    - Badge de beneficio con borde dorado e icono star.
    - `PinIndicatorRow`.
    - `PinKeyboard`.
    - Texto "Cancel" (gris, clickable).
- [ ] Estado de bloqueo: overlay semitransparente con mensaje "Demasiados intentos" + countdown "Espera 28s...".
- **Resultado visual:** ✅ BottomSheet completo renderizado acorde al mockup.

### T-004.6: Crear `PassActiveScreen`
- [ ] Fondo negro con efecto blur del contenido de atrás (si posible, sino negro sólido).
- [ ] Círculo verde `#4CAF50` con borde + icono check animado (scale 0 → 1 con spring, fade-in).
- [ ] Título "PASS ACTIVE!" — Space Grotesk Bold 32px, blanco.
- [ ] "Benefit Confirmed:" — Regular 14px, gris.
- [ ] Nombre del beneficio (ej. "2x1 Gin Tonic") — Bold 20px, verde.
- [ ] Barra inferior: "VALID FOR" label + `LinearProgressIndicator` verde que decrece + "4:52 remaining" texto verde.
- [ ] Timer de 5 minutos: actualiza cada segundo, al llegar a 0 navega de vuelta.
- **Resultado visual:** ✅ Pantalla de éxito con animación de check y countdown activo.

### T-004.7: Crear `PinValidationViewModel`
- [ ] Gestionar acumulación de dígitos (max 4).
- [ ] Auto-validar al ingresar el 4to dígito.
- [ ] Manejar estados: Entering → Validating → Success/Error/Blocked.
- [ ] Timer de 5min (Success) con emissions cada segundo (para actualizar barra + texto).
- [ ] Timer de 30s (Blocked) con countdown.
- [ ] Side effects: `NavigateBack`, `NavigateToFeed`.
- **Resultado visual:** ✅ Flujo completo funcional: ingresar PIN → éxito o error → bloqueo tras 3 fallos.

### T-004.8: Registrar validación en Firestore
- [ ] Al lograr `Success`, escribir documento en `validations_log` con: `userId`, `venueId`, `venueName`, `benefit`, `timestamp`, `status: "success"`.
- **Resultado visual:** Documento visible en Firebase Console tras una validación exitosa.

---

## Definición de "Done"
- [ ] El teclado numérico se muestra correctamente con el diseño del mockup.
- [ ] Los indicadores se llenan y hacen shake en error.
- [ ] PIN correcto → pantalla "PASS ACTIVE!" con animación y timer.
- [ ] 3 intentos fallidos → bloqueo visible con countdown de 30s.
- [ ] La validación queda registrada en Firestore.
- [ ] El timer de éxito llega a 0 y regresa al Feed.
