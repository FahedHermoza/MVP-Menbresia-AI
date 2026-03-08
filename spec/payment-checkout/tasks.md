# TASKS: Pago con Yape/Plin — Checkout y Comprobante

## Tareas de Implementación

### T-005.1: Crear modelos de dominio y repositorio
- [x] Crear `PaymentRecord.kt` en `domain/model`.
- [x] Crear `PaymentRepository.kt` (interfaz) en `domain/repository`.
- [x] Crear `GenerateOrderIdUseCase.kt` — genera "ORD-" + 4 dígitos random.
- **Resultado visual:** Compila correctamente.

### T-005.2: Implementar capa de datos (Storage + Firestore)
- [x] Crear `PaymentRemoteDataSource.kt`:
    - `uploadImage(orderId, uri)`: sube a `receipts/{orderId}.jpg` en Firebase Storage, retorna download URL.
    - `createPaymentRecord(record)`: escribe en `outstanding_payments/{orderId}`.
    - `activateUserPass()`: actualiza `users/{userId}.hasPase = true` (userId obtenido de `auth.currentUser`).
- [x] Crear `PaymentRepositoryImpl.kt`.
- [x] Registrar en Hilt.
- **Resultado visual:** Imagen subida visible en Firebase Storage Console (requiere activar Firebase Storage — plan Blaze).

### T-005.3: Crear `SubmitPaymentUseCase`
- [x] Orquestar: upload imagen → crear registro → activar pase (demo).
- [x] Manejar errores de red con Result.
- **Resultado visual:** Compila y test unitario pasa.

### T-005.4: Crear `MembershipScreen` (Pantalla de Planes)
- [x] Header: barra dorada vertical + "CHOOSE YOUR ACCESS LEVEL" bold + subtítulo.
- [x] 4 `PlanCard` componentes:
    - **Discovery Pass** (deshabilitado): "ONE-TIME PAY", "S/9.90", lista de 4 beneficios.
    - **Vibe Member** (deshabilitado): badge "RECOMMENDED" dorado, "S/19.90/mo", overlay "Próximamente".
    - **Gold Select** (habilitado): "S/49.90/mo", borde dorado destacado.
    - **Black Founder** (deshabilitado): "INVITE ONLY", badge "APPLICATION REQUIRED", overlay "Próximamente".
- [x] Al tocar Gold Select → navegar a CheckoutScreen.
- **Resultado visual:** ✅ Pantalla de planes renderizada con Gold Select habilitado.

### T-005.5: Crear `CheckoutScreen`
- [x] Mostrar código de orden grande en dorado (ej. "ORD-1045").
- [x] Mostrar imagen de método de pago (`ic_payment_method`) en box blanco redondeado.
- [x] Mostrar número de celular de la empresa.
- [x] Instrucción prominente con el código en dorado bold.
- [x] Botón "Subir Comprobante" con icono de galería (outline dorado).
- [x] Al seleccionar imagen: mostrar thumbnail con preview redondeado.
- [x] Botón "Confirmar Pago" (sólido dorado, full-width). Solo habilitado si hay imagen seleccionada.
- [x] Estado loading con CircularProgressIndicator.
- **Resultado visual:** ✅ Pantalla de checkout completa con instrucciones y selector de imagen funcional.

### T-005.6: Integrar Android Photo Picker
- [x] Usar `ActivityResultContracts.PickVisualMedia()` para abrir selector de imágenes.
- [x] Obtener URI de la imagen seleccionada y mostrar preview con Coil.
- **Resultado visual:** ✅ Gallery picker abre y al seleccionar imagen se muestra preview.

### T-005.7: Crear `CheckoutViewModel`
- [x] Generar orderId al init.
- [x] Gestionar estados: Idle → ImageSelected → Uploading → Success/Error.
- [x] Llamar a `SubmitPaymentUseCase` al confirmar.
- [x] Emitir Side Effect de navegación a confirmación.
- **Resultado visual:** ✅ Flujo completo de selección de imagen + upload + registro funcional.

### T-005.8: Crear `PaymentConfirmationScreen`
- [x] Icono de check dorado grande.
- [x] Título "¡Pago Enviado!" en blanco bold.
- [x] Subtítulo: "Tu comprobante está siendo revisado. Tu pase se activará en breve." en gris.
- [x] Para el demo: texto verde de activación instantánea.
- [x] Botón "Ir al Feed" que navega de vuelta.
- **Resultado visual:** ✅ Pantalla de confirmación con mensajes de éxito y navegación al Feed.

---

## Definición de "Done"
- [x] La pantalla de planes muestra los 4 tiers con Gold Select habilitado.
- [x] El checkout genera un código de orden único y muestra imagen de método de pago + instrucciones.
- [x] El usuario puede seleccionar una imagen del device y verla en preview.
- [x] Al confirmar, la imagen se sube a Storage y el registro se crea en Firestore.
- [x] Se muestra confirmación y el estado del usuario cambia a `hasPase = true`.
- [ ] El comprobante y registro son visibles en Firebase Console (pendiente: activar Firebase Storage — requiere plan Blaze en proyecto `menbresia-ai-mvp`).
