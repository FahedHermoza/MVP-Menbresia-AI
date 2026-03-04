# SPEC-001: Autenticación con Google Sign-In

## Historia de Usuario
**Como** usuario nuevo de Menbresia AI,  
**quiero** iniciar sesión rápidamente con mi cuenta de Google,  
**para** acceder al contenido de la app sin tener que crear una cuenta nueva manualmente.

---

## Contexto
La autenticación es la puerta de entrada a la app. Para el MVP reducido se usa exclusivamente **Google Sign-In** vía Firebase Auth, eliminando la complejidad de múltiples proveedores u onboarding de múltiples pasos.

## Alcance Funcional

### Incluido
- Pantalla de bienvenida / splash con branding "Menbresia AI".
- Botón "Continuar con Google" prominente.
- Integración con Firebase Auth (Google Provider).
- Persistencia de sesión: si el usuario ya inició sesión, se salta al Feed directamente.
- Manejo de error básico (sin conexión, login cancelado).

### Excluido
- Login con Facebook, correo/contraseña o Apple.
- Pantalla de edición de perfil.
- Onboarding de múltiples pasos o carrusel de bienvenida.

---

## Criterios de Aceptación

| # | Criterio | Validación Visual |
|---|----------|-------------------|
| AC-1 | Al abrir la app sin sesión activa, se muestra la pantalla de login con el logo y el botón "Continuar con Google". | Captura de la pantalla de login. |
| AC-2 | Al presionar el botón, se abre el selector de cuentas de Google del sistema operativo. | Selector de cuentas visible. |
| AC-3 | Tras seleccionar una cuenta válida, se navega automáticamente al Feed. | Transición visible al Vibe Feed. |
| AC-4 | Si el usuario cancela el selector de Google, permanece en la pantalla de login y se muestra un Snackbar con mensaje informativo. | Snackbar visible. |
| AC-5 | Si el usuario abre la app con sesión activa, se redirige directamente al Feed sin mostrar pantalla de login. | App abre directo en Feed. |

---

## Diseño de Referencia
- **Paleta de colores:** Fondo `#111111`, acentos dorados `#D4AF37`, texto blanco `#FFFFFF`.
- **Tipografía:** Space Grotesk (Bold para títulos, Medium para subtítulos).
- **Componentes:** Botón redondeado con icono de Google a la izquierda, estilo premium oscuro.

---

## Dependencias
- Proyecto Firebase configurado con Auth habilitado (Google Provider).
- `google-services.json` integrado en el módulo `app`.
- Dependencias: `firebase-auth-ktx`, `play-services-auth`, `credentials`.
