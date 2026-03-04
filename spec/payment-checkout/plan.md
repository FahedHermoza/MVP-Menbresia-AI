# PLAN: Pago con Yape/Plin — Checkout y Comprobante

## Enfoque Técnico

### Capa de Datos (`data`)
1. **`PaymentRemoteDataSource`**: 
   - Sube imagen a Firebase Storage (`comprobantes/{orderId}.jpg`).
   - Crea documento en Firestore `pagos_pendientes`.
   - Actualiza el estado del usuario (`users/{userId}.hasPase = true`) para la aprobación simulada.
2. **`PaymentRepositoryImpl`**:
   - `uploadProof(orderId, imageUri): Result<String>` — retorna la URL de Storage.
   - `createPaymentRecord(payment: PaymentRecord): Result<Unit>`.
   - `activatePass(userId): Result<Unit>` — actualiza flag del usuario.

### Capa de Dominio (`domain`)
1. **`PaymentRepository`** (interfaz).
2. **`SubmitPaymentUseCase`**: Orquesta el flujo completo:
   - Sube imagen → obtiene URL → crea registro en Firestore → activa pase (demo).
3. **`GenerateOrderIdUseCase`**: Genera un código único `ORD-XXXX` (random 4 dígitos).
4. **`PaymentRecord`**: Modelo con `orderId`, `userId`, `plan`, `amount`, `imageUrl`, `status`, `timestamp`.

### Capa de Presentación (`feature`)
1. **`MembershipScreen`** (Composable):
   - Lista de `PlanCard` componentes. Solo Discovery Pass es clickable.
   - Header con título y subtítulo.
2. **`CheckoutScreen`** (Composable):
   - Muestra código de orden, QR, instrucciones, selector de imagen, preview, botón confirmar.
   - States: `Idle → ImageSelected → Uploading → Success → Error`.
3. **`PaymentConfirmationScreen`** (Composable):
   - Pantalla de éxito con check e indicación de que el pago está en revisión.
4. **`CheckoutViewModel`** (MVI):
   - `CheckoutUiState`: `Idle(orderId) | ImageSelected(orderId, imageUri) | Uploading | Success | Error(message)`.
   - `CheckoutIntent`: `SelectImage | ImagePicked(uri) | ConfirmPayment | Retry`.

---

## Flujo de Datos
```
User navega a PASSES tab (o selecciona plan desde algún entry point)
  → MembershipScreen muestra planes
  → User taps "Discovery Pass"
    → Navega a CheckoutScreen

CheckoutScreen.init()
  → GenerateOrderIdUseCase() → "ORD-1045"
  → Emite CheckoutUiState.Idle(orderId="ORD-1045")

User taps "Subir Comprobante"
  → Android Photo Picker / Gallery Intent
  → User selecciona imagen → uri retornado
  → CheckoutIntent.ImagePicked(uri)
  → Emite CheckoutUiState.ImageSelected(orderId, uri)

User taps "Confirmar Pago"
  → CheckoutIntent.ConfirmPayment
  → Emite CheckoutUiState.Uploading
  → SubmitPaymentUseCase(orderId, imageUri, userId, plan, amount)
    → Upload to Storage → URL
    → Create Firestore record
    → Activate pass (demo)
  → Emite CheckoutUiState.Success
    → Navega a PaymentConfirmationScreen
```

---

## Archivos a Crear/Modificar
| Archivo | Capa | Acción |
|---------|------|--------|
| `MembershipScreen.kt` | feature/screen/membership | Crear |
| `PlanCard.kt` | feature/screen/membership/component | Crear |
| `CheckoutScreen.kt` | feature/screen/checkout | Crear |
| `CheckoutViewModel.kt` | feature/screen/checkout | Crear |
| `CheckoutUiState.kt` | feature/screen/checkout | Crear |
| `PaymentConfirmationScreen.kt` | feature/screen/checkout | Crear |
| `PaymentRepository.kt` | domain/repository | Crear |
| `SubmitPaymentUseCase.kt` | domain/usecase | Crear |
| `GenerateOrderIdUseCase.kt` | domain/usecase | Crear |
| `PaymentRecord.kt` | domain/model | Crear |
| `PaymentRepositoryImpl.kt` | data/repository | Crear |
| `PaymentRemoteDataSource.kt` | data/source/remote | Crear |
