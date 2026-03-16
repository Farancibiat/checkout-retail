# Checkout Retail

Servicio de checkout para retail: procesa un carrito, aplica promociones y descuentos, y calcula el total final según el medio de pago. API REST con Spring Boot.

## API desplegada (Railway)

La API está desplegada en Railway. Puedes probar los endpoints sin ejecutar el backend en local.

| Enlace | Descripción |
|--------|-------------|
| **Base URL** | [https://checkout-retail-production.up.railway.app](https://checkout-retail-production.up.railway.app) |
| **Swagger UI** | [https://checkout-retail-production.up.railway.app/swagger-ui/index.html](https://checkout-retail-production.up.railway.app/swagger-ui/index.html) — documentación interactiva para probar catálogo y checkout desde el navegador. |

## Requisitos

- **Java:** compatible con versiones **17 a 25** (mínimo 17 por Spring Boot 3.x; el proyecto está configurado con 25 en `pom.xml`). Cualquier versión en ese rango permite compilar y ejecutar.
- **Maven:** hace falta para compilar y ejecutar. Se recomienda 3.x en adelante.

## Cómo ejecutar

1. Clonar el repositorio y ubicarse en la carpeta del proyecto:
   ```bash
   cd checkout-retail
   ```

2. Compilar:
   ```bash
   mvn clean compile
   ```

3. Ejecutar la aplicación:
   ```bash
   mvn spring-boot:run
   ```
   La aplicación queda disponible en **http://localhost:8080**.

4. Documentación e interacción con la API (con la aplicación en ejecución):
   - **OpenAPI (Swagger UI):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) — ver endpoints, esquemas de request/response y probar el checkout desde el navegador. (Si no carga, probar [swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).)
   - **Spec OpenAPI (JSON):** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) — documento OpenAPI 3 en bruto.

## CORS

Por defectla API local acepta llamadas desde localhost (`http://localhost:5173`, `http://localhost:3000`, `http://localhost:8080`). Para cargar en un entorno, debes utilizar el archivo .env.example para guiarte al ingresar las variebles de entorno.

