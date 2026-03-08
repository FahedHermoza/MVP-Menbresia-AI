# Menbresia AI — Arquitectura del Sistema

Diagrama visual y detallado de las tecnologías utilizadas en la aplicación Mobile Android y el backend serverless Firebase.

---

```
╔══════════════════════════════════════════════════════════════════════════════════════════╗
║                         MENBRESIA AI — ARQUITECTURA DEL SISTEMA                         ║
║                              Android Native + Firebase Serverless                        ║
╚══════════════════════════════════════════════════════════════════════════════════════════╝


┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                              MOBILE — ANDROID NATIVE                                    │
│                         Kotlin 2.0.21 · Jetpack Compose · API 29+                      │
└─────────────────────────────────────────────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────────────────────────────────────┐
│  PRESENTATION LAYER  (feature/)                    Patrón: MVI                        │
│                                                                                       │
│  ┌────────────────┐  ┌──────────────────┐  ┌──────────────────┐  ┌────────────────┐  │
│  │  LoginScreen   │  │  VibeFeedScreen  │  │VenueDetailScreen │  │PinValidation   │  │
│  │  ────────────  │  │  ──────────────  │  │  ──────────────  │  │  Screen        │  │
│  │LoginViewModel  │  │VibeFeedViewModel │  │VenueDetailVM     │  │  ──────────    │  │
│  │                │  │                  │  │                  │  │PinValidationVM │  │
│  │Intent          │  │State (venues,    │  │DetailUiState     │  │                │  │
│  │LoginUiState    │  │ distances, loc)  │  │DetailIntent      │  │UiState         │  │
│  └────────────────┘  │Event             │  │DetailSideEffect  │  │Intent          │  │
│                      │FeedSideEffect    │  └──────────────────┘  │SideEffect      │  │
│  ┌────────────────┐  └──────────────────┘                        └────────────────┘  │
│  │CheckoutScreen  │                         ┌──────────────────┐                     │
│  │  ────────────  │  ┌──────────────────┐   │MembershipScreen  │                     │
│  │CheckoutVM      │  │PaymentConfirm    │   │  (sin VM)        │                     │
│  │                │  │  Screen (sin VM) │   │  ──────────────  │                     │
│  │CheckoutUiState │  └──────────────────┘   │PlanCard          │                     │
│  │CheckoutIntent  │                         └──────────────────┘                     │
│  │CheckoutSideEff │                                                                   │
│  └────────────────┘                                                                   │
│                                                                                       │
│  Base:  BaseViewModel ──► BaseSideEffectViewModel<SE>                                 │
│         └─ launch() helper (viewModelScope)   └─ SharedFlow<SideEffect>               │
│                                                                                       │
│  ┌────────────────────────────────────────────────────────────┐                       │
│  │  NAVEGACIÓN  (AppNavHost + NavRoutes)                      │                       │
│  │                                                            │                       │
│  │  LOGIN ──► FEED ──► VENUE_DETAIL ──► PIN_VALIDATION        │                       │
│  │              │                                             │                       │
│  │              └──► PASSES ──► CHECKOUT ──► PAYMENT_CONFIRM  │                       │
│  │              └──► EXPLORE  (placeholder)                   │                       │
│  │              └──► PROFILE  (placeholder)                   │                       │
│  └────────────────────────────────────────────────────────────┘                       │
│                                                                                       │
│  Design System:  MenbresiaColors  ·  PeruPassTheme (Material 3)                       │
│  Background #111111 · Surface #1C1C1C · Primary Gold #D4AF37                         │
└───────────────────────────────────────────────────────────────────────────────────────┘
                                         │
                                         │  Hilt DI
                                         ▼
┌───────────────────────────────────────────────────────────────────────────────────────┐
│  DOMAIN LAYER  (domain/)                           Patrón: Clean Architecture         │
│                                                                                       │
│  ┌─────────────────────────────────────────────────────────────────────────────────┐  │
│  │  USE CASES (8)                                                                  │  │
│  │                                                                                 │  │
│  │  SignInWithGoogleUseCase    GetCurrentUserUseCase    GetVenuesUseCase           │  │
│  │  GetVenueByIdUseCase        CheckProximityUseCase    ValidatePinUseCase         │  │
│  │  SubmitPaymentUseCase       GenerateOrderIdUseCase                              │  │
│  └─────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                       │
│  ┌───────────────────────┐   ┌─────────────────────────────────────────────────────┐  │
│  │  MODELS               │   │  REPOSITORY INTERFACES (5)                          │  │
│  │  ─────────────────    │   │  ─────────────────────────                          │  │
│  │  Venue                │   │  AuthRepository      VenueRepository                │  │
│  │  User                 │   │  ValidationRepository LocationRepository            │  │
│  │  UserLocation         │   │  PaymentRepository                                  │  │
│  │  ProximityResult      │   └─────────────────────────────────────────────────────┘  │
│  │  ValidationResult     │                                                            │
│  │   ├─ Success          │   ┌─────────────────────────────────────────────────────┐  │
│  │   ├─ WrongPin         │   │  LÓGICA EN USECASE                                  │  │
│  │   └─ Blocked          │   │  CheckProximity:  Haversine < 50m                   │  │
│  │  PaymentRecord        │   │  ValidatePin:     3 intentos → bloqueo 30s          │  │
│  └───────────────────────┘   │  SubmitPayment:   upload → record → activatePass    │  │
│                              └─────────────────────────────────────────────────────┘  │
└───────────────────────────────────────────────────────────────────────────────────────┘
                                         │
                                         │  implements
                                         ▼
┌───────────────────────────────────────────────────────────────────────────────────────┐
│  DATA LAYER  (data/)                                                                  │
│                                                                                       │
│  ┌──────────────────────────────────────────────────────────────────────────────┐     │
│  │  REPOSITORIES (Implementations)                                              │     │
│  │  AuthRepositoryImpl  ·  VenueRepositoryImpl  ·  ValidationRepositoryImpl     │     │
│  │  LocationRepositoryImpl  ·  PaymentRepositoryImpl                            │     │
│  └──────────────────────────────────────────────────────────────────────────────┘     │
│                        │                                                              │
│                        ▼                                                              │
│  ┌──────────────────────────────────────────────────────────────────────────────┐     │
│  │  REMOTE DATA SOURCES                                                         │     │
│  │                                                                              │     │
│  │  AuthRemoteDataSource        VenueRemoteDataSource                           │     │
│  │  ValidationRemoteDataSource  PaymentRemoteDataSource                         │     │
│  └──────────────────────────────────────────────────────────────────────────────┘     │
│                                                                                       │
│  ┌───────────────────────────┐   ┌──────────────────────────┐                        │
│  │  DTOs & Mappers           │   │  Utilidades              │                        │
│  │  VenueDTO  ──►  Venue     │   │  HaversineHelper         │                        │
│  │  FirebaseUserMapper       │   │  AuthContextHolder        │                        │
│  └───────────────────────────┘   │  LocationDataSource       │                        │
│                                  └──────────────────────────┘                        │
│                                                                                       │
│  DI MODULES (Hilt @SingletonComponent)                                                │
│  AuthModule · VenueModule · ValidationModule · PaymentModule                          │
└───────────────────────────────────────────────────────────────────────────────────────┘
                                         │
                                         │  Firebase SDK (BOM 33.10.0)
                                         ▼
╔═══════════════════════════════════════════════════════════════════════════════════════╗
║  BACKEND — FIREBASE SERVERLESS                      Proyecto: menbresia-ai-mvp       ║
╠═══════════════════════════════════════════════════════════════════════════════════════╣
║                                                                                       ║
║  ┌─────────────────────────────┐                                                     ║
║  │  FIREBASE AUTH              │                                                     ║
║  │  ─────────────────────────  │                                                     ║
║  │  ✦ Proveedor: Google        │                                                     ║
║  │  ✦ CredentialManager API    │                                                     ║
║  │  ✦ GoogleIdTokenCredential  │                                                     ║
║  │  ✦ signInWithCredential()   │                                                     ║
║  │  ✦ currentUser (uid, email) │                                                     ║
║  │  ✦ signOut() + clearState   │                                                     ║
║  └─────────────────────────────┘                                                     ║
║                                                                                       ║
║  ┌──────────────────────────────────────────────────────────────────────────────┐    ║
║  │  CLOUD FIRESTORE                                                             │    ║
║  │  ─────────────────────────────────────────────────────────────────────────   │    ║
║  │                                                                              │    ║
║  │  venues/                          validations_log/                           │    ║
║  │  └─ {venueId}                     └─ {logId}                                │    ║
║  │       ├─ name, address                  ├─ userId                           │    ║
║  │       ├─ latitude, longitude            ├─ venueId, venueName               │    ║
║  │       ├─ imageUrl                       ├─ benefit                          │    ║
║  │       ├─ hours, days, rating            ├─ timestamp                        │    ║
║  │       ├─ isOpen: Boolean                └─ status: "success"                │    ║
║  │       ├─ pin: String  ◄─ PIN validation                                     │    ║
║  │       └─ benefits: Map<tier, text>                                          │    ║
║  │                                                                              │    ║
║  │  users/                           outstanding_payments/                      │    ║
║  │  └─ {userId}                      └─ {orderId}                              │    ║
║  │       └─ hasPase: Boolean               ├─ userId, userEmail                │    ║
║  │           ▲                             ├─ plan: "gold_select"              │    ║
║  │           └── activatePass()            ├─ amount: 49.90, currency:"PEN"    │    ║
║  │                                         ├─ imageUrl (Storage URL)           │    ║
║  │                                         ├─ status: "pending"                │    ║
║  │                                         └─ createdAt: Timestamp             │    ║
║  └──────────────────────────────────────────────────────────────────────────────┘    ║
║                                                                                       ║
║  ┌─────────────────────────────────────────────────────────────────────────────┐     ║
║  │  FIREBASE STORAGE                        ⚠ Requiere plan Blaze             │     ║
║  │  ─────────────────────────────────────────────────────────────────────────  │     ║
║  │  Bucket: menbresia-ai-mvp.firebasestorage.app                               │     ║
║  │  Path:   receipts/{orderId}.jpg                                             │     ║
║  │  Rules:  allow read, write: if request.auth != null                         │     ║
║  │  Uso:    Subida de comprobantes de pago (imagen del usuario)                │     ║
║  └─────────────────────────────────────────────────────────────────────────────┘     ║
╚═══════════════════════════════════════════════════════════════════════════════════════╝


╔═══════════════════════════════════════════════════════════════════════════════════════╗
║  FLUJOS PRINCIPALES                                                                   ║
╠═══════════════════════════════════════════════════════════════════════════════════════╣
║                                                                                       ║
║  1. AUTENTICACIÓN                                                                     ║
║     Usuario ──► Google Sign-In ──► CredentialManager ──► FirebaseAuth ──► FEED       ║
║                                                                                       ║
║  2. DESCUBRIMIENTO DE VENUES                                                          ║
║     FEED ──► Firestore(venues) ──► HaversineHelper(distancia) ──► VenueCard          ║
║                                                                                       ║
║  3. VALIDACIÓN EN LOCAL (Geofencing)                                                  ║
║     VenueDetail ──► FusedLocation ──► Haversine < 50m ──► PIN Screen                 ║
║     PIN Screen ──► Firestore(venues/pin) ──► Comparar ──► Log(validations_log)       ║
║     3 intentos fallidos ──► Bloqueo 30s (in-memory)                                  ║
║                                                                                       ║
║  4. PAGO Y MEMBRESÍA                                                                  ║
║     MembershipScreen ──► CheckoutScreen ──► PhotoPicker(imagen)                       ║
║     ──► Storage(receipts/) ──► Firestore(outstanding_payments)                        ║
║     ──► Firestore(users/hasPase=true) ──► PaymentConfirmation                        ║
╚═══════════════════════════════════════════════════════════════════════════════════════╝


╔═══════════════════════════════════════════════════════════════════════════════════════╗
║  STACK TECNOLÓGICO                                                                    ║
╠═══════════╦══════════════════════════════╦══════════════╦══════════════════════════════╣
║  Categoría║  Tecnología                  ║  Versión     ║  Uso                        ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  UI       ║  Jetpack Compose             ║  BOM 25.02   ║  Toda la UI declarativa     ║
║           ║  Material 3                  ║  via BOM     ║  Componentes y temas        ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  DI       ║  Dagger Hilt                 ║  2.53.1      ║  Inyección de dependencias  ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  Nav      ║  Navigation Compose          ║  2.8.9       ║  Ruteo entre pantallas      ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  Estado   ║  StateFlow + SharedFlow      ║  Kotlin      ║  MVI state + side effects   ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  Imágenes ║  Coil                        ║  2.7.0       ║  Carga de imágenes async    ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  Auth     ║  Firebase Auth               ║  BOM 33.10   ║  Google Sign-In             ║
║           ║  CredentialManager           ║  1.3.0       ║  Auth moderna (API 29+)     ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  DB       ║  Cloud Firestore             ║  BOM 33.10   ║  venues, users, payments    ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  Storage  ║  Firebase Storage            ║  BOM 33.10   ║  Comprobantes de pago       ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  Location ║  FusedLocationProviderClient ║  21.3.0      ║  GPS del dispositivo        ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  Geo      ║  HaversineHelper (on-device) ║  custom      ║  Distancia lat/lng → metros ║
╠═══════════╬══════════════════════════════╬══════════════╬══════════════════════════════╣
║  Test     ║  MockK + Coroutines Test     ║  1.13.10     ║  Unit tests UseCase/VM      ║
╚═══════════╩══════════════════════════════╩══════════════╩══════════════════════════════╝
```

---

> Última actualización: Marzo 2026 · SPEC-005 implementado
