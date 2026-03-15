package com.retail.checkout.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Item del carrito (SKU y cantidad)")
public record CartItemRequest(
        @NotBlank(message = "sku es requerido")
        @Schema(description = "Código del producto", example = "p-001", requiredMode = Schema.RequiredMode.REQUIRED)
        String sku,

        @Min(value = 1, message = "quantity debe ser al menos 1")
        @Schema(description = "Cantidad", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        int quantity
) {}
