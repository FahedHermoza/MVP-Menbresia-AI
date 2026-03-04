# SPEC-005: Pago con Yape/Plin — Checkout y Comprobante

## Historia de Usuario
**Como** usuario que quiere adquirir su pase de Menbresia AI,  
**quiero** pagar mediante Yape o Plin y subir el comprobante de la transferencia,  
**para** que el equipo valide mi pago y active mi membresía rápidamente.

---

## Contexto
Para el MVP, se evita la complejidad de Google Play Billing y se implementa el flujo manual de pagos con billeteras electrónicas peruanas (Yape/Plin), tal como se definió en el PRD-MVP (sección 2.4). El modelo centraliza la revisión del comprobante en una bandeja de email, y para propósitos del demo, la aprobación se simula instantáneamente.

El mockup `Mobile - PIN Validation-4.png` muestra la pantalla de selección de plan (Discovery Pass S/9.90, Vibe Member S/19.90/mo, Gold Select S/49.90/mo, Black Founder por invitación).

## Alcance Funcional

### Incluido
- **Pantalla de Selección de Plan:** Lista de planes (basada en el mockup) con precios. Para el MVP, solo el **Discovery Pass** (S/9.90, pago único) estará habilitado. Los demás se muestran deshabilitados con indicación de "Próximamente".
- **Pantalla de Checkout:**
  - Código de orden generado automáticamente (ej. `ORD-1045`).
  - QR estático de Yape y número de celular de la empresa.
  - Instrucción prominente en negrita: **"Realiza el pago en Yape/Plin y coloca el código ORD-1045 en el mensaje de la transferencia"**.
  - Botón para abrir selector de imágenes (gallery picker) para subir el comprobante.
  - Vista previa de la imagen seleccionada.
  - Botón "Confirmar Pago" que sube el comprobante a Firebase Storage y crea registro en Firestore `pagos_pendientes`.
- **Confirmación:** Pantalla de "Pago Enviado" con indicación de que será revisado. Para el demo, se activa el pase instantáneamente.
  
### Excluido
- Integración real con Google Play Billing.
- Envío real de email (se simula con el registro en Firestore).
- Suscripciones mensuales funcionales (solo Discovery Pass).
- Validación automática del comprobante (se hace manualmente en Firebase Console).

---

## Criterios de Aceptación

| # | Criterio | Validación Visual |
|---|----------|-------------------|
| AC-1 | Se muestra la pantalla de selección de plan con Discovery Pass habilitado (S/9.90) y los demás deshabilitados. | Pantalla de planes renderizada. |
| AC-2 | Al seleccionar Discovery Pass, se navega al Checkout con un código de orden único (ej. ORD-1045). | Pantalla de checkout con código visible. |
| AC-3 | Se muestra el QR de Yape/Plin con la instrucción clara y el código de orden resaltado en negrita. | QR e instrucciones visibles. |
| AC-4 | Al presionar "Subir Comprobante", se abre el selector de imágenes del device. Al seleccionar una imagen, se muestra una vista previa. | Gallery picker funcional + preview. |
| AC-5 | Al presionar "Confirmar Pago", la imagen se sube a Firebase Storage y se crea el registro `pagos_pendientes` con: `orderId`, `userId`, `plan`, `amount`, `imageUrl`, `status: "pending"`, `timestamp`. | Loading → Mensaje de éxito. |
| AC-6 | Tras confirmar, se muestra pantalla "¡Pago Enviado!" con mensaje de revisión pendiente. Para el demo, el estado del usuario cambia a `hasPase = true`. | Pantalla de confirmación visible. |
| AC-7 | Si hay error de red al subir, se muestra un Snackbar con opción de reintentar. | Snackbar de error visible. |

---

## Diseño de Referencia
### Pantalla de Planes (`Mobile - PIN Validation-4.png`)
- **Header:** Barra dorada vertical + "CHOOSE YOUR ACCESS LEVEL" Bold 20px. Subtítulo: "Unlock exclusive Cusco nightlife benefits".
- **Cards de planes:** Fondo `#1C1C1C`, corner 8px.
  - Discovery Pass: "ONE-TIME PAY" tag, "S/9.90", lista de beneficios con iconos.
  - Vibe Member: Badge "RECOMMENDED" dorado, "S/19.90/mo", "1 MONTH FREE TRIAL" badge verde.
  - Gold Select: "S/49.90/mo", beneficios premium.
  - Black Founder: "INVITE ONLY / ANNUAL", badge "APPLICATION REQUIRED".

### Pantalla de Checkout (diseño nuevo)
- Fondo oscuro `#111111`.
- Card central con código de orden grande (ej. "ORD-1045") en dorado Bold 28px.
- Sección QR: imagen QR estática centrada, número de celular debajo.
- Instrucción: texto con el código en negrita dorado.
- Botón "Subir Comprobante": outline dorado, icono de cámara/galería.
- Preview del comprobante: imagen con thumbnail redondeado.
- Botón "Confirmar Pago": sólido dorado, full-width.

---

## Modelo de Datos (Firestore)
```
pagos_pendientes/{orderId}
├── orderId: "ORD-1045"
├── userId: "firebase-uid-123"
├── userEmail: "user@gmail.com"
├── plan: "discovery_pass"
├── amount: 9.90
├── currency: "PEN"
├── imageUrl: "gs://bucket/comprobantes/ORD-1045.jpg"
├── status: "pending" | "approved" | "rejected"
├── createdAt: Timestamp
```

---

## Dependencias
- Feature `auth` (userId para el registro del pago).
- Firebase Storage configurado para subir imágenes.
- QR estático de Yape/Plin (imagen hardcodeada en assets o en Firebase Storage).
