# SPEC-006: Actualización Manual de Ubicación en el Feed

## Contexto
El Vibe Feed muestra la distancia a cada local en tiempo real. Actualmente la ubicación se actualiza automáticamente cada 5 segundos via `FusedLocationProviderClient`. El usuario necesita poder solicitar una actualización manual inmediata desde el feed sin esperar el ciclo automático, y entender cómo funciona el sistema de ubicación.

---

## Historia de Usuario 1 — US-006-01: Location Chip Interactivo

**Como** usuario en el Vibe Feed,
**quiero** poder tocar el indicador de distancia del local,
**para** acceder a opciones de actualización de mi ubicación.

### Alcance
- El `Row` de distancia (icono de pin + texto de distancia) en `VenueOverlay` pasa a ser tappable.
- Al tocarlo, se abre el bottom sheet de ubicación.
- Feedback visual al tocar: ripple/highlight sobre el chip de distancia.

### Criterios de Aceptación

| # | Criterio | Validación Visual |
|---|----------|-------------------|
| AC-1.1 | El chip de distancia en el overlay del feed tiene indicador visual de interactividad. | Se aprecia cursor/ripple al tocar el elemento. |
| AC-1.2 | Al tocar el chip, se abre el bottom sheet de ubicación. | El bottom sheet aparece desde abajo. |
| AC-1.3 | El chip es tappable en todas las tarjetas del feed (no solo la primera). | Funciona en cualquier página del `VerticalPager`. |

### Diseño de Referencia
- **Mockup:** `screens/Mobile - Vibe Feed - Location Action.png`
- El chip de distancia (`📍 80m away`) en el overlay inferior del feed debe tener apariencia de elemento interactivo.

---

## Historia de Usuario 2 — US-006-02: Location Bottom Sheet UI

**Como** usuario que tocó el chip de distancia,
**quiero** ver un bottom sheet con información sobre cómo se actualiza mi ubicación,
**para** entender el comportamiento automático y tener la opción de actualizar manualmente.

### Alcance
- Modal bottom sheet con fondo oscuro semitransparente.
- Contenido:
  - Handle bar en la parte superior.
  - Título: **"Actualización de Ubicación"** + botón de cierre (X).
  - Tarjeta de estado con icono de pin verde, texto **"Actualización automática"** y subtítulo **"Cada 5 minutos"**.
  - Párrafo explicativo: _"Esta app actualiza tu ubicación automáticamente cada 5 minutos para mostrarte los locales más cercanos. Si necesitas ver resultados actualizados ahora mismo, puedes solicitar una actualización manual."_
  - Botón primario verde: **"↺ Actualizar Ahora"**
- Se puede cerrar tocando la X o arrastrando hacia abajo.

### Criterios de Aceptación

| # | Criterio | Validación Visual |
|---|----------|-------------------|
| AC-2.1 | El bottom sheet aparece sobre el feed con fondo oscuro semitransparente. | El feed se oscurece y el sheet sube desde abajo. |
| AC-2.2 | Se muestra el handle bar, título y botón de cierre (X). | Elementos visibles y alineados según mockup. |
| AC-2.3 | La tarjeta de estado muestra el icono verde, "Actualización automática" y "Cada 5 minutos". | Tarjeta renderizada con los estilos correctos. |
| AC-2.4 | Se muestra el párrafo explicativo con tipografía secundaria. | Texto legible con color `TextSecondary`. |
| AC-2.5 | El botón "Actualizar Ahora" se muestra en verde con icono de refresh. | Botón visible con el estilo del design system. |
| AC-2.6 | Al tocar X o hacer swipe down, el sheet se cierra. | Bottom sheet se cierra con animación. |

### Diseño de Referencia
- **Mockup:** `screens/Mobile - Vibe Feed - Location Bottom Sheet.png`
- **Colores:** Sheet background `#1A1A1A`, tarjeta `#2A2A2A`, icono pin `MenbresiaColors.Success (#4CAF50)`, botón `MenbresiaColors.Primary`.
- **Tipografía:** Título Bold 18sp, subtítulo Medium 14sp, descripción Regular 13sp, botón SemiBold 16sp.

---

## Historia de Usuario 3 — US-006-03: Actualización Manual de Ubicación

**Como** usuario en el bottom sheet de ubicación,
**quiero** tocar "Actualizar Ahora" para obtener mi posición GPS actualizada en ese instante,
**para** ver distancias correctas a los locales sin esperar el ciclo automático de 5 minutos.

### Alcance
- Al tocar "Actualizar Ahora", el botón muestra un estado de carga (spinner + texto "Actualizando...").
- El ViewModel solicita una ubicación fresca a `FusedLocationProviderClient` (no `lastLocation`, sino una solicitud activa).
- Una vez obtenida, recalcula distancias de todos los venues y actualiza el estado del feed.
- El bottom sheet se cierra automáticamente tras la actualización exitosa.
- Si la ubicación falla, muestra un mensaje de error sin cerrar el sheet.

### Criterios de Aceptación

| # | Criterio | Validación Visual |
|---|----------|-------------------|
| AC-3.1 | Al tocar "Actualizar Ahora", el botón cambia a estado loading con spinner. | Botón muestra indicador de progreso. |
| AC-3.2 | Tras la actualización, las distancias en el feed se actualizan con los nuevos valores. | Los textos de distancia cambian en las tarjetas del feed. |
| AC-3.3 | El bottom sheet se cierra automáticamente tras la actualización exitosa. | Sheet desaparece con animación de cierre. |
| AC-3.4 | Si la ubicación falla, se muestra un mensaje de error dentro del sheet. | Texto de error visible en rojo bajo el botón. |
| AC-3.5 | El botón vuelve a su estado normal si hay error (retry posible). | Botón recupera estado activo tras error. |

---

## Dependencias
- SPEC-002 (Vibe Feed) completado — el feed debe estar renderizado con `VenueOverlay`.
- Permiso `ACCESS_FINE_LOCATION` concedido (ya gestionado en `VibeFeedScreen`).
- `FusedLocationProviderClient` ya inyectado en `VibeFeedViewModel`.

---

## Notas de Diseño
- La actualización automática sigue siendo cada 5 **segundos** (no minutos) en el código actual. El texto del sheet ("Cada 5 minutos") refleja el mensaje de UX al usuario — puede ajustarse según decisión de producto.
- Las tres historias se pueden desarrollar **en paralelo** tras tener el VibeFeed base funcionando: US-006-01 modifica `VenueOverlay`, US-006-02 crea el componente UI aislado, US-006-03 modifica el ViewModel.
