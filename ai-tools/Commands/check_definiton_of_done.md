---
description: Verifica que la funcionalidad cumpla con la definición de hecho (Definition of Done) antes de crear un PR, revisando la calidad del código, chequeos locales y actualizando las tareas.
---
1. Recibe el parámetro `TASK_DIR` del usuario.
2. Revisa los ficheros modificados y asegúrate de que no lleven comentarios innecesarios, los cuales deben ser eliminados, a excepción de comentarios muy importantes o estrictamente necesarios. Recuerda que es mejor tener una variable declarada con un *naming* correcto a tener comentarios explicatorios.
3. Verifica que se cumpla minuciosamente el **Code Quality Checklist** detallado en el archivo `MVP-MenbresiaAI/ai-tools/CONTRIBUTING.md`.
4. Verifica que se cumpla con todos los pasos de la sección de **Local Verification Before Push** del archivo `MVP-MenbresiaAI/ai-tools/CONTRIBUTING.md`.
5. Si todo está completo y se validan con éxito los pasos anteriores, se deben marcar los puntos como elaborados/completados en el archivo especificado en `TASK_DIR`.

### Ejemplo de Uso
Puedes ejecutar este comando directamente usando la instrucción:
@check_definiton_of_done.md TASK_DIR="MVP-MenbresiaAI/spec/venue-detail/tasks.md"
