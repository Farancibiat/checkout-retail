package com.retail.checkout.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Carrito y datos para procesar el checkout")
public record CartCheckoutRequest(
        @NotBlank(message = "cartId es requerido")
        @Schema(description = "Identificador del carrito", example = "cart-1001", requiredMode = Schema.RequiredMode.REQUIRED)
        String cartId,

        @NotEmpty(message = "items no puede estar vacío")
        @Valid
        @Schema(description = "Items del carrito (SKU y cantidad)")
        List<CartItemRequest> items,

        @NotNull(message = "shippingAddress es requerido")
        @Valid
        @Schema(description = "Dirección de envío")
        ShippingAddressRequest shippingAddress,

        @NotBlank(message = "paymentMethod es requerido")
        @Schema(description = "Medio de pago (soportado: DEBITO)", example = "DEBITO", requiredMode = Schema.RequiredMode.REQUIRED)
        String paymentMethod
) {}
