# PLAN: Autenticación con Google Sign-In

## Enfoque Técnico

### Capa de Datos (`data`)
1. **`AuthRemoteDataSource`**: Wrapper sobre `FirebaseAuth` y `CredentialManager` (nueva API recomendada por Google).
2. **`AuthRepositoryImpl`**: Implementa `AuthRepository` (interfaz en Domain). Expone:
   - `signInWithGoogle(): Result<User>`
   - `getCurrentUser(): Flow<User?>`
   - `signOut(): Result<Unit>`

### Capa de Dominio (`domain`)
1. **`AuthRepository`** (interfaz): Contrato para autenticación.
2. **`SignInWithGoogleUseCase`**: Orquesta el login, puede incluir lógica de creación de perfil en Firestore si es primer login.
3. **`GetCurrentUserUseCase`**: Retorna el usuario autenticado actual (o null).

### Capa de Presentación (`feature`)
1. **`LoginScreen`** (Composable): 
   - Layout centrado verticalmente: Logo → Título → Subtítulo → Botón Google.
   - Fondo oscuro (#111111), acentos dorados.
2. **`LoginViewModel`** (MVI):
   - `LoginUiState`: `Idle | Loading | Success | Error(message)`.
   - `LoginIntent`: `GoogleSignInClicked`.
   - Emite `SideEffect.NavigateToFeed` en éxito.

### Navegación
- **`AuthNavGraph`**: Si `getCurrentUser() != null` → navega a `FeedRoute`. Si no → `LoginRoute`.
- Se evaluará en el `MainActivity` o en un `SplashViewModel` con un check rápido.

---

## Flujo de Datos (Secuencia)
```
User taps "Continuar con Google"
  → LoginViewModel recibe Intent.GoogleSignInClicked
    → SignInWithGoogleUseCase.invoke()
      → AuthRepository.signInWithGoogle()
        → CredentialManager → Google ID Token
        → FirebaseAuth.signInWithCredential(googleCredential)
          → Success: Retorna FirebaseUser mapeado a User
    → ViewModel emite UiState.Success
      → SideEffect.NavigateToFeed
```

---

## Archivos a Crear/Modificar
| Archivo | Capa | Acción |
|---------|------|--------|
| `LoginScreen.kt` | feature/screen/auth | Crear |
| `LoginViewModel.kt` | feature/screen/auth | Crear |
| `LoginUiState.kt` | feature/screen/auth | Crear |
| `AuthRepository.kt` | domain/repository | Crear |
| `SignInWithGoogleUseCase.kt` | domain/usecase | Crear |
| `GetCurrentUserUseCase.kt` | domain/usecase | Crear |
| `AuthRepositoryImpl.kt` | data/repository | Crear |
| `AuthRemoteDataSource.kt` | data/source/remote | Crear |
| `User.kt` | domain/model | Crear |
| `AuthNavGraph.kt` | feature/navigation | Crear |
