# Microservicios Productos & Inventario

Este proyecto implementa dos microservicios independientes:
- **productos-service**: Gestión de productos
- **inventario-service**: Gestión de inventario y compras

Ambos servicios están desarrollados en Java 17 con Spring Boot y utilizan PostgreSQL como base de datos.

---

## Estructura del proyecto

```
├── docker-compose.yml
├── productos-service/
│   ├── src/
│   ├── pom.xml
│   └── ...
└── inventario-service/
    ├── src/
    ├── pom.xml
    └── ...
```

---

## Requisitos previos
- Docker y Docker Compose
- Java 17
- Maven 3.8+

---

## Levantar los servicios con Docker Compose

1. Ejecuta en la raíz del proyecto:
   ```bash
   docker-compose up
   ```
   Esto levantará dos bases de datos PostgreSQL:
   - productos-db (puerto 5432, base: productos_db)
   - inventario-db (puerto 5433, base: inventario_db)

2. En terminales separadas, inicia cada microservicio:
   ```bash
   cd productos-service
   ./mvnw spring-boot:run
   # o en Windows: mvnw.cmd spring-boot:run
   ```
   ```bash
   cd inventario-service
   ./mvnw spring-boot:run
   # o en Windows: mvnw.cmd spring-boot:run
   ```

---

## Configuración por defecto

- **productos-service**
  - Puerto: 8082
  - Base de datos: productos_db (localhost:5432)
- **inventario-service**
  - Puerto: 8081
  - Base de datos: inventario_db (localhost:5433)
  - Se comunica con productos-service en `http://localhost:8082`

---

## Endpoints principales

### productos-service

- `POST   /api/productos` — Crear producto
- `GET    /api/productos/{id}` — Obtener producto por ID
- `GET    /api/productos` — Listar productos paginados

#### Ejemplo de creación de producto
```json
{
  "data": {
    "type": "productos",
    "attributes": {
      "nombre": "Camiseta",
      "precio": 19.99,
      "descripcion": "Camiseta de algodón"
    }
  }
}
```

### inventario-service

- `GET    /api/inventario/{productoId}` — Consultar inventario de un producto
- `PUT    /api/inventario/{productoId}` — Actualizar cantidad de inventario
- `POST   /api/inventario/compras` — Realizar compra (descontar stock)

#### Ejemplo de actualización de inventario
```json
{
  "data": {
    "type": "inventario",
    "attributes": {
      "cantidad": 50
    }
  }
}
```

#### Ejemplo de compra
```json
{
  "data": {
    "type": "compras",
    "attributes": {
      "productoId": 2,
      "cantidad": 3
    }
  }
}
```

---

## Pruebas con Postman

1. **Crear producto**
   - Método: POST
   - URL: `http://localhost:8082/api/productos`
   - Body: (ver ejemplo arriba)

2. **Actualizar inventario**
   - Método: PUT
   - URL: `http://localhost:8081/api/inventario/2`
   - Body: (ver ejemplo arriba)

3. **Realizar compra**
   - Método: POST
   - URL: `http://localhost:8081/api/inventario/compras`
   - Body: (ver ejemplo arriba)

---

## Notas
- Si reinicias los servicios, los datos pueden perderse (por configuración `ddl-auto: create-drop`). Cambia a `update` si deseas persistencia.
- Asegúrate de crear productos antes de operar sobre inventario.
- Puedes consultar la documentación Swagger en:
  - `http://localhost:8082/swagger-ui.html` (productos)
  - `http://localhost:8081/swagger-ui.html` (inventario)

---

## Créditos y contacto

Proyecto de ejemplo para microservicios. 
Contacto: [Tu Nombre/Email] 