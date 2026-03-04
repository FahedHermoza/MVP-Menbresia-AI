# TASKS: Pago con Yape/Plin — Checkout y Comprobante

## Tareas de Implementación

### T-005.1: Crear modelos de dominio y repositorio
- [ ] Crear `PaymentRecord.kt` en `domain/model`.
- [ ] Crear `PaymentRepository.kt` (interfaz) en `domain/repository`.
- [ ] Crear `GenerateOrderIdUseCase.kt` — genera "ORD-" + 4 dígitos random.
- **Resultado visual:** Compila correctamente.

### T-005.2: Implementar capa de datos (Storage + Firestore)
- [ ] Crear `PaymentRemoteDataSource.kt`:
    - `uploadImage(orderId, uri)`: sube a `comprobantes/{orderId}.jpg` en Firebase Storage, retorna download URL.
    - `createPaymentRecord(record)`: escribe en `pagos_pendientes/{orderId}`.
    - `activateUserPass(userId)`: actualiza `users/{userId}.hasPase = true`.
- [ ] Crear `PaymentRepositoryImpl.kt`.
- [ ] Registrar en Hilt.
- **Resultado visual:** Imagen subida visible en Firebase Storage Console.

### T-005.3: Crear `SubmitPaymentUseCase`
- [ ] Orquestar: upload imagen → crear registro → activar pase (demo).
- [ ] Manejar errores de red con Result.
- **Resultado visual:** Compila y test unitario pasa.

### T-005.4: Crear `MembershipScreen` (Pantalla de Planes)
- [ ] Header: barra dorada vertical + "CHOOSE YOUR ACCESS LEVEL" bold + subtítulo.
- [ ] 4 `PlanCard` componentes:
    - **Discovery Pass** (habilitado): "ONE-TIME PAY", "S/9.90", lista de 4 beneficios con iconos, fondo `#1C1C1C`.
    - **Vibe Member** (deshabilitado): badge "RECOMMENDED" dorado, "S/19.90/mo", badge "1 MONTH FREE TRIAL", overlay "Próximamente".
    - **Gold Select** (deshabilitado): "S/49.90/mo", overlay "Próximamente".
    - **Black Founder** (deshabilitado): "INVITE ONLY", badge "APPLICATION REQUIRED", overlay "Próximamente".
- [ ] Al tocar Discovery Pass → navegar a CheckoutScreen.
- **Resultado visual:** ✅ Pantalla de planes renderizada acorde al mockup con solo Discovery habilitado.

### T-005.5: Crear `CheckoutScreen`
- [ ] Mostrar código de orden grande en dorado (ej. "ORD-1045").
- [ ] Mostrar imagen QR de Yape (hardcodeada en assets o cargada de Storage).
- [ ] Mostrar número de celular de la empresa.
- [ ] Instrucción prominente: **"Realiza el pago en Yape/Plin y coloca el código ORD-1045 en el mensaje de la transferencia"** con el código en dorado bold.
- [ ] Botón "Subir Comprobante" con icono de galería (outline dorado).
- [ ] Al seleccionar imagen: mostrar thumbnail con preview redondeado.
- [ ] Botón "Confirmar Pago" (sólido dorado, full-width). Solo habilitado si hay imagen seleccionada.
- [ ] Estado loading con CircularProgressIndicator.
- **Resultado visual:** ✅ Pantalla de checkout completa con QR, instrucciones y selector de imagen funcional.

### T-005.6: Integrar Android Photo Picker
- [ ] Usar `ActivityResultContracts.PickVisualMedia()` para abrir selector de imágenes.
- [ ] Obtener URI de la imagen seleccionada y mostrar preview con Coil.
- **Resultado visual:** ✅ Gallery picker abre y al seleccionar imagen se muestra preview.

### T-005.7: Crear `CheckoutViewModel`
- [ ] Generar orderId al init.
- [ ] Gestionar estados: Idle → ImageSelected → Uploading → Success/Error.
- [ ] Llamar a `SubmitPaymentUseCase` al confirmar.
- [ ] Emitir Side Effect de navegación a confirmación.
- **Resultado visual:** ✅ Flujo completo de selección de imagen + upload + registro funcional.

### T-005.8: Crear `PaymentConfirmationScreen`
- [ ] Icono de check dorado grande animado.
- [ ] Título "¡Pago Enviado!" en blanco bold.
- [ ] Subtítulo: "Tu comprobante está siendo revisado. Tu pase se activará en breve." en gris.
- [ ] Para el demo: texto verde "✓ Tu Discovery Pass ha sido activado" (aprobación instantánea).
- [ ] Botón "Ir al Feed" que navega de vuelta.
- **Resultado visual:** ✅ Pantalla de confirmación con mensajes de éxito y navegación al Feed.

---

## Definición de "Done"
- [ ] La pantalla de planes muestra los 4 tiers con Discovery Pass habilitado.
- [ ] El checkout genera un código de orden único y muestra QR + instrucciones.
- [ ] El usuario puede seleccionar una imagen del device y verla en preview.
- [ ] Al confirmar, la imagen se sube a Storage y el registro se crea en Firestore.
- [ ] Se muestra confirmación y el estado del usuario cambia a `hasPase = true`.
- [ ] El comprobante y registro son visibles en Firebase Console.
