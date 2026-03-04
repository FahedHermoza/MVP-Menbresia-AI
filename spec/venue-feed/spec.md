# SPEC-002: Vibe Feed — Lista de Locales

## Historia de Usuario
**Como** usuario autenticado de Menbresia AI,  
**quiero** ver un feed vertical con los locales disponibles cerca de mí,  
**para** descubrir rápidamente bares, restaurantes y discotecas con beneficios exclusivos.

---

## Contexto
El Vibe Feed es la pantalla principal post-login. Basado en el mockup `Mobile - PIN Validation-3.png`, muestra un feed estilo TikTok con video/imagen a pantalla completa, nombre del local, distancia, estado (OPEN NOW), beneficio del nivel del usuario, y acciones laterales (heart, share, save). Para el MVP, se simplificará a imágenes de alta calidad con datos pre-cargados en Firestore.

## Alcance Funcional

### Incluido
- Feed vertical de scroll con imágenes a pantalla completa (3-5 locales mockeados en Firestore).
- Overlay inferior con: nombre del local (bold), distancia calculada GPS, estado "OPEN NOW", beneficio destacado del usuario (ej. "GOLD MEMBER: Skip the Line + 1 Free Drink").
- Acciones laterales (heart, share, save) — solo iconos visuales, sin funcionalidad real en MVP.
- Bottom Navigation Bar con 4 tabs: **FEED** (activo), EXPLORE, PASSES, PROFILE (los demás como placeholders).
- Al hacer tap en la card/overlay inferior, se navega al **Venue Detail**.

### Excluido
- Reproducción real de video (se usarán imágenes estáticas).
- Filtros de búsqueda o categorías.
- Funcionalidad real de like/share/save.
- Tabs EXPLORE, PASSES y PROFILE funcionales (solo UI placeholder).

---

## Criterios de Aceptación

| # | Criterio | Validación Visual |
|---|----------|-------------------|
| AC-1 | Al navegar al Feed, se muestra la primera imagen/local a pantalla completa con overlay de información. | Captura del feed con la imagen y el overlay. |
| AC-2 | Se puede hacer scroll vertical para ver los siguientes locales (snap scrolling). | Scroll fluido entre locales. |
| AC-3 | El overlay muestra: nombre del local, distancia en metros, estado "OPEN NOW" y el beneficio del nivel actual. | Datos visibles y legibles. |
| AC-4 | Los iconos de heart (con contador "2.4K"), share y save se muestran en el lateral derecho. | Iconos visibles. |
| AC-5 | El Bottom Navigation muestra los 4 tabs con el tab "FEED" visualmente activo (resaltado). | Bottom bar visible con tab activo. |
| AC-6 | Al hacer tap en el overlay o la imagen, se navega a la pantalla de detalle del local. | Navegación funcional al detalle. |

---

## Diseño de Referencia
- **Mockup:** `Mobile - PIN Validation-3.png` (Vibe Feed).
- **Layout:** Imagen full-screen + gradient inferior oscuro + overlay con datos del local. Status bar con hora y señal.
- **Colores:** Fondo negro, texto blanco, tags en dorado `#D4AF37`, badge "LIVE" en rojo `#F44336`.
- **Tipografía:** Space Grotesk. Nombre del local en Bold 24-28px, distancia en Regular 12px, beneficio en Semi-Bold 12px.
- **Bottom Nav:** Iconos Lucide (flame/FEED, columns/EXPLORE, ticket/PASSES, user/PROFILE).

---

## Modelo de Datos (Firestore)
```
venues/{venueId}
├── name: "CHANGU CLUB"
├── address: "Avenida El Sol 123, Cusco"
├── latitude: -13.5226
├── longitude: -71.9673
├── imageUrl: "https://..."
├── hours: "10PM — 4AM"
├── days: "Thur–Sun"
├── rating: 4.8
├── isOpen: true
├── pin: "1234"
├── benefits: {
│     "discovery": "Cover fee + welcome drink included",
│     "vibe": "Extended Happy Hour until midnight",
│     "gold": "Skip the Line + 1 Free Drink",
│     "black": "All-Access + Private Concierge"
│   }
```

---

## Dependencias
- Feature `auth` completada (el usuario debe estar autenticado para ver el Feed).
- Datos pre-cargados en Firestore con al menos 3-5 locales de prueba.
- Dependencias: `firebase-firestore-ktx`, `coil-compose` (carga de imágenes), `play-services-location`.
