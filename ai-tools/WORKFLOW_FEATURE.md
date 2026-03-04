# WORKFLOW_FEATURE.md — Flujo de Trabajo para Desarrollo de Features en PeruPass

> Guía paso a paso para implementar cualquier feature del proyecto PeruPass (MenbresiaAI).  
> Adaptado a la arquitectura, stack y convenciones específicas del proyecto.

---

## Interpretación de Inputs

### Capturas de Pantalla (carpeta `screens/`)
- Analizar diseño visual: paleta de colores del DesignSystem, espaciado, tipografía Material 3
- Identificar elementos interactivos: botones, campos de texto, checkboxes, tier cards
- Determinar estados visuales: normal, error, loading, disabled, geofence (gris → dorado)
- Extraer textos y labels exactos → deben ir en `strings.xml` (ES + EN), nunca hardcodeados

### Especificaciones (carpeta `spec/`)
- **`spec/{NN}-{feature}/spec.md`** — Requisitos funcionales y user stories
- **`spec/{NN}-{feature}/plan.md`** — Plan técnico de implementación
- **`spec/{NN}-{feature}/tasks.md`** — Tareas granulares con IDs, estimaciones y estados

### Firebase Backend (Firestore / Storage / Auth)
- Colecciones Firestore involucradas (`users/`, `venues/`, `tiers/`, `validations_log/`)
- Estructura de documentos y subcolecciones
- Reglas de seguridad necesarias
- Operaciones: `get`, `set`, `update`, `delete`, `addSnapshotListener`
- Firebase Auth: método de login (Google/Facebook)
- Firebase Storage: URLs de videos/imágenes

### Base de Datos Local / Persistencia
- **Room**: Cache de datos de locales para modo offline
- **DataStore**: Preferencias, última ubicación conocida, cooldown, rate limiting
- Entidades a almacenar y queries necesarias

### Descripción de la Tarea
- Funcionalidad principal a implementar
- Validaciones requeridas (client-side y Firestore rules)
- Navegación post-acción (usar Navigation Compose)
- Casos edge: modo offline, geofencing fallback, rate limiting

---

## Flujo de Trabajo

### Paso 0: Lectura Obligatoria Pre-Implementación

Antes de escribir cualquier línea de código:

1. **Leer** `ai-tools/PROJECT_ARCHITECTURE.md` — Arquitectura completa, módulos, reglas de negocio
2. **Leer** `mobile/AGENTS.md` — Convenciones de código, prohibiciones, stack técnico actual
3. **Leer** `ai-tools/CONTRIBUTING.md` — Formato de commits, branching, PRs
4. **Leer** `spec/{NN}-{feature}/spec.md` → Entender requisitos y user stories
5. **Consultar** `spec/{NN}-{feature}/plan.md` → Revisar el plan técnico
6. **Seguir** `spec/{NN}-{feature}/tasks.md` → Implementar tareas en orden por ID

---

### Paso 1: Análisis y Planificación

1. Revisar todos los inputs proporcionados (capturas, specs, colecciones Firestore)
2. Identificar componentes necesarios y su ubicación en la arquitectura
3. Confirmar patrón **MVI** para la pantalla (State, Event, ViewModel, Screen)
4. Listar archivos a crear/modificar organizados por capa
5. Identificar dependencias entre tareas (ver `tasks.md`)
6. Crear branch: `<type>/#<ISSUE>-<CODE-TASK>-<brief-slug>`

---

### Paso 2: Crear Modelos de Datos (Data Layer)

```
data/model/
├── dto/
│   ├── {Feature}DTO.kt          # Objeto que mapea documento Firestore
│   └── {Related}DTO.kt          # DTOs de documentos relacionados
├── entity/
│   └── {Feature}Entity.kt       # Entidad Room (si requiere cache offline)
└── mapper/
    └── {Feature}Mapper.kt       # Transformación DTO ↔ Domain Model
```

**Acciones:**
- Crear `data class` con `@SerializedName` para campos Firestore (si se usa Gson)
- Crear entidades Room con `@Entity`, `@PrimaryKey` (si requiere persistencia offline)
- Implementar mappers bidireccionales: `toDomain()` y `toDTO()`

**Reglas del proyecto:**
- Usar `val` (nunca `var`) en data classes
- Usar tipos nullable (`?`) solo para campos opcionales
- Naming: `{Entidad}DTO.kt`, `{Entidad}Entity.kt`, `{Entidad}Mapper.kt`

---

### Paso 3: Implementar Data Sources (Data Layer)

```
data/source/
├── remote/
│   └── {Feature}RemoteDataSource.kt   # Operaciones contra Firebase
└── local/
    ├── {Feature}Dao.kt                # Room DAO (si aplica)
    └── {Feature}DataStore.kt          # DataStore preferences (si aplica)
```

**Acciones para Remote (Firebase):**
- Firestore: definir operaciones (`get`, `set`, `update`, `addSnapshotListener`)
- Storage: URLs de descarga de videos/imágenes
- Auth: operaciones de sesión (si la feature lo requiere)

**Ejemplo Firestore:**
```kotlin
class VenueRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getVenueById(venueId: String): VenueDTO? {
        return firestore.collection("venues")
            .document(venueId)
            .get()
            .await()
            .toObject(VenueDTO::class.java)
    }
}
```

**Acciones para Local (Room / DataStore):**
- Crear DAOs con queries necesarias (`@Insert`, `@Query`, `@Update`, `@Delete`)
- Configurar DataStore para preferencias simples (cooldown, rate limiting, última ubicación)

---

### Paso 4: Crear Repositorio (Data Layer)

```
data/repository/
└── {Feature}Repository.kt
```

**Acciones:**
- Implementar patrón Repository combinando remote + local data sources
- Manejar caché y sincronización offline
- Exponer `Flow<T>` para datos reactivos o `suspend fun` para operaciones one-shot
- Implementar la interfaz definida en Domain (si se usa inversión de dependencias)

**Ejemplo:**
```kotlin
class ValidationRepository @Inject constructor(
    private val remoteDataSource: ValidationRemoteDataSource,
    private val localDataSource: CooldownDataStore,
    private val mapper: ValidationMapper
) {
    suspend fun validatePin(venueId: String, pin: String): Result<ValidationResult> {
        // Verificar cooldown local
        // Llamar a Firestore
        // Registrar validación
        // Actualizar cooldown local
    }
}
```

---

### Paso 5: Implementar Use Cases (Domain Layer)

```
domain/
├── model/
│   └── {Feature}.kt              # Modelo de dominio (entidad pura)
└── usecases/
    └── {Action}{Entity}UseCase.kt # Caso de uso con lógica de negocio
```

**Acciones:**
- Crear modelos de dominio (independientes de Firebase/Room)
- Implementar use cases con lógica de negocio
- Cada use case tiene una sola responsabilidad
- Nombrar como verbo + sustantivo: `ValidatePinUseCase`, `GetNearbyVenuesUseCase`

**Ejemplo:**
```kotlin
class ValidatePinUseCase @Inject constructor(
    private val validationRepository: ValidationRepository,
    private val rateLimiter: RateLimiter
) {
    suspend operator fun invoke(venueId: String, pin: String): Result<ValidationResult> {
        // Verificar rate limiting
        // Verificar cooldown
        // Validar PIN contra Firestore
        // Registrar resultado
    }
}
```

---

### Paso 6: Implementar Estado y Eventos MVI (Feature Layer)

```
feature/screen/{feature}/
├── State.kt     # Data class con el estado de la pantalla
└── Event.kt     # Sealed class con eventos/intents del usuario
```

**State — Estructura obligatoria:**
```kotlin
data class State(
    val isLoading: Boolean = false,
    val data: {Type}? = null,
    val error: String? = null,
    // Campos específicos de la feature
)
```

**Event — Estructura obligatoria:**
```kotlin
sealed class Event {
    data class OnAction(val param: Type) : Event()
    data object OnRetry : Event()
    // Eventos específicos de la feature
}
```

---

### Paso 7: Implementar ViewModel (Feature Layer)

```
feature/screen/{feature}/
└── {Feature}ViewModel.kt
```

**Acciones:**
- Inyectar use cases via `@HiltViewModel` + `@Inject constructor`
- Gestionar estado con `StateFlow` (no LiveData)
- Manejar eventos del usuario → ejecutar use cases → actualizar estado
- Gestionar estados: Loading, Success, Error

**Ejemplo:**
```kotlin
@HiltViewModel
class ValidationViewModel @Inject constructor(
    private val validatePinUseCase: ValidatePinUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnPinDigitEntered -> handleDigitEntered(event.digit)
            is Event.OnBackspacePressed -> handleBackspace()
            is Event.OnSubmitPin -> validatePin()
        }
    }

    private fun validatePin() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // ...
        }
    }
}
```

---

### Paso 8: Crear UI con Jetpack Compose (Feature Layer)

```
feature/screen/{feature}/
├── {Feature}Screen.kt       # Composable principal
└── components/              # Componentes reutilizables de la feature
    ├── {Component}A.kt
    └── {Component}B.kt
```

**Acciones:**
- Recrear diseño fiel a las capturas de pantalla
- Conectar con ViewModel via `collectAsStateWithLifecycle()`
- Manejar estados visuales (loading, error, success)
- Extraer componentes reutilizables a `components/`

**Reglas de Compose del proyecto:**
- ⛔ **NO usar `Spacer`** → usar `Modifier.padding()` o `Arrangement.spacedBy()`
- ✅ Usar `stringResource(R.string.key)` para todo texto visible
- ✅ Usar `contentDescription` con `stringResource()` en elementos interactivos
- ✅ State hoisting: pasar state y callbacks como parámetros
- ✅ Un `@Composable` principal por archivo (para screens)
- ✅ Usar `remember` y `rememberSaveable` apropiadamente

**Ejemplo:**
```kotlin
@Composable
fun ValidationScreen(
    viewModel: ValidationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ValidationContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ValidationContent(
    state: State,
    onEvent: (Event) -> Unit
) {
    // UI implementation
}
```

---

### Paso 9: Implementar Strings (i18n)

```
mobile/app/src/main/res/
├── values/strings.xml          # Español (default)
└── values-en/strings.xml       # Inglés
```

**Acciones:**
- Agregar todos los textos de la feature en ambos archivos
- Naming convention: `{feature}_{component}_{description}`
- Ejemplo: `validation_pin_title`, `validation_error_rate_limited`
- Usar `stringResource(R.string.key)` en Compose

---

### Paso 10: Implementar Validaciones

**Validaciones comunes en PeruPass:**
- Campos vacíos en formularios
- Formato de PIN (4 dígitos numéricos)
- Rate limiting (3 intentos fallidos → bloqueo temporal)
- Cooldown por venue (1 validación cada 12-24h)
- Geofencing (distancia < 50m del local)
- Estado de membresía (tier válido, tokens disponibles)

**Dónde validar:**
- **UI (Screen)**: Feedback visual inmediato (campos rojos, shake animation)
- **ViewModel**: Lógica de validación y estados (rate limiting, cooldown)
- **UseCase**: Reglas de negocio (tokens, geofencing, PIN correcto)
- **Firestore Rules**: Validación definitiva server-side

---

### Paso 11: Manejo de Navegación

```kotlin
// En AppNavHost (feature/navigation/AppNavHost.kt)
composable("validation/{venueId}") { backStackEntry ->
    val venueId = backStackEntry.arguments?.getString("venueId") ?: return@composable
    ValidationScreen(venueId = venueId)
}
```

**Acciones:**
- Registrar nueva ruta en `AppNavHost`
- Implementar navegación post-éxito (ej: PIN válido → PassActiveScreen)
- Pasar argumentos entre pantallas via route parameters
- Limpiar backstack apropiadamente con `popUpTo`

---

### Paso 12: Manejo de Errores

**Tipos de error en PeruPass:**
- **Network errors**: Sin conexión, timeout (escenario muy común en Cusco)
- **Firebase errors**: Firestore unavailable, Auth expired, Storage timeout
- **Validation errors**: PIN incorrecto, rate limited, cooldown activo
- **Geofencing errors**: GPS deshabilitado, fuera de rango, permiso denegado
- **Billing errors**: Compra cancelada, pago fallido, producto no encontrado

**Implementación con sealed class:**
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(
        val message: String,
        val type: ErrorType = ErrorType.GENERIC
    ) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

enum class ErrorType {
    NETWORK, FIREBASE, VALIDATION, GEOFENCE, BILLING, GENERIC
}
```

**Offline Resilience:**
- Usar cache Room para datos de locales
- Última ubicación conocida en DataStore
- Registrar validaciones localmente y sincronizar al recuperar conexión

---

### Paso 13: Dependency Injection (Hilt)

```
di/
└── {Feature}Module.kt
```

**Acciones:**
- Crear módulo Hilt con `@Module` + `@InstallIn(SingletonComponent::class)`
- Proveer data sources, repositories y use cases
- Usar `@Provides` para dependencias de Firebase y `@Binds` para interfaces

**Ejemplo:**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object ValidationModule {

    @Provides
    @Singleton
    fun provideValidationRepository(
        remoteDataSource: ValidationRemoteDataSource,
        cooldownDataStore: CooldownDataStore,
        mapper: ValidationMapper
    ): ValidationRepository {
        return ValidationRepository(remoteDataSource, cooldownDataStore, mapper)
    }
}
```

---

### Paso 14: Testing

**Herramientas**: JUnit 4, MockK, Turbine, Coroutines Test

**Tests obligatorios por capa:**

| Capa | Test | Naming |
|------|------|--------|
| **Mappers** | DTO ↔ Domain transformaciones | `{Feature}MapperTest.kt` |
| **UseCases** | Lógica de negocio + edge cases | `{Action}{Entity}UseCaseTest.kt` |
| **Repositories** | Integración de data sources (mocked) | `{Feature}RepositoryTest.kt` |
| **ViewModels** | Transiciones de estado MVI (futuro) | `{Feature}ViewModelTest.kt` |

**Ejemplo:**
```kotlin
class ValidatePinUseCaseTest {

    @MockK private lateinit var repository: ValidationRepository
    @MockK private lateinit var rateLimiter: RateLimiter

    private lateinit var useCase: ValidatePinUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = ValidatePinUseCase(repository, rateLimiter)
    }

    @Test
    fun `given correct pin when validate then return success`() = runTest {
        // Given
        coEvery { rateLimiter.canAttempt(any()) } returns true
        coEvery { repository.validatePin(any(), any()) } returns Result.Success(validResult)

        // When
        val result = useCase("venue123", "1234")

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }
}
```

**Comandos:**
```bash
cd mobile
./gradlew testDebugUnitTest                     # Unit tests
./gradlew lint                                  # Static analysis
./gradlew connectedDebugAndroidTest             # Instrumented tests (si aplica)
```

---

## Estructura de Archivos Generados por Feature

```
# === Data Layer ===
data/
├── model/
│   ├── dto/{Feature}DTO.kt
│   ├── entity/{Feature}Entity.kt          # Solo si requiere cache offline
│   └── mapper/{Feature}Mapper.kt
├── source/
│   ├── remote/{Feature}RemoteDataSource.kt
│   └── local/
│       ├── {Feature}Dao.kt                # Solo si usa Room
│       └── {Feature}DataStore.kt          # Solo si usa DataStore
└── repository/
    └── {Feature}Repository.kt

# === Domain Layer ===
domain/
├── model/{Feature}.kt
└── usecases/{Action}{Entity}UseCase.kt

# === Feature Layer (UI) ===
feature/screen/{feature}/
├── {Feature}Screen.kt
├── {Feature}ViewModel.kt
├── State.kt
├── Event.kt
└── components/
    └── {Component}.kt

# === DI ===
di/{Feature}Module.kt

# === Resources ===
app/src/main/res/values/strings.xml           # +entries ES
app/src/main/res/values-en/strings.xml        # +entries EN

# === Tests ===
app/src/test/.../
├── {Feature}MapperTest.kt
├── {Action}{Entity}UseCaseTest.kt
└── {Feature}RepositoryTest.kt
```

---

## Convenciones de Código (Resumen)

### Naming
| Tipo | Patrón | Ejemplo |
|------|--------|---------|
| Screen | `{Feature}Screen.kt` | `ValidationScreen.kt` |
| ViewModel | `{Feature}ViewModel.kt` | `FeedViewModel.kt` |
| State | `State.kt` (en paquete de feature) | `feature/screen/feed/State.kt` |
| Event | `Event.kt` (en paquete de feature) | `feature/screen/feed/Event.kt` |
| UseCase | `{Acción}{Entidad}UseCase.kt` | `ValidatePinUseCase.kt` |
| Repository | `{Entidad}Repository.kt` | `VenueRepository.kt` |
| DTO | `{Entidad}DTO.kt` | `VenueDTO.kt` |
| Mapper | `{Entidad}Mapper.kt` | `MembershipMapper.kt` |
| Manager | `{Feature}Manager.kt` | `GeofenceManager.kt` |
| Helper | `{Feature}Helper.kt` | `ValidationHelper.kt` |

### Kotlin
- `data class` con `val` (inmutabilidad)
- `sealed class` para estados y eventos
- `Flow` / `StateFlow` (nunca `LiveData`)
- `suspend fun` para operaciones async
- Clean Code, DRY, YAGNI, KISS, SOLID

### Compose
- ⛔ No usar `Spacer` → `Modifier.padding()` o `Arrangement.spacedBy()`
- ✅ `stringResource(R.string.key)` para todo texto
- ✅ State hoisting
- ✅ Un `@Composable` principal por archivo

---

## Checklist de Completitud

Antes de marcar la tarea como completa, verificar:

### UI
- [ ] Todos los elementos de UI implementados según captura/spec  
- [ ] Estilos y colores del DesignSystem aplicados correctamente  
- [ ] Estados de loading/success/error visibles  
- [ ] No hay hardcoded strings (usar `strings.xml` ES + EN)
- [ ] `contentDescription` con `stringResource()` en todos los elementos interactivos

### Data Layer
- [ ] DTOs mapean correctamente documentos Firestore  
- [ ] Mappers transforman DTO ↔ Domain correctamente  
- [ ] Repository combina remote + local apropiadamente  
- [ ] Cache offline implementado (si requerido por spec)

### Domain Layer
- [ ] Use cases implementan reglas de negocio del `spec.md`  
- [ ] Modelos de dominio independientes de Firebase/Room

### Feature Layer (MVI)
- [ ] `State.kt` con todos los campos necesarios  
- [ ] `Event.kt` con todos los eventos de usuario  
- [ ] Navegación funciona según spec

### Reglas de Negocio (según feature)
- [ ] Geofencing: botón gris/dorado según proximidad (< 50m)  
- [ ] Rate limiting: bloqueo tras 3 intentos fallidos  
- [ ] Cooldown: 1 validación/venue cada 12-24h  
- [ ] Tokens: consumo correcto según tier  
- [ ] Offline: fallback con última ubicación conocida

### Testing
- [ ] Tests unitarios de Mappers  
- [ ] Tests unitarios de UseCases (happy path + edge cases)  
- [ ] Tests unitarios de Repository (con MockK)  
- [ ] `./gradlew testDebugUnitTest` pasa ✅  
- [ ] `./gradlew lint` sin errores ✅

### Git
- [ ] Commit format: `<type>: #<ISSUE> <CODE-TASK> <Brief description>`  
- [ ] Branch format: `<type>/#<ISSUE>-<CODE-TASK>-<brief-slug>`  
- [ ] Un solo commit por PR (usar `--amend` para updates)  
- [ ] Sin imports no utilizados ni dead code

---

## Prioridades de Implementación

1. **Data Layer primero** (bottom-up): DTOs → Data Sources → Repository
2. **Domain Layer**: Models → Use Cases
3. **Feature Layer**: State/Event → ViewModel → Screen/Components
4. **Strings**: `strings.xml` (ES + EN)
5. **DI**: Hilt Module
6. **Testing**: Mappers → UseCases → Repository
7. **Polish**: Animaciones, transiciones, edge cases

---

## Notas Importantes

### Cuando hay ambigüedad
- Seguir patrones existentes en el proyecto
- Consultar `spec/{NN}-{feature}/plan.md` para decisiones de diseño
- Aplicar mejores prácticas de Android/Kotlin
- Preguntar si la decisión es crítica para el negocio

### Seguridad
- NO almacenar PINs en plain text localmente
- Usar HTTPS/TLS siempre (Firebase lo maneja)
- Validar en cliente Y en Firestore Security Rules
- Sanitizar inputs
- Tokens de autenticación gestionados por Firebase Auth

### Offline Resilience (Contexto Cusco)
- Escenario muy común: pérdida de señal en interiores de locales
- Siempre tener fallback con última ubicación conocida (DataStore)
- Cache de datos de locales en Room para consulta offline
- Registrar validaciones localmente y sincronizar al recuperar conexión

---

## Output Esperado al Completar una Feature

1. **Código fuente** de todos los archivos creados/modificados
2. **Tasks actualizadas** en `spec/{NN}-{feature}/tasks.md` (marcar ✅ completados)
3. **Strings** agregados en `strings.xml` (ES + EN)
4. **Tests** pasando: `./gradlew testDebugUnitTest` ✅
5. **Commit** con formato: `feat: #<ISSUE> <CODE-TASK> <description>`
6. **Dependencias** nuevas agregadas en `gradle/libs.versions.toml` (si aplica)
7. **Resumen** de cambios realizados y próximos pasos sugeridos
