package me.jenny.firstwebflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

@Configuration
public class RouterFunctionConfig {
    @Bean
    public RouterFunction<? extends ServerResponse> helloRouter() {
        return route(GET("/hello"), request -> ok().body(just("Hello world!"), String.class))
                .andRoute(GET("/bye"), request -> ok().body(just("bye!"), String.class));
    }
}
