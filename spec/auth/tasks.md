# TASKS: Autenticación con Google Sign-In

## Tareas de Implementación

### T-001.1: Setup Firebase Auth y dependencias
- [ ] Agregar dependencias de Firebase Auth, Play Services Auth y Credential Manager al `build.gradle`.
- [ ] Verificar que `google-services.json` esté correctamente configurado.
- [ ] Habilitar Google como proveedor en Firebase Console.
- **Resultado visual:** El proyecto compila sin errores.

### T-001.2: Crear modelo de dominio `User` e interfaz `AuthRepository`
- [ ] Crear `User.kt` en `domain/model` con campos: `uid`, `displayName`, `email`, `photoUrl`.
- [ ] Crear `AuthRepository.kt` en `domain/repository` con las firmas definidas en el plan.
- **Resultado visual:** N/A (capa de dominio), pero compila.

### T-001.3: Implementar `AuthRemoteDataSource` y `AuthRepositoryImpl`
- [ ] Implementar `AuthRemoteDataSource` usando `CredentialManager` para obtener Google ID Token.
- [ ] Implementar `AuthRepositoryImpl` mapeando `FirebaseUser` → `User`.
- [ ] Registrar en módulo de DI (Hilt).
- **Resultado visual:** N/A (capa de datos), pero compila.

### T-001.4: Crear Use Cases (`SignInWithGoogleUseCase`, `GetCurrentUserUseCase`)
- [ ] Implementar lógica de orquestación en cada use case.
- **Resultado visual:** N/A (capa de dominio), pero compila.

### T-001.5: Crear `LoginScreen` UI
- [ ] Diseñar la pantalla con Jetpack Compose: logo centrado, título "Menbresia AI", subtítulo, y botón "Continuar con Google".
- [ ] Aplicar paleta oscura (#111111 fondo, #D4AF37 acentos, #FFFFFF texto).
- [ ] Tipografía Space Grotesk.
- [ ] Manejar estados: `Loading` (CircularProgressIndicator), `Error` (Snackbar).
- **Resultado visual:** ✅ Pantalla de login renderizada con diseño premium.

### T-001.6: Crear `LoginViewModel` con patrón MVI
- [ ] Definir `LoginUiState` (Idle, Loading, Success, Error).
- [ ] Definir `LoginIntent` (GoogleSignInClicked).
- [ ] Conectar ViewModel con Use Cases.
- [ ] Emitir Side Effect de navegación al Feed al lograr Success.
- **Resultado visual:** Al presionar botón, se abre selector de Google y tras login exitoso se navega.

### T-001.7: Configurar navegación Auth → Feed
- [ ] Crear `AuthNavGraph` o lógica en `MainActivity` que checkee sesión activa.
- [ ] Si hay sesión → Feed. Si no → Login.
- **Resultado visual:** ✅ App redirige correctamente según estado de autenticación.

---

## Definición de "Done"
- [ ] El usuario puede hacer tap en "Continuar con Google" y autenticarse.
- [ ] Si cancela, se muestra Snackbar y permanece en la pantalla.
- [ ] Al reabrir la app con sesión activa, se salta al Feed.
- [ ] La pantalla de login se ve premium y acorde al design system (oscuro + dorado).
