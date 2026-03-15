package com.retail.checkout.config;

import com.retail.checkout.controller.CheckoutController;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class OpenApiConfig {

    private static final String TAG_CHECKOUT = "Checkout";
    private static final String TAG_CATALOGO = "Catálogo";

    @Bean
    public OperationCustomizer checkoutApiDocs() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (handlerMethod.getBeanType() != CheckoutController.class) {
                return operation;
            }
            String methodName = handlerMethod.getMethod().getName();
            switch (methodName) {
                case "getProducts" -> {
                    operation.setTags(java.util.List.of(TAG_CATALOGO));
                    operation.setSummary("Listar productos");
                    operation.setDescription("Devuelve todos los productos del catálogo con las promociones asociadas a cada uno.");
                    operation.setResponses(apiResponses(
                            "200", "Lista de productos con sus promociones"));
                }
                case "checkout" -> {
                    operation.setTags(java.util.List.of(TAG_CHECKOUT));
                    operation.setSummary("Procesar checkout");
                    operation.setDescription("Recibe el carrito, aplica promociones y descuento por medio de pago, devuelve desglose y confirmación simulada.");
                    operation.setResponses(apiResponses(
                            "200", "Checkout procesado correctamente",
                            "400", "Request inválido, SKU no encontrado o medio de pago no soportado"));
                }
                default -> { }
            }
            return operation;
        };
    }

    private static ApiResponses apiResponses(String... codeAndDescription) {
        ApiResponses responses = new ApiResponses();
        for (int i = 0; i < codeAndDescription.length; i += 2) {
            ApiResponse r = new ApiResponse();
            r.setDescription(codeAndDescription[i + 1]);
            responses.addApiResponse(codeAndDescription[i], r);
        }
        return responses;
    }
}
