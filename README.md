# Checkout Retail

Servicio de checkout para retail: procesa un carrito, aplica promociones y descuentos, y calcula el total final según el medio de pago. API REST con Spring Boot.

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

Para efectos de esta prueba, la API acepta llamadas desde localhost (`http://localhost:5173`, `http://localhost:3000`, `http://localhost:8080`) y desde el frontend desplegado (`https://walmart.farancibiat.cl`). Dejar abierto a localhost permite probar el flujo completo en desarrollo sin restricciones adicionales.

## Definición de archivos del proyecto

### Dominio

- **Product**: Producto del catálogo (SKU, nombre, precio). Usado por el repositorio de catálogo.

### Configuración y carga de datos

- **ProductEntry**: Entrada de producto tal como viene en catalog.json (para deserialización).
- **PromotionConfig**: Configuración de una promoción leída del JSON. Parámetros según el tipo (quantity_discount: minQuantity, percent).
- **CatalogLoader**: Carga el catálogo desde catalog.json al arranque y expone productos y configuraciones de promoción por SKU.

### Repositorio

- **ProductRepository**: Abstracción para obtener productos del catálogo por SKU o todos. Implementación por defecto en memoria (datos demo); permite sustituir por BD sin tocar la lógica de checkout.
- **InMemoryProductRepository**: Implementación del catálogo que delega en CatalogLoader (datos cargados desde catalog.json).

### DTOs (request / response)

- **CartItemRequest**: Item de línea del carrito en el request de checkout.
- **ShippingAddressRequest**: Dirección de envío en el request de checkout.
- **CartCheckoutRequest**: Request del endpoint de checkout. Estructura según cart-example.json.
- **DiscountAppliedDto**: Desglose de un descuento aplicado en la respuesta de checkout.
- **PaymentConfirmationDto**: Confirmación simulada de pago en la respuesta de checkout.
- **CheckoutResponse**: Respuesta del endpoint de checkout: desglose de subtotal, descuentos y total final.
- **PromotionSummaryDto**: Resumen de una promoción asociada a un producto en el catálogo.
- **ProductCatalogEntryDto**: Producto del catálogo con las promociones que le aplican.

### Servicio (checkout y catálogo)

- **LineItemWithPrice**: Línea del carrito con precio unitario y total de línea (para cálculo y promociones).
- **CheckoutContext**: Contexto de checkout para que promociones y medios de pago puedan calcular descuentos.
- **Promotion**: Abstracción para reglas de promoción/descuento por producto o campaña. Permite añadir nuevas promociones sin modificar el núcleo del checkout. El método `getPromotionInfoForSku` devuelve el resumen de la promoción para un SKU dado (si aplica); se usa al listar catálogo.
- **ConfigurablePromotion**: Aplica promociones según la configuración cargada desde catalog.json (por SKU).
- **PaymentMethodHandler**: Abstracción para medios de pago: descuento/recargo y simulación de confirmación. Permite añadir nuevos medios sin modificar el núcleo del checkout. `getDiscountPercentage`: porcentaje de descuento (positivo) o recargo (negativo) sobre el total después de promociones (ej.: 10 = 10% de descuento).
- **DebitPaymentHandler**: Medio de pago débito: 10% de descuento (según enunciado) y confirmación simulada.
