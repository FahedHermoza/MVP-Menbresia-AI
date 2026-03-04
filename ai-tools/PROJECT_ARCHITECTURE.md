# PROJECT_ARCHITECTURE.md

## Información General
- **Nombre del proyecto**: MenbresiaAI (Android Mobile App)
- **Propósito principal**: Membresía digital de "acceso inmediato" que fusiona descubrimiento visual (estilo TikTok) con beneficios exclusivos en la vida nocturna y gastronómica de Cusco, Perú.
- **Stack tecnológico principal**: Kotlin 2.1.10, Jetpack Compose, Hilt, Firebase (Auth, Firestore, Storage), ExoPlayer, Google Play Billing, Google Maps SDK, Material 3

---

## Objetivo del Proyecto

Desarrollar un MVP Android nativo para el mercado de Cusco que permita a los usuarios (jóvenes locales 18-35 y turistas tech-savvy) descubrir locales de vida nocturna y gastronomía a través de un feed de video vertical geolocalizado, gestionar su membresía por niveles (Discovery Pass, Vibe Member, Gold, Black), y validar beneficios in-situ mediante un sistema de PIN rotativo con geofencing. El modelo de negocio se basa en suscripciones recurrentes (Google Play Billing) y pases de pago único.

---

## Arquitectura

### Patrón Arquitectónico
- **Clean Architecture** con separación en capas Data, Domain y UI
- **MVI (Model-View-Intent)** para gestión de estado en la capa de presentación
- **Modularización** por capas y funcionalidades

### Estructura de Carpetas Clave
```
menbresiaai/
├── app/                        # Módulo principal, entry point
├── feature/                    # UI screens y componentes
│   ├── screen/                # Pantallas organizadas por sección
│   │   ├── feed/              # Vibe Feed (video vertical)
│   │   ├── venuedetail/       # Detalle de local / beneficios
│   │   ├── validation/        # Flujo Merchant PIN / Geofence
│   │   ├── membership/        # Gestión de membresía y tiers
│   │   ├── profile/           # Dashboard de usuario
│   │   ├── onboarding/        # Registro y Login Social
│   │   └── payments/          # Flujo de compra / suscripción
│   ├── shared/                # Componentes y utilidades compartidas
│   └── navigation/            # Sistema de navegación
├── domain/                     # Lógica de negocio y use cases
│   ├── model/                 # Entidades de dominio
│   └── usecases/              # Casos de uso
├── data/                       # Repositorios y fuentes de datos
│   ├── repository/            # Implementaciones de repositorios
│   ├── model/                 # DTOs y mappers
│   └── source/                # Data sources (remote/local)
│       ├── remote/            # Firebase Firestore, Storage
│       └── local/             # Room / DataStore / Cache
├── designsystem/              # Sistema de diseño y theming
├── core/                      # Módulos core compartidos
│   ├── common/                # Utilidades comunes
│   ├── location/              # Geofencing, GPS, Google Maps SDK
│   ├── analytics/             # Tracking y analytics (Firebase)
│   ├── media/                 # ExoPlayer, video caching
│   └── security/              # Seguridad, cifrado, PIN handling
└── build-logic/               # Convention plugins de Gradle
```

### Capas Principales y Responsabilidades
| Capa | Responsabilidad |
|------|-----------------|
| **App** | Entry point, configuración de DI (Hilt), navegación principal |
| **Feature** | Pantallas UI con Compose, ViewModels, estados MVI (Feed, Validation, Membership, Profile, Payments, Onboarding) |
| **Domain** | Use cases, modelos de dominio, interfaces de repositorios (Venue, Membership, Validation, User) |
| **Data** | Repositorios, DTOs, mappers, data sources (Firestore, Storage, local cache), Google Play Billing wrapper |
| **Core** | Location/Geofencing, Media/ExoPlayer, Analytics, Security, utilidades comunes |
| **DesignSystem** | Componentes UI reutilizables, theming Menbresia AI, tipografía, paleta de colores |

### Dependencias Críticas entre Módulos
```
app → feature → domain → data → core
           ↘ designsystem ↙
```

> **Regla de dependencia**: Las capas internas (Domain) nunca dependen de capas externas (Data/Feature). Data implementa las interfaces definidas en Domain. Feature consume los use cases de Domain mediante inyección de dependencias.

---

## Componentes Principales

| Componente | Responsabilidad | Ubicación |
|------------|-----------------|-----------|
| **MainActivity** | Entry point, inicialización de Firebase, configuración de permisos (Location) | `app/src/main/java/.../MainActivity.kt` |
| **AppNavHost** | Navegación raíz de la aplicación (Onboarding → Feed → Profile) | `feature/.../navigation/AppNavHost.kt` |
| **FeedScreen** | Vibe Feed — reproductor de video vertical con overlay de info del local | `feature/.../screen/feed/FeedScreen.kt` |
| **FeedViewModel** | Gestión de estado del feed, carga de videos, filtrado por geolocalización | `feature/.../screen/feed/FeedViewModel.kt` |
| **VenueDetailScreen** | Detalle del local: beneficios por tier, distancia, botón "Activar Pase" | `feature/.../screen/venuedetail/VenueDetailScreen.kt` |
| **ValidationScreen** | Flujo Merchant PIN: teclado numérico, feedback de éxito, animación | `feature/.../screen/validation/ValidationScreen.kt` |
| **ValidationViewModel** | Lógica de validación PIN: geofencing check, rate limiting, cooldown | `feature/.../screen/validation/ValidationViewModel.kt` |
| **MembershipScreen** | Gestión de suscripción: selección de tier, compra, estado actual | `feature/.../screen/membership/MembershipScreen.kt` |
| **ProfileScreen** | Dashboard de usuario: estado de suscripción, tokens disponibles, sellos | `feature/.../screen/profile/ProfileScreen.kt` |
| **AuthRepository** | Autenticación via Firebase Auth (Google/Facebook Login Social) | `data/.../repository/auth/AuthRepository.kt` |
| **VenueRepository** | CRUD de locales, videos, PINs desde Firestore + Storage | `data/.../repository/VenueRepository.kt` |
| **MembershipRepository** | Estado de membresía, tokens, sellos digitales | `data/.../repository/MembershipRepository.kt` |
| **ValidationRepository** | Registro de validaciones (Vibe Validations), historial, cooldown | `data/.../repository/ValidationRepository.kt` |
| **BillingRepository** | Wrapper de Google Play Billing: suscripciones, pases únicos | `data/.../repository/BillingRepository.kt` |
| **GeofenceManager** | Gestión de geofencing: activación/desactivación por proximidad (<50m) | `core/.../location/GeofenceManager.kt` |
| **VideoPlayerManager** | ExoPlayer con precarga y caching para scroll fluido | `core/.../media/VideoPlayerManager.kt` |
| **PinSecurityManager** | Gestión de PINs rotativos, validación, rate limiting (3 intentos) | `core/.../security/PinSecurityManager.kt` |

---

## Dependencias Clave

| Librería | Versión | Propósito |
|----------|---------|----------|
| **AGP (Android Gradle Plugin)** | `8.9.1` | Sistema de build de Android |
| **Kotlin** | `2.0.21` | Lenguaje de programación |
| **Jetpack Compose** | BOM `2025.02.00` | Framework UI declarativo |
| **AndroidX Core KTX** | `1.16.0` | Extensiones Kotlin para Android |
| **Lifecycle Runtime KTX** | `2.8.7` | Ciclo de vida y coroutines |
| **Activity Compose** | `1.10.1` | Integración de Compose con Activity |
| **Material 3** | vía Compose BOM | Sistema de diseño Material You |
| **Dagger Hilt** | `2.53.1` | Inyección de dependencias |
| **Firebase BOM** | `33.10.0` | Plataforma Firebase (versión centralizada) |
| **Firebase Auth** | vía Firebase BOM | Login Social (Google / Facebook) |
| **Firebase Firestore** | vía Firebase BOM | Base de datos serverless (locales, usuarios, membresías) |
| **Firebase Storage** | vía Firebase BOM | Almacenamiento de videos de locales |
| **Firebase Analytics** | vía Firebase BOM | Tracking de eventos y métricas |
| **Firebase Crashlytics** | vía Firebase BOM | Reporte de crashes |
| **Firebase Remote Config** | vía Firebase BOM | Feature flags y configuración dinámica |
| **Google Services Plugin** | `4.4.2` | Integración de google-services.json |
| **ExoPlayer (Media3)** | `1.5.0` | Reproductor de video con precarga y caching |
| **Google Play Billing** | `7.1.1` | In-App Purchases: suscripciones y pases únicos |
| **Google Maps SDK** | `19.0.0` | Geolocalización y cálculo de distancias |
| **Google Play Services Location** | `21.3.0` | Geofencing API y GPS |
| **Navigation Compose** | `2.8.9` | Navegación entre pantallas |
| **Coil** | `2.7.0` | Carga de imágenes (thumbnails de locales) |
| **DataStore** | `1.1.4` | Persistencia local de preferencias y caché offline |
| **Room** | `2.7.1` | Cache local de datos de locales para modo offline |
| **OkHttp** | `4.12.0` | Interceptors para logging y debugging |
| **JUnit** | `4.13.2` | Framework de testing unitario |
| **AndroidX JUnit** | `1.2.1` | Extensiones JUnit para tests en Android |
| **Espresso** | `3.6.1` | Tests de UI instrumentados |

---

## Flujos Principales

### 1. Flujo de Onboarding y Autenticación
1. Usuario abre la app → `OnboardingScreen` (carrusel de valor: ahorro, estatus, descubrimiento)
2. Selecciona Login Social (Google/Facebook) → `AuthRepository.loginWithProvider()`
3. Firebase Auth crea/recupera sesión → Token almacenado
4. Primera vez: selección de tier → `MembershipScreen`
5. Navegación al `FeedScreen` (Vibe Feed)

### 2. Flujo del Vibe Feed (Descubrimiento)
1. `FeedViewModel` solicita locales cercanos via `GetNearbyVenuesUseCase`
2. `VenueRepository` consulta Firestore ordenando por proximidad GPS
3. `FeedScreen` renderiza feed vertical con `ExoPlayer` (video autoplay)
4. Smart Overlay muestra: nombre del local, distancia en tiempo real, beneficio según tier
5. Usuario hace scroll vertical → precarga de siguiente video
6. Tap en overlay → navega a `VenueDetailScreen`

### 3. Flujo de Validación (Merchant PIN)
1. Usuario en `VenueDetailScreen` → ve botón "Activar Pase" (gris = lejos, dorado = cerca)
2. `GeofenceManager` detecta proximidad <50m → botón se habilita (animación gris → dorado)
3. Usuario presiona "Activar Pase" → `ValidationScreen` muestra teclado numérico
4. Usuario ingresa PIN de 4 dígitos dictado por personal del local
5. `ValidatePinUseCase` verifica:
   - PIN correcto contra Firestore (PIN rotativo diario/semanal)
   - Rate limiting: máximo 3 intentos fallidos → bloqueo temporal
   - Cooldown: 1 validación por local cada 12/24 horas por usuario
6. Éxito → animación "Pase Activo" (mostrar al personal) + registro en Firestore
7. Fallo → feedback con intentos restantes

### 4. Flujo de Gestión de Membresía
1. Usuario accede a `MembershipScreen` desde Profile o Onboarding
2. Visualiza tiers disponibles: Discovery Pass, Vibe Member, Gold, Black
3. Selecciona tier → `BillingRepository` inicia flujo Google Play Billing
4. Pago exitoso → `MembershipRepository.activateTier()` actualiza Firestore
5. Tokens asignados según tier (Discovery: 10 fijos, Vibe: 2/mes, Gold: 4/mes, Black: ilimitado)
6. Suscripción renovada automáticamente vía Google Play

### 5. Flujo Offline (Resiliencia)
1. Usuario pierde señal GPS o datos en interiores (escenario común en Cusco)
2. App utiliza "última ubicación conocida" almacenada en DataStore
3. Si la última ubicación está dentro del geofence → permite validación
4. Validación se registra localmente (Room) y se sincroniza al recuperar conexión

---

## Configuración Importante

### Backend Serverless (Firebase)
La estrategia de backend es **100% serverless** para minimizar costos iniciales del MVP:

| Servicio Firebase | Uso |
|-------------------|-----|
| **Auth** | Login Social (Google/Facebook), gestión de sesiones |
| **Firestore** | Base de datos principal: locales, usuarios, membresías, PINs, validaciones |
| **Storage** | Almacenamiento de videos de ambientes de los locales |
| **Analytics** | Tracking de Vibe Validations, engagement del feed, conversiones |
| **Crashlytics** | Monitoreo de estabilidad en tiempo real |
| **Remote Config** | Feature flags, configuración de umbral de geofence, períodos de cooldown |

### Colecciones Firestore (MVP)
```
firestore/
├── users/                     # Datos de usuario
│   ├── {userId}/
│   │   ├── profile            # Nombre, email, avatar
│   │   ├── membership         # Tier actual, fecha inicio/fin, tokens restantes
│   │   └── validations/       # Historial de Vibe Validations
├── venues/                    # Locales registrados (50 para MVP)
│   ├── {venueId}/
│   │   ├── info               # Nombre, categoría, ubicación (GeoPoint), descripción
│   │   ├── media              # URLs de videos en Storage
│   │   ├── benefits           # Beneficios por tier (Map<tier, benefit>)
│   │   └── pin                # PIN actual (rotativo), última rotación
├── tiers/                     # Configuración de tiers
│   └── {tierId}/              # Nombre, precio, tokens, beneficios genéricos
└── validations_log/           # Log global de Vibe Validations (métricas B2B)
```

### Build Types
- **Debug**: Suffix `.dev`, debugging habilitado, Firebase Emulators opcionales
- **Release**: Optimizaciones de ProGuard/R8, ofuscación habilitada

### Integraciones con Servicios Externos
- **Firebase Suite**: Auth, Firestore, Storage, Analytics, Crashlytics, Remote Config
- **Google Play Billing**: Suscripciones y pagos únicos In-App
- **Google Maps SDK / Play Services Location**: Geolocalización, distancias, Geofencing API
- **ExoPlayer (Media3)**: Reproducción y caching de video

### Deep Links (Futuro)
- Base URI: `menbresiaai://app`
- Feed: `https://menbresiaai.com/feed`
- Venue: `https://menbresiaai.com/venue/{venueId}`
- Membership: `https://menbresiaai.com/membership`

---

## Reglas de Negocio

### Gestión de Estado (MVI)
- Usar `_state.update { it.copy(field = value) }` para actualizaciones inmutables del estado
- Cada pantalla tiene su propio `State.kt`, `Event.kt` y `ViewModel`

### Sistema de Tiers y Tokens
| Tier | Modelo de Pago | Tokens | Beneficios Clave |
|------|---------------|--------|-------------------|
| **Discovery Pass** | Pago único | 10 fijos (3 cafés, 3 bares, 2 restaurantes, 2 discos) | Acceso básico a beneficios |
| **Vibe Member** | Suscripción mensual | 2/mes | Happy Hour extendido, ingreso libre antes de medianoche |
| **Gold / Select** | Suscripción premium | 4/mes (alto valor) | Fast-Pass, reservas, prioridad |
| **Black / Founder** | All-Access | Ilimitado | Concierge, zona Backstage, Sellos Digitales |

### Validación Merchant PIN
- Botón "Activar Pase" habilitado **solo** cuando GPS detecta usuario a <50 metros del local
- PIN de 4 dígitos, rotativo (diario o semanal), configurado por admin
- **Rate Limiting**: bloqueo temporal tras 3 intentos fallidos de PIN
- **Cooldown**: máximo 1 validación exitosa por local cada 12/24 horas por usuario
- En modo offline, usar última ubicación conocida si está dentro del geofence

### Gamificación (Tier Black)
- Sellos Digitales: se desbloquean experiencias tras 5 visitas a lugares exclusivos
- Conteo de visitas almacenado en subcolección `validations/` del usuario

### Geofencing
- Radio de activación: 50 metros (configurable via Remote Config)
- Transición visual del botón: gris (fuera de rango) → dorado (dentro de rango)
- Fallback offline: última ubicación conocida almacenada en DataStore

### Labels y Recursos
- Strings de UI gestionados vía `strings.xml` (con soporte futuro para Remote Config)
- Idiomas soportados MVP: Español (es), Inglés (en)
- Todos los `contentDescription` deben usar `stringResource(R.string.key)`

---

## Testing

### Estrategia de Testing
- Tests unitarios para mappers, repositorios y use cases
- MockK como framework de mocking
- Coroutines Test para testing de código asíncrono
- Tests de integración para flujos críticos (Validación PIN, Billing)
- UI Tests con Compose Testing para flujos de usuario principales

### Tests Prioritarios (MVP)
| Área | Tests |
|------|-------|
| **Validación PIN** | `ValidatePinUseCaseTest` — PIN correcto, incorrecto, rate limiting, cooldown |
| **Geofencing** | `GeofenceManagerTest` — dentro/fuera de rango, modo offline |
| **Membresía** | `MembershipRepositoryTest` — activación de tier, consumo de tokens, expiración |
| **Feed** | `GetNearbyVenuesUseCaseTest` — ordenamiento por proximidad, filtrado por categoría |
| **Billing** | `BillingRepositoryTest` — compra exitosa, error, restauración de compras |
| **Auth** | `AuthRepositoryTest` — login social, sesión persistida, logout |
| **Mappers** | `VenueMapper`, `UserMapper`, `MembershipMapper` — transformación DTO ↔ Domain |

### Herramientas de Testing
| Herramienta | Propósito |
|-------------|-----------|
| JUnit 5 | Framework de testing |
| MockK | Mocking de dependencias |
| Turbine | Testing de Flows y StateFlows |
| Coroutines Test | Testing de coroutines |
| Compose Testing | UI tests declarativos |
| Firebase Emulators | Testing local de Auth, Firestore, Storage |

---

## Notas Importantes

### Decisiones de Diseño
- **Modularización por capas**: Facilita testing y mantenimiento independiente, esencial para un equipo de un solo desarrollador
- **Convention Plugins**: Build logic centralizado en `build-logic/` para consistencia entre módulos
- **MVI sobre MVVM**: Flujo de datos unidireccional más predecible, especialmente útil en flujos complejos como Validación PIN
- **Firebase Serverless**: Elimina costos de infraestructura y mantenimiento de servidor para el MVP
- **ExoPlayer con Caching**: Garantiza scroll fluido en el feed de video, crítico para la experiencia "tipo TikTok"
- **Google Play Billing**: Reduce complejidad de PCI y compliance de pagos, aprovecha ecosistema de Google
- **Geofencing nativo**: Play Services Location API para detección de proximidad con bajo consumo de batería
- **Spacer prohibido**: Usar `Modifier.padding()` o `Arrangement.spacedBy()` en lugar de `Spacer`

### Deuda Técnica Conocida / Pendientes
- Panel Administrativo web (para rotación de PINs y métricas) — fuera del scope de la app Android
- Modelo B2B/SaaS para locales — descartado para MVP
- Publicidad dentro de la app — descartada para MVP
- Internacionalización completa (actualmente solo ES/EN)
- CI/CD pipeline setup
- Estrategia de migración de datos post-MVP

### TODOs Críticos (MVP)
- [ ] Configuración de Firebase project y reglas de seguridad Firestore
- [ ] Integración de Google Play Billing Library
- [ ] Implementación de ExoPlayer con caching para el Vibe Feed
- [ ] Geofencing con Play Services Location
- [ ] Precarga de 50 locales de Cusco en Firestore (Shadow Launch)
- [ ] Videos scrapeados de redes sociales cargados en Firebase Storage
- [ ] Configuración de PINs genéricos para fase de prueba
- [ ] UI/UX del flujo de validación (animación gris → dorado → "Pase Activo")

### Patrón de Nomenclatura
- ViewModels: `{Feature}ViewModel.kt` (ej. `FeedViewModel.kt`, `ValidationViewModel.kt`)
- States: `State.kt` (en mismo paquete del ViewModel)
- Events: `Event.kt` (en mismo paquete del ViewModel)
- Screens: `{Feature}Screen.kt` (ej. `FeedScreen.kt`, `ValidationScreen.kt`)
- UseCases: `{Acción}{Entidad}UseCase.kt` (ej. `ValidatePinUseCase.kt`, `GetNearbyVenuesUseCase.kt`)
- Repositories: `{Entidad}Repository.kt` (ej. `VenueRepository.kt`, `MembershipRepository.kt`)
- DTOs: `{Entidad}DTO.kt` (ej. `VenueDTO.kt`, `UserDTO.kt`)
- Mappers: `{Entidad}Mapper.kt` (ej. `VenueMapper.kt`, `MembershipMapper.kt`)
- Managers: `{Feature}Manager.kt` (ej. `GeofenceManager.kt`, `VideoPlayerManager.kt`)
- Labels: `{Feature}Label.kt`
- Helpers: `{Feature}Helper.kt`

### Métricas de Éxito (KPIs)
| Métrica | Descripción | Target |
|---------|-------------|--------|
| **Vibe Validations** | PINs correctos ingresados | Métrica clave para demostrar valor B2B |
| **Tasa de Conversión** | % usuarios que pagan tras Free Trial | > 15% |
| **Frecuencia de Uso** | Aperturas y validaciones por usuario/mes | > 3 veces/mes |
