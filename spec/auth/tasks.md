# TASKS: Autenticación con Google Sign-In

## Tareas de Implementación

### T-001.1: Setup Firebase Auth y dependencias
- [x] Agregar dependencias de Firebase Auth, Play Services Auth y Credential Manager al `build.gradle`.
- [x] Verificar que `google-services.json` esté correctamente configurado.
- [x] Habilitar Google como proveedor en Firebase Console.
- **Resultado visual:** El proyecto compila sin errores.

### T-001.2: Crear modelo de dominio `User` e interfaz `AuthRepository`
- [x] Crear `User.kt` en `domain/model` con campos: `uid`, `displayName`, `email`, `photoUrl`.
- [x] Crear `AuthRepository.kt` en `domain/repository` con las firmas definidas en el plan.
- **Resultado visual:** N/A (capa de dominio), pero compila.

### T-001.3: Implementar `AuthRemoteDataSource` y `AuthRepositoryImpl`
- [x] Implementar `AuthRemoteDataSource` usando `CredentialManager` para obtener Google ID Token.
- [x] Implementar `AuthRepositoryImpl` mapeando `FirebaseUser` → `User`.
- [x] Registrar en módulo de DI (Hilt) — `AuthModule.kt`.
- **Resultado visual:** N/A (capa de datos), pero compila.

### T-001.4: Crear Use Cases (`SignInWithGoogleUseCase`, `GetCurrentUserUseCase`)
- [x] Implementar lógica de orquestación en cada use case.
- **Resultado visual:** N/A (capa de dominio), pero compila.

### T-001.5: Crear `LoginScreen` UI
- [x] Diseñar la pantalla con Jetpack Compose: logo centrado, título "Menbresia AI", subtítulo, y botón "Continuar con Google".
- [x] Aplicar paleta oscura (#111111 fondo, #D4AF37 acentos, #FFFFFF texto).
- [x] Tipografía Space Grotesk.
- [x] Manejar estados: `Loading` (CircularProgressIndicator), `Error` (Snackbar).
- **Resultado visual:** ✅ Pantalla de login renderizada con diseño premium.

### T-001.6: Crear `LoginViewModel` con patrón MVI
- [x] Definir `LoginUiState` (Idle, Loading, Success, Error).
- [x] Definir `LoginIntent` (GoogleSignInClicked, ErrorDismissed).
- [x] Conectar ViewModel con Use Cases.
- [x] Emitir Side Effect de navegación al Feed al lograr Success.
- **Resultado visual:** Al presionar botón, se abre selector de Google y tras login exitoso se navega.

### T-001.7: Configurar navegación Auth → Feed
- [x] Crear `AppNavHost` con rutas LOGIN y FEED.
- [x] Crear `SplashViewModel` que checkea sesión activa al inicio.
- [x] Si hay sesión → Feed. Si no → Login.
- **Resultado visual:** ✅ App redirige correctamente según estado de autenticación.

---

## Definición de "Done"
- [x] El usuario puede hacer tap en "Continuar con Google" y autenticarse.
- [x] Si cancela, se muestra Snackbar y permanece en la pantalla.
- [x] Al reabrir la app con sesión activa, se salta al Feed.
- [x] La pantalla de login se ve premium y acorde al design system (oscuro + dorado).
