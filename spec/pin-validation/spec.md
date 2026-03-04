# SPEC-004: Validación Merchant PIN + Pass Active

## Historia de Usuario
**Como** usuario que está físicamente en un local asociado,  
**quiero** ingresar el PIN de 4 dígitos que me dicta el personal del local,  
**para** activar mi pase y mostrar la pantalla de confirmación al mesero o seguridad como prueba de mi beneficio.

---

## Contexto
Este es el **flujo más crítico ("Core Loop")** de toda la aplicación. Es lo que diferencia a Menbresia AI de una simple guía turística. Basado en los mockups `Mobile - PIN Validation-1.png` (PIN pad) y `Mobile - PIN Validation.png` (Pass Active), el usuario ingresa un PIN de 4 dígitos en un teclado numérico estilizado. Si es correcto, se muestra una pantalla de éxito con el beneficio confirmado y un temporizador de validez.

## Alcance Funcional

### Incluido
- **BottomSheet / Pantalla de PIN:** Teclado numérico personalizado (0-9 + backspace) con indicadores de 4 dígitos (círculos dorados que se llenan).
- Título "ENTER MERCHANT PIN" con icono de escudo.
- Subtítulo: "Show this screen to the venue staff".
- Badge con el beneficio actual del usuario (ej. "Gold: Skip Line + 1 Free Drink").
- Botón "Cancel" para regresar.
- Validación del PIN contra Firestore (PIN estático del local).
- **Anti-fraude básico:** Máximo 3 intentos fallidos → mensaje de error y bloqueo temporal (30 segundos).
- **Pantalla de éxito "PASS ACTIVE!":** Check verde animado, nombre del beneficio confirmado (ej. "2x1 Gin Tonic"), barra de progreso de validez con cuenta regresiva (ej. "4:52 remaining"). La validez es de 5 minutos.
- Registro de la validación exitosa en Firestore (`validations_log`).

### Excluido
- PIN dinámico/rotativo (se usa PIN estático por local en MVP).
- Cooldown de 12/24 horas entre validaciones (simplificado).
- Validación offline con última ubicación conocida.

---

## Criterios de Aceptación

| # | Criterio | Validación Visual |
|---|----------|-------------------|
| AC-1 | Se muestra un BottomSheet con teclado numérico, título "ENTER MERCHANT PIN", icono de escudo dorado y badge de beneficio. | Screenshot del PIN pad. |
| AC-2 | Al ingresar cada dígito, un indicador circular cambia de gris (`#666666`) a dorado (`#D4AF37`). | Progreso visual de los 4 indicadores. |
| AC-3 | Al ingresar el 4to dígito, se valida automáticamente contra Firestore. Si es correcto → pantalla de éxito. | Transición al Pass Active. |
| AC-4 | Si el PIN es incorrecto, los indicadores hacen una animación de "shake" (vibración horizontal) y se resetean. Se muestra cuántos intentos quedan. | Animación de error visible. |
| AC-5 | Tras 3 intentos fallidos, el teclado se bloquea con mensaje "Demasiados intentos. Espera 30 segundos." y un countdown visible. | Mensaje de bloqueo visible. |
| AC-6 | La pantalla de éxito muestra: check verde animado, texto "PASS ACTIVE!", beneficio confirmado (ej. "2x1 Gin Tonic"), y barra de tiempo restante con countdown (5 min). | Screenshot del Pass Active. |
| AC-7 | Al presionar "Cancel" se regresa a la pantalla de detalle del local. | Navegación de retorno. |

---

## Diseño de Referencia
### PIN Pad (`Mobile - PIN Validation-1.png`)
- **Fondo:** BottomSheet con fondo oscuro `#1A1A1A` y handle gris superior.
- **Icono:** Escudo dorado con check.
- **Título:** "ENTER MERCHANT PIN" — Space Grotesk Bold ~16px, white.
- **Subtítulo:** "Show this screen to the venue staff" — Space Grotesk Regular 13px, gris `#A0A0A0`.
- **Badge:** Fondo con borde dorado, texto "Gold: Skip Line + 1 Free Drink", icono star.
- **Indicadores:** 4 círculos, filled = `#D4AF37`, empty = `#444444`.
- **Teclado:** Grid 3x4, botones con fondo `#2A2A2A`, corner radius ~12px, números blancos Space Grotesk 24px. Backspace icono en última celda.
- **Cancel:** Texto centrado gris inferior.

### Pass Active (`Mobile - PIN Validation.png`)
- **Fondo:** Negro `#000000` con overlay borroso del PIN pad detrás.
- **Check:** Círculo verde `#4CAF50` con borde, icono check animado (escala + fade-in).
- **Título:** "PASS ACTIVE!" — Space Grotesk Bold ~32px, blanco.
- **Subtítulo:** "Benefit Confirmed:" — Regular 14px, gris.
- **Beneficio:** "2x1 Gin Tonic" — Bold 20px, verde `#4CAF50`.
- **Timer:** "VALID FOR" label + barra de progreso verde que se va vaciando + texto "4:52 remaining" en verde a la derecha.

---

## Dependencias
- Feature `venue-detail` (de ahí viene el `venueId` y el trigger de navegación).
- El local en Firestore debe tener un campo `pin` con el valor correcto.
