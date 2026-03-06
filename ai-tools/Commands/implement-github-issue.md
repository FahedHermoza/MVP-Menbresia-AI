---
description: Implementa un issue de GitHub basado en su carpeta de especificaciones y crea el respectivo PR.
---
1. Recibe los parámetros `PR_LINK_ISSUE` y `SPEC_DIR` del usuario. Si no los proporciona en el comando, pídeselos antes de continuar.
2. Utiliza la herramienta de lectura de archivos para leer `plan.md`, `spec.md` y `tasks.md` ubicados en el directorio provisto en `SPEC_DIR`.
3. Revisa y asimila los lineamientos del proyecto ubicados en:
   - [MVP-MenbresiaAI/mobile/AGENTS.md](cci:7://file:///Users/fae/Documents/GitHub-World/GitHub/CursoAIExpert/MVP-Project/MVP-MenbresiaAI/mobile/AGENTS.md:0:0-0:0)
   - [MVP-MenbresiaAI/ai-tools/PROJECT_ARCHITECTURE.md](cci:7://file:///Users/fae/Documents/GitHub-World/GitHub/CursoAIExpert/MVP-Project/MVP-MenbresiaAI/ai-tools/PROJECT_ARCHITECTURE.md:0:0-0:0)
   - [MVP-MenbresiaAI/ai-tools/WORKFLOW_FEATURE.md](cci:7://file:///Users/fae/Documents/GitHub-World/GitHub/CursoAIExpert/MVP-Project/MVP-MenbresiaAI/ai-tools/WORKFLOW_FEATURE.md:0:0-0:0)
   - [MVP-MenbresiaAI/ai-tools/CONTRIBUTING.md](cci:7://file:///Users/fae/Documents/GitHub-World/GitHub/CursoAIExpert/MVP-Project/MVP-MenbresiaAI/ai-tools/CONTRIBUTING.md:0:0-0:0)
4. Si el issue involucra cambios visuales o de UI, revisa las pantallas de referencia disponibles en el directorio `MVP-MenbresiaAI/screens` para asegurarte de que la implementación sea fiel al diseño esperado. Si el issue es puramente lógico/backend, omite este paso.
5. Implementa el requerimiento descrito en el issue `PR_LINK_ISSUE`, asegurándote de completar secuencialmente todas las tareas listadas en el `tasks.md`.
6. Al finalizar toda la implementación, repasa las reglas de empaquetado y PR en:
   - `MVP-MenbresiaAI/ai-tools/Skills/git-commit-formatter.md`
   - `MVP-MenbresiaAI/ai-tools/Skills/git-pr-formatter.md`
7. Crea los commits y el respectivo Pull Request aplicando estrictamente el formato de los documentos del paso anterior.
### Ejemplo de Uso
Puedes ejecutar este workflow directamente desde el chat de Antigravity usando el comando:
@implement-github-issue.md PR_LINK_ISSUE="https://github.com/FahedHermoza/MVP-Menbresia-AI/issues/3" SPEC_DIR="MVP-MenbresiaAI/spec/auth"
