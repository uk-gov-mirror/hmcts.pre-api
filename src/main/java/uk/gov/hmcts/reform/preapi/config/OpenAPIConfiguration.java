package uk.gov.hmcts.reform.preapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    public static final String X_USER_ID_HEADER = "X-User-Id";
    public static final String APIM_SUBSCRIPTION_KEY_HEADER = "Ocp-Apim-Subscription-Key";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info().title("PRE API")
                      .description("PRE API - Used for managing courts, bookings, recordings and permissions.")
                      .version("v0.0.1")
                      .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
            .addSecurityItem(new SecurityRequirement().addList(APIM_SUBSCRIPTION_KEY_HEADER).addList(X_USER_ID_HEADER))
            .components(
                new Components()
                    .addSecuritySchemes(APIM_SUBSCRIPTION_KEY_HEADER,
                                        new SecurityScheme()
                                            .type(SecurityScheme.Type.APIKEY)
                                            .in(SecurityScheme.In.HEADER)
                                            .name(APIM_SUBSCRIPTION_KEY_HEADER))
                    .addSecuritySchemes(X_USER_ID_HEADER,
                                        new SecurityScheme()
                                            .type(SecurityScheme.Type.APIKEY)
                                            .in(SecurityScheme.In.HEADER)
                                            .name(X_USER_ID_HEADER)))
            .externalDocs(new ExternalDocumentation().description("README").url("https://github.com/hmcts/pre-api"));
    }
}
