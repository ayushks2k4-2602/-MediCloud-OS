package com.saas.platform.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";
    private static final String TENANT_HEADER_NAME = "X-Tenant-ID";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enterprise Multi-Tenant SaaS Platform API")
                        .description("Production-Grade Scalable Multi-Tenant RESTful APIs with Clean Architecture, JWT Authentication, Subscriptions, CRM, and RBAC.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SaaS Architecture Team")
                                .email("support@saasplatform.com"))
                        .license(new License().name("Apache 2.0")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        .addParameters(TENANT_HEADER_NAME,
                                new HeaderParameter()
                                        .name(TENANT_HEADER_NAME)
                                        .description("Multi-Tenant Context Identifier (Tenant UUID)")
                                        .required(false)
                                        .schema(new io.swagger.v3.oas.models.media.StringSchema())));
    }
}
