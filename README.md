# Menbresia AI — MVP Repository

[![Tech Stack](https://img.shields.io/badge/Tech%20Stack-Android%20%7C%20Kotlin%20%7C%20Compose-green)](https://developer.android.com/jetpack/compose)
[![Status](https://img.shields.io/badge/Status-Development-orange)](./spec/README.md)

Este repositorio contiene el código fuente y las especificaciones para el MVP de **Menbresia AI**, una plataforma móvil diseñada para gestionar membresías exclusivas con beneficios en locales locales, validación por geofencing y pagos integrados.

---

## 🚀 Arquitectura del Proyecto

El proyecto está organizado en cuatro pilares principales:

1.  **`/mobile`**: Aplicación nativa Android desarrollada con **Kotlin** y **Jetpack Compose**.
2.  **`/spec`**: Especificaciones detalladas de cada funcionalidad (Historias de Usuario, Criterios de Aceptación y Planes Técnicos).
3.  **`/screens`**: Capturas de pantalla y diseños de referencia de la interfaz de usuario.
4.  **`/ai-tools`**: Herramientas y configuraciones para agentes de IA que asisten en el desarrollo (prompts, flujos y skills).

---

## 📋 Funcionalidades del MVP

Para un desglose detallado de las tareas y el progreso, consulta el [Índice de Especificaciones](./spec/README.md).

*   **SPEC-001: Autenticación** (Google Sign-In).
*   **SPEC-002: Vibe Feed** (Feed vertical de locales con geolocalización).
*   **SPEC-003: Venue Detail** (Detalle del local y activación de beneficios).
*   **SPEC-004: PIN Validation** (Validación presencial mediante PIN del comercio).
*   **SPEC-005: Payment Checkout** (Flujo de suscripción con carga de comprobantes).

---

## 🛠️ Stack Tecnológico

*   **Lenguaje:** Kotlin
*   **UI:** Jetpack Compose
*   **Inyección de Dependencias:** (Hilt/Koin - Ver detalle en `mobile/`)
*   **Networking:** Retrofit / Ktor
*   **Persistence:** Room / DataStore

---

## 📖 Cómo empezar

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/fahedhermoza/MVP-Menbresia-AI.git
    ```
2.  **Explorar especificaciones:** Revisa la carpeta `/spec` para entender el alcance de cada módulo antes de programar.
3.  **Abrir en Android Studio:** Importa la carpeta `/mobile` como un proyecto Gradle.

---

## 🤖 AI-Powered Development

Este proyecto utiliza agentes de IA personalizados. Consulta `/ai-tools/AGENTS.md` para más información sobre cómo interactuar con el sistema de desarrollo asistido.

---

© 2026 Menbresia AI. Todos los derechos reservados.
