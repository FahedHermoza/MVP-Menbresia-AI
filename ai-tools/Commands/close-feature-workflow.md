---
description: Finaliza el flujo de una funcionalidad cerrando el PR, eliminando la rama y actualizando/cerrando los issues relacionados.
---
1. Recibe los parámetros `PR_LINK`, `ISSUE_REF` y `TASKS_FILE` del usuario.
2. Valida el estado del Pull Request en `PR_LINK`. Si está validado, procede a cerrarlo utilizando la estrategia **Rebase and Merge**.
3. Una vez que los cambios se hayan integrado correctamente en la rama `develop`, procede a eliminar la rama de origen del PR local y remotamente para mantener el repositorio limpio.
4. Localiza el archivo de tareas especificado en `TASKS_FILE` (por ejemplo, `spec/auth/tasks.md`) y lee su contenido para verificar el progreso de las tareas.
5. Actualiza la descripción del issue relacionado en `ISSUE_REF` (GitHub Issue) incluyendo un resumen del estado final de las tareas basado en el archivo `TASKS_FILE` del último commit ingresado, actualiza los checklist de la desripción del issue.
6. Finalmente, procede a cerrar el issue relacionado en GitHub si todas las tareas han sido completadas satisfactoriamente.

### Ejemplo de Uso
Puedes ejecutar este workflow directamente usando el comando:
@close-feature-workflow.md PR_LINK="https://github.com/FahedHermoza/MVP-Menbresia-AI/pull/7" ISSUE_REF="https://github.com/FahedHermoza/MVP-Menbresia-AI/issues/2" TASKS_FILE="spec/auth/tasks.md"
