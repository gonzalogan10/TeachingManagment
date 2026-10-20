# Sistema de Gestión de Evaluación Docente - Resumen del Proyecto

## 📋 Descripción General

Sistema web completo para gestionar evaluaciones de estudiantes en equipos de trabajo, permitiendo evaluaciones tanto a nivel de equipo como individuales por miembro. Proporciona una interfaz intuitiva para docentes con análisis de calificaciones y gestión centralizada de criterios.

---

## 🏗️ Arquitectura Técnica

### Backend
- **Tecnología**: Java 17 + Spring Boot 3.5.7
- **Base de Datos**: H2 (en memoria)
- **ORM**: Hibernate + Spring Data JPA
- **API**: RESTful con arquitectura en capas (Controller → Service → Repository)

### Frontend
- **Tecnología**: React 18 + Vite
- **Estilos**: CSS personalizado
- **Comunicación**: Fetch API + Autenticación Basic

### Estructura del Proyecto
```
teaching-management/
├── backend/                 # Spring Boot application
│   ├── src/main/java/      # Código fuente Java
│   ├── pom.xml             # Dependencias Maven
│   └── mvnw                # Maven wrapper
├── frontend/               # Aplicación React
│   ├── src/
│   │   ├── components/     # Componentes React reutilizables
│   │   ├── screen/         # Pantallas principales
│   │   └── assets/         # Imágenes y recursos
│   └── vite.config.js      # Configuración Vite
└── README.md               # Documentación
```

---

## 💡 Características Principales

### 1️⃣ **Evaluación por Criterios**
- Crear criterios de evaluación con configuración flexible
- Asignar criterios a actividades específicas
- Dos tipos de evaluación:
  - **PER_TEAM**: Un mismo valor de puntos para todos los miembros
  - **PER_STUDENT**: Puntos individuales diferenciados por estudiante

### 2️⃣ **Gestión de Equipos**
- Crear y organizar equipos de trabajo
- Asignar estudiantes a equipos
- Ver miembros del equipo y su información

### 3️⃣ **Evaluación Individual vs Grupal**
- Interfaz dinámica que se adapta al tipo de evaluación:
  - **Modo grupal**: Un campo de entrada para todo el equipo
  - **Modo individual**: Campos separados para cada miembro del equipo
- Carga automática de miembros del equipo según el tipo

### 4️⃣ **Filtrado por Actividad**
- Cada evaluación está vinculada a una actividad específica
- Al cambiar de actividad, se muestran SOLO las evaluaciones de esa actividad
- Permite evaluar el mismo criterio-equipo en diferentes actividades con valores distintos

### 5️⃣ **Redistribución de Puntos**
- Opción de permitir redistribuir puntos después de evaluar
- Los criterios con "Sin redistribución" quedan bloqueados una vez evaluados
- Mensajes claros sobre el estado de bloqueo

### 6️⃣ **Cálculo de Totales**
- Cálculo automático de puntos totales por criterio
- Distinción entre puntos del equipo y suma de puntos individuales
- Actualización en tiempo real

---

## 🔄 Flujo de Usuario Principal

### Paso 1: Seleccionar Curso
```
Pantalla → CourseSelector → Obtiene lista de cursos disponibles
```

### Paso 2: Seleccionar Actividad
```
Actividad → ActivitySelector → Carga criterios de esa actividad
```

### Paso 3: Seleccionar Equipo
```
Equipo → TeamSelector → Carga evaluaciones previas de esa actividad
```

### Paso 4: Evaluar Criterios
```
Para cada criterio:
  ├─ Determinar tipo: PER_TEAM vs PER_STUDENT
  ├─ Cargar evaluaciones previas (solo de esta actividad)
  ├─ Mostrar interfaz apropiada
  ├─ Permitir ingreso de puntos
  ├─ Agregar retroalimentación opcional
  └─ Guardar evaluación
```

### Paso 5: Ver Evaluaciones Guardadas
```
EvaluationList → Muestra todas las evaluaciones de la actividad actual
                → Detalles por estudiante y criterio
```

---

## 🗄️ Modelo de Datos

### Entidades Principales

#### **Criterion**
```
- id: Long (PK)
- name: String
- activity: Activity (FK)
- evaluationMethod: ENUM [PER_TEAM, PER_STUDENT]
- pointsRedistribution: Boolean
- maxPoints: Double
```

#### **Assessment** (Evaluación)
```
- id: Long (PK)
- criterion: Criterion (FK)
- teamMembership: TeamMembership (FK)
- activityId: Long (filtrado por actividad)
- points: Double
- feedback: String
- createdAt: LocalDateTime
```

#### **Team**
```
- id: Long (PK)
- name: String
- course: Course (FK)
```

#### **TeamMembership**
```
- team: Team (FK)
- student: Student (FK)
```

#### **Student**
```
- id: Long (PK)
- firstName: String
- lastName: String
- email: String
```

#### **Activity**
```
- id: Long (PK)
- name: String
- course: Course (FK)
- startDate: LocalDate
- endDate: LocalDate
```

---

## 🔌 Endpoints Principales de la API

### Evaluaciones
```
POST   /api/evaluations                                 → Crear evaluación
GET    /api/evaluations/team/{teamId}                   → Obtener evaluaciones del equipo
GET    /api/evaluations/team/{teamId}/activity/{actId}  → Obtener evaluaciones por actividad
GET    /api/evaluations/criterion/{critId}/team/{teamId}/activity/{actId}
                                                        → Verificar si criterio fue evaluado
PUT    /api/evaluations/criterion/{critId}/team/{teamId}→ Actualizar evaluación
DELETE /api/evaluations/{assessmentId}                  → Eliminar evaluación
```

### Criterios
```
GET    /api/criteria                      → Listar todos los criterios
POST   /api/criteria                      → Crear criterio
GET    /api/criteria/{id}                 → Obtener criterio
PUT    /api/criteria/{id}                 → Actualizar criterio
DELETE /api/criteria/{id}                 → Eliminar criterio
```

### Equipos
```
GET    /api/teams                         → Listar equipos
GET    /api/teams/{id}                    → Obtener equipo con miembros
POST   /api/teams                         → Crear equipo
```

### Cursos y Actividades
```
GET    /api/courses                       → Listar cursos
GET    /api/activities                    → Listar actividades
```

---

## 🎯 Características Técnicas Avanzadas

### 1. **Evaluaciones Activity-Aware**
- Cada evaluación está vinculada a una actividad específica
- Consultas con filtrado por actividad a nivel de base de datos
- Evita conflictos entre evaluaciones de diferentes actividades

### 2. **Deserialización JSON Flexible**
- EvaluationDto maneja tanto valores únicos como mapas
- Conversión automática: `Object → Double o Map<Long, Double>`
- Soporta ambos tipos de evaluación con una sola estructura

### 3. **Lazy Loading Optimizado**
- Queries con `LEFT JOIN FETCH` para evitar N+1 problems
- Carga ansiosa de relaciones: Criterion, Student, TeamMembership

### 4. **Validaciones en Cascada**
- Verificación a nivel de servicio
- Validaciones de duplicados específicas por actividad
- Manejo robusto de excepciones

### 5. **Estado Dinámico del Componente**
- Cálculo en tiempo real del estado de bloqueo
- Transiciones suaves entre modos de evaluación
- Manejo de estados de carga y error

---

## 📊 Casos de Uso Principales

### Caso 1: Evaluar Equipo Completo (PER_TEAM)
**Profesor ingresa puntos únicos para todo el equipo**
- Todos los miembros reciben el mismo puntaje
- Un comentario general para el equipo
- Ideal para evaluaciones de desempeño grupal

### Caso 2: Evaluar Contribución Individual (PER_STUDENT)
**Profesor diferencia puntos por miembro**
- Campo de entrada separado para cada estudiante
- Captura contribuciones individuales
- Ideal para evaluar participación dentro del equipo

### Caso 3: Múltiples Evaluaciones por Actividad
**Mismo criterio-equipo evaluado en diferentes actividades**
- Actividad A: Criterio X = 8 puntos
- Actividad B: Criterio X = 7 puntos
- No genera conflictos gracias al filtrado por actividad

### Caso 4: Redistribución de Puntos
**Profesor necesita ajustar puntos después de evaluar**
- Si "Sin redistribución" está activo → Criterio bloqueado
- Si "Permitir redistribución" → Puede editar (endpoint PUT)
- Feedback claro sobre cambios permitidos

---

## 🚀 Mejoras Implementadas en Esta Sesión

### ✅ Per-Student Evaluation
- Interfaz dinámica con inputs individuales por estudiante
- Carga automática de miembros del equipo
- Serialización correcta de puntos individuales

### ✅ Activity-Based Filtering
- Evaluaciones ligadas a actividades específicas
- Campo `activityId` en la entidad Assessment
- Consultas filtradas a nivel de base de datos
- Endpoints con rutas parametrizadas por actividad

### ✅ Activity-Specific Locking
- Verificación de duplicados solo dentro de la misma actividad
- Permite evaluar el mismo criterio en diferentes actividades
- Mensaje de error mejorado indicando la actividad

### ✅ Full Stack Integration
- Backend: Repositories, Services, Controllers con filtrado por actividad
- Frontend: Componentes actualizados para pasar `activityId`
- Estado consistente entre cliente y servidor

---

## 💾 Base de Datos - Datos de Prueba

El sistema viene con datos precargados:
- **Cursos**: Al menos 1 curso disponible
- **Actividades**: Varias actividades por curso
- **Criterios**: Criterios por actividad con diferentes métodos
- **Equipos**: Equipos con estudiantes asignados
- **Evaluaciones**: Ejemplos de evaluaciones para testing

---

## 🔐 Seguridad

- Autenticación Basic: usuario `teacher1` / contraseña `test`
- CORS habilitado para desarrollo
- Validación de entidades a nivel de servicio
- Manejo seguro de excepciones

---

## 🛠️ Stack Tecnológico Completo

| Capa | Tecnología | Versión |
|------|-----------|---------|
| Frontend | React | 18.x |
| Frontend Build | Vite | 5.x |
| Backend | Spring Boot | 3.5.7 |
| Backend Language | Java | 17 |
| ORM | Hibernate/JPA | - |
| Database | H2 | En memoria |
| Build Tool | Maven | 3.x |

---

## 📈 Rendimiento y Escalabilidad

- **Queries optimizadas** con FETCH JOIN
- **Índices implícitos** en IDs de entidades
- **Lazy loading** controlado en relaciones
- **Caché a nivel de cliente** con React
- **Serialización eficiente** de evaluaciones complejas

---

## ✨ Conclusión

Este sistema proporciona una solución completa para evaluaciones académicas con:
- ✅ Flexibilidad: Soporta múltiples tipos de evaluación
- ✅ Organización: Criterios y actividades independientes
- ✅ Precisión: Filtrado granular por actividad
- ✅ Usabilidad: Interfaz intuitiva y responsiva
- ✅ Robustez: Arquitectura escalable y bien estructurada

El proyecto demuestra patrones profesionales de desarrollo full-stack con una clara separación de responsabilidades y una experiencia de usuario fluida.
