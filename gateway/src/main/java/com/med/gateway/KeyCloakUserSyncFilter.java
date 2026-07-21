package com.med.gateway;

import com.med.gateway.user.RegisterRequest;
import com.med.gateway.user.UserValidationService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeyCloakUserSyncFilter implements WebFilter {

    private final UserValidationService userValidationService;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        String authHeader = serverWebExchange.getRequest().getHeaders().getFirst("Authorization");

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return webFilterChain.filter(serverWebExchange);
        }

        RegisterRequest registerRequest = getUserDetails(authHeader);
        if (registerRequest == null || !StringUtils.hasText(registerRequest.getKeycloakId())) {
            return webFilterChain.filter(serverWebExchange);
        }

        String keycloakId = registerRequest.getKeycloakId();

        return userValidationService.validateUser(String.valueOf(keycloakId))
                .flatMap(exists -> {
                    if (!exists) {
                        log.info("New Keycloak user detected. Registering user with ID: {}", keycloakId);
                        return userValidationService.registerUser(registerRequest);
                    } else {
                        log.info("User already exists with Keycloak ID: {}", keycloakId);
                        return Mono.empty();
                    }
                })
                .then(Mono.defer(() -> {
                    ServerHttpRequest request = serverWebExchange.getRequest().mutate()
                            .header("X-User-ID", keycloakId)
                            .build();
                    return webFilterChain.filter(serverWebExchange.mutate().request(request).build());
                }));
    }

    private RegisterRequest getUserDetails(String token) {
        try {
            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(claimsSet.getStringClaim("email"));
            registerRequest.setKeycloakId(claimsSet.getStringClaim("sub"));
            registerRequest.setFirstName(claimsSet.getStringClaim("given_name"));
            registerRequest.setLastName(claimsSet.getStringClaim("family_name"));
            registerRequest.setPassword("dummy@123123");

            return registerRequest;
        } catch (Exception e) {
            log.error("Failed to parse JWT token: {}", e.getMessage());
            return null;
        }
    }
}