package me.jenny.firstwebflux.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

@Slf4j
@Configuration
public class RouterFunctionConfig {
    @Bean
    public RouterFunction<? extends ServerResponse> helloRouter() {
        return route(GET("/hello"), request -> ok().body(just("Hello world!"), String.class))
                .andRoute(GET("/bye"), request -> ok().body(just("GET bye!"), String.class))
                .andRoute(POST("/bye"), this::doFunc)
                .andRoute(PUT("/bye"), request -> doPutFunc(request))
                .andRoute(DELETE("/bye/{id}"), request -> doDeleteFunc(request));
    }

    private Mono<ServerResponse> doDeleteFunc(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(String.class).flatMap(body -> {
            log.info("delete {} reqBody: {}", id, body);
            return ok().build();
        });
    }

    private Mono<ServerResponse> doPutFunc(ServerRequest request) {
        return request.bodyToMono(String.class).flatMap(body -> {
            log.info("put reqBody: {}", body);
            return ok().build();
        });
    }

    public Mono<ServerResponse> doFunc(ServerRequest request) {
        return request.bodyToMono(String.class).flatMap(body -> {
            log.info("post reqBody: {}", body);
            return ok().body(just("POST bye!"), String.class);
        });
    }
}
