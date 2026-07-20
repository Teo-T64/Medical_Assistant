package com.med.gateway;

import com.med.gateway.user.RegisterRequest;
import com.med.gateway.user.UserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeyCloakUserSyncFilter implements WebFilter {
    private UserValidationService userValidationService;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        Long userId = Long.valueOf(serverWebExchange.getRequest().getHeaders().getFirst("X-User-ID"));
        String token = serverWebExchange.getRequest().getHeaders().getFirst("Authorization");

        if(userId != null && token != null) {
            return userValidationService.validateUser(userId)
                    .flatMap(exist->{
                        if(!exist) {
                            //Register user
                            RegisterRequest  registerRequest = new RegisterRequest();

                            return Mono.empty();
                        }else{
                            log.info("User already exists");
                            return Mono.empty();
                        }
                    }).then(Mono.defer(()->{
                        ServerHttpRequest request = serverWebExchange.getRequest().mutate()
                                .header("X-User-ID", String.valueOf(userId))
                                .build();
                        return webFilterChain.filter(serverWebExchange.mutate().request(request).build());
                    }));
        }

    }

}
