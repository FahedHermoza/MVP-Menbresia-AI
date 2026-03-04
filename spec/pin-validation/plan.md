# PLAN: Validación Merchant PIN + Pass Active

## Enfoque Técnico

### Capa de Datos (`data`)
1. **`ValidationRemoteDataSource`**: 
   - Lee el campo `pin` del documento `venues/{venueId}` en Firestore.
   - Escribe el registro de validación exitosa en `validations_log`.
2. **`ValidationRepositoryImpl`**:
   - `validatePin(venueId, enteredPin): Result<Boolean>` — compara PIN.
   - `logValidation(venueId, userId, benefit, timestamp): Result<Unit>` — registra.

### Capa de Dominio (`domain`)
1. **`ValidationRepository`** (interfaz).
2. **`ValidatePinUseCase`**: 
   - Recibe `venueId` y `enteredPin`.
   - Verifica el PIN contra la BD.
   - Controla lógica de intentos fallidos (máximo 3 → lanza `TooManyAttemptsException`).
   - En éxito, registra la validación y retorna `ValidationSuccess(benefit, expiresAt)`.
3. **`ValidationResult`**: Sealed class → `Success(benefit, expiresAt)` | `WrongPin(attemptsRemaining)` | `Blocked(unblockAt)`.

### Capa de Presentación (`feature`)
1. **`PinValidationScreen`** (Composable — BottomSheet):
   - Header: escudo dorado + título + subtítulo + badge de beneficio.
   - `PinIndicatorRow`: 4 círculos con animación fill/shake.
   - `PinKeyboard`: Grid 3x4 custom con botones numéricos y backspace.
   - Estado de bloqueo: overlay con countdown.
   - Cancel button inferior.
2. **`PassActiveScreen`** (Composable — Dialog/FullScreen):
   - Animación de check (scale up + fade in con `animateFloatAsState`).
   - Título + beneficio confirmado.
   - `CountdownTimer`: barra de progreso linear que decrece + texto de tiempo restante.
   - Auto-cierre tras expirar el timer (regresa al Feed).
3. **`PinValidationViewModel`** (MVI):
   - `PinUiState`: `Idle | Entering(digits, indicators) | Validating | Success(benefit, timeRemaining) | Error(attemptsLeft) | Blocked(secondsRemaining)`.
   - `PinIntent`: `DigitPressed(digit) | BackspacePressed | CancelPressed`.
   - Timer de 5 minutos en estado Success con updates cada segundo.
   - Timer de 30 segundos en estado Blocked.

---

## Flujo de Datos
```
User llega desde Venue Detail con venueId
  → PinValidationViewModel recibe venueId

User ingresa dígito
  → PinIntent.DigitPressed(digit)
    → ViewModel acumula en lista (max 4)
    → Emite PinUiState.Entering(digits=[1,2], indicators=[filled,filled,empty,empty])

User ingresa 4to dígito
  → ViewModel detecta digits.size == 4
    → Emite PinUiState.Validating (loading breve)
    → ValidatePinUseCase(venueId, "1234")
      → PIN correcto:
        → ValidationResult.Success(benefit="2x1 Gin Tonic", expiresAt=now+5min)
        → Emite PinUiState.Success(...)
        → Inicia countdown timer (300s → 0)
      → PIN incorrecto:
        → ValidationResult.WrongPin(attemptsRemaining=2)
        → Emite PinUiState.Error(2)
        → UI: shake animation + reset indicators
      → Bloqueado:
        → ValidationResult.Blocked(unblockAt=30s)
        → Emite PinUiState.Blocked(30)
        → Inicia countdown 30 → 0, luego reset a Idle
```

---

## Archivos a Crear/Modificar
| Archivo | Capa | Acción |
|---------|------|--------|
| `PinValidationScreen.kt` | feature/screen/validation | Crear |
| `PassActiveScreen.kt` | feature/screen/validation | Crear |
| `PinValidationViewModel.kt` | feature/screen/validation | Crear |
| `PinUiState.kt` | feature/screen/validation | Crear |
| `PinIndicatorRow.kt` | feature/screen/validation/component | Crear |
| `PinKeyboard.kt` | feature/screen/validation/component | Crear |
| `CountdownTimer.kt` | designsystem/component | Crear |
| `ValidationRepository.kt` | domain/repository | Crear |
| `ValidatePinUseCase.kt` | domain/usecase | Crear |
| `ValidationResult.kt` | domain/model | Crear |
| `ValidationRepositoryImpl.kt` | data/repository | Crear |
| `ValidationRemoteDataSource.kt` | data/source/remote | Crear |
