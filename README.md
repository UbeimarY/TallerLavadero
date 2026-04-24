# Sistema de Gestión — Centro de Lavado de Vehículos

**Universidad Cooperativa de Colombia — Campus Pasto**  
**Asignatura:** Programación Orientada a Objetos  
**Estudiante:** Ubeimar Yepes  

---

## Descripción

Sistema de gestión para un centro de lavado de vehículos desarrollado como API REST con Java y Spring Boot. Permite digitalizar la operación diaria del negocio: registro de clientes y vehículos, creación de órdenes de servicio, control de estados y registro de pagos.

---

## Tecnologías utilizadas

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.2.5 | Framework base |
| Spring Data JPA | 3.2.5 | Persistencia y acceso a datos |
| Spring Validation | 3.2.5 | Validación de entradas |
| PostgreSQL | 16 | Base de datos relacional |
| Flyway | 10.10.0 | Migraciones y versionado del esquema |
| Lombok | 1.18.32 | Reducción de código repetitivo |
| SpringDoc OpenAPI | 2.5.0 | Documentación automática de la API |
| HikariCP | 5.0.1 | Pool de conexiones |
| Maven | 3.9+ | Gestión de dependencias y build |

---

## Arquitectura del proyecto

El proyecto aplica una arquitectura en capas siguiendo principios de POO como encapsulamiento, herencia, polimorfismo y abstracción.

```
src/main/java/com/lavado/api/
│
├── controller/       # Capa de presentación — recibe peticiones HTTP REST
├── service/          # Capa de negocio — interfaces e implementaciones
│   └── impl/
├── repository/       # Capa de acceso a datos — Spring Data JPA
├── entity/           # Entidades del dominio — mapeadas a tablas de BD
├── dto/
│   ├── request/      # Objetos de entrada con validaciones
│   └── response/     # Objetos de salida hacia el cliente
├── enums/            # Enumeraciones del dominio
├── exception/        # Manejo centralizado de errores
└── config/           # Configuración de OpenAPI
```

---

## Modelo de dominio

| Entidad | Descripción |
|---|---|
| `Cliente` | Datos del propietario del vehículo |
| `Vehiculo` | Vehículo registrado, asociado a un cliente |
| `Servicio` | Catálogo de servicios ofrecidos |
| `PrecioServicio` | Precio de un servicio según tipo de vehículo |
| `OrdenServicio` | Orden de atención con ciclo de vida completo |
| `DetalleOrden` | Línea de servicio dentro de una orden |
| `Pago` | Registro de pagos asociados a una orden |

---

## Decisiones de diseño

**1. Precios flexibles por tipo de vehículo**  
La entidad `PrecioServicio` permite definir tarifas distintas según el tipo de vehículo (moto, auto, camioneta, etc.). Si no existe precio específico, se usa el `precioBase` del servicio. El método `calcularPrecio(TipoVehiculo)` encapsula esta lógica dentro de la entidad `Servicio`.

**2. Máquina de estados en OrdenServicio**  
Las transiciones de estado están controladas con un `switch` expresivo dentro de la entidad, garantizando que solo se permitan cambios válidos:
```
REGISTRADA → EN_PROCESO | CANCELADA
EN_PROCESO → FINALIZADA | CANCELADA
FINALIZADA → ENTREGADA
```
Cualquier transición inválida lanza una `BusinessException`.

**3. Precio capturado al momento de la orden**  
El `precioUnitario` se registra en `DetalleOrden` al crear la orden, garantizando que cambios futuros en el catálogo no afecten órdenes ya generadas.

**4. Baja lógica de servicios**  
Los servicios se desactivan (`activo = false`) en lugar de eliminarse físicamente, preservando la integridad histórica de las órdenes.

**5. Migraciones versionadas con Flyway**  
El esquema de base de datos es gestionado por Flyway, garantizando reproducibilidad y trazabilidad de cambios:
- `V1__crear_esquema_inicial.sql` — crea tablas, constraints e índices
- `V2__datos_iniciales.sql` — inserta catálogo de servicios y datos de demo

---

## Requisitos previos

- Java 17 o superior
- Maven 3.9 o superior
- PostgreSQL corriendo con los siguientes datos:

| Parámetro | Valor |
|---|---|
| Host | localhost |
| Puerto | 5432 |
| Base de datos | lavadero_db |
| Usuario | postgres |
| Contraseña | postgres |

---

## Ejecución

```bash
# Desde la raíz del proyecto
mvn spring-boot:run
```

Al iniciar, Flyway ejecuta automáticamente las migraciones y crea toda la estructura de base de datos si no existe.

---

## Documentación de la API

Una vez en ejecución, la documentación interactiva está disponible en:

```
http://localhost:8080/swagger-ui.html
```

---

## Endpoints disponibles

### Clientes — `/api/v1/clientes`
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/` | Registrar cliente |
| GET | `/` | Listar clientes |
| GET | `/{id}` | Obtener cliente por ID |
| PUT | `/{id}` | Actualizar cliente |
| DELETE | `/{id}` | Eliminar cliente |

### Vehículos — `/api/v1/vehiculos`
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/` | Registrar vehículo |
| GET | `/` | Listar vehículos |
| GET | `/{id}` | Obtener vehículo por ID |
| GET | `/cliente/{clienteId}` | Vehículos de un cliente |
| PUT | `/{id}` | Actualizar vehículo |

### Servicios — `/api/v1/servicios`
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/` | Crear servicio |
| GET | `/` | Listar todos los servicios |
| GET | `/activos` | Listar servicios activos |
| GET | `/{id}` | Obtener servicio por ID |
| PUT | `/{id}` | Actualizar servicio |
| DELETE | `/{id}` | Desactivar servicio (baja lógica) |

### Órdenes de Servicio — `/api/v1/ordenes`
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/` | Crear orden con servicios |
| GET | `/` | Listar órdenes (filtrar con `?estado=`) |
| GET | `/{id}` | Obtener orden por ID |
| GET | `/{id}/detalle` | Detalle completo con servicios y pagos |
| GET | `/cliente/{clienteId}` | Órdenes de un cliente |
| PATCH | `/{id}/estado` | Cambiar estado de la orden |

### Pagos
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/ordenes/{id}/pagos` | Registrar pago |
| GET | `/api/v1/ordenes/{id}/pagos` | Pagos de una orden |
| GET | `/api/v1/pagos/{id}` | Obtener pago por ID |
| PATCH | `/api/v1/pagos/{id}/anular` | Anular pago |

---

## Flujo de uso típico

```
1. Registrar cliente
2. Registrar vehículo asociado al cliente
3. Consultar servicios disponibles
4. Crear orden de servicio con los servicios requeridos
5. Cambiar estado a EN_PROCESO cuando inicia la atención
6. Cambiar estado a FINALIZADA al terminar el trabajo
7. Registrar el pago
8. Cambiar estado a ENTREGADA al devolver el vehículo
```

---

## Datos de demo precargados

La migración V2 inserta automáticamente datos para pruebas:

- **3 clientes** con datos completos
- **4 vehículos** de diferentes tipos
- **5 servicios** con precios diferenciados por tipo de vehículo:
  - Lavado Básico — desde $15.000 (moto) hasta $100.000 (camión)
  - Lavado Premium — desde $25.000 (moto) hasta $180.000 (camión)
  - Polichado — $120.000 precio fijo
  - Desinfección Interna — $50.000 precio fijo
  - Cambio de Aceite — $30.000 precio fijo
- **2 órdenes de ejemplo**: una entregada con pago registrado y una en proceso
