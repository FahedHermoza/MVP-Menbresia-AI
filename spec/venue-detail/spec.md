# SPEC-003: Venue Detail — Detalle del Local con Geofencing

## Historia de Usuario
**Como** usuario que descubrió un local en el Feed,  
**quiero** ver la información completa del local, sus beneficios por nivel y si estoy lo suficientemente cerca para activar mi pase,  
**para** decidir si voy al local y saber exactamente qué beneficios obtendré.

---

## Contexto
La pantalla de detalle es la antesala del "Merchant PIN". Basada en el mockup `Mobile - PIN Validation-2.png`, muestra el video/imagen del local en la parte superior, información (dirección, horario, rating) y los beneficios separados por tier. El CTA principal es el botón "ACTIVATE PASS NOW" que se habilita solo por geofencing (< 50m).

## Alcance Funcional

### Incluido
- Imagen/video del local en la mitad superior con botón de cerrar (X).
- Tags sobre la imagen: badge "LIVE" (rojo) + nombre del local.
- Sección de información: dirección, horario, rating dorado.
- Lista de beneficios segmentada por nivel (Vibe Member, Gold Select, Black Founder), con el nivel del usuario resaltado con borde dorado.
- Indicador de Geofence: texto "GEOFENCE ACTIVE — YOU MUST BE NEARBY TO ACTIVATE".
- Botón "Get Closer to Activate" (deshabilitado/gris) que cambia a "ACTIVATE PASS NOW" (dorado con glow) cuando la distancia GPS es < 50 metros.
- Al presionar el botón activo → navega al flujo de PIN.

### Excluido
- Reproducción de video real (se usa imagen estática).
- Mapa embebido con la ubicación del local.
- Reviews o comentarios de otros usuarios.

---

## Criterios de Aceptación

| # | Criterio | Validación Visual |
|---|----------|-------------------|
| AC-1 | Al navegar desde el Feed, se muestra la pantalla de detalle con la imagen y datos del local seleccionado. | Pantalla de detalle renderizada correctamente. |
| AC-2 | La sección de beneficios muestra 3 cards (Vibe, Gold, Black) con sus respectivos beneficios e iconos. El nivel del usuario tiene borde dorado `#D4AF37`. | Cards de beneficios visibles y distinguibles. |
| AC-3 | Cuando el usuario está a > 50m del local, el botón muestra "Get Closer to Activate (Geofence Active)" en gris (#282828 fondo, #666666 texto). | Botón gris visible. |
| AC-4 | Cuando el usuario está a < 50m del local, el botón cambia a "ACTIVATE PASS NOW" con gradiente dorado (#F5D060 → #D4AF37 → #A07820) y efecto glow. | Botón dorado activo visible. |
| AC-5 | El botón (X) en la esquina superior izquierda cierra la pantalla y regresa al Feed. | Navegación de retorno funcional. |
| AC-6 | Al presionar el botón dorado activo, se navega al flujo de ingreso de PIN. | Navegación al PIN pad. |

---

## Diseño de Referencia
- **Mockup:** `Mobile - PIN Validation-2.png` (Venue Detail).
- **Layout Superior (340px):** Imagen full-width + overlay con botón close (círculo negro semitransparente + icono X) y tags (LIVE badge rojo + nombre).
- **Layout Inferior (scrollable):** Padding 20px. Info row (dirección + horario a la izquierda, rating dorado a la derecha). Divider `#2A2A2A`. Título "MEMBER BENEFITS" con barra dorada vertical. 3 benefit cards con fondo `#1C1C1C`.
- **CTA:** Ancho completo, 54px height. Estado deshabilitado: `#282828` con borde `#3A3A3A`. Estado activo: gradiente dorado con shadow glow `#D4AF3766`.

---

## Dependencias
- Feature `venue-feed` (de ahí viene el `venueId` para cargar los datos).
- Ubicación GPS activa para calcular distancia en tiempo real.
