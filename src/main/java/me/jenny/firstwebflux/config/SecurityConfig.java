package me.jenny.firstwebflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

//    @Bean
//    public MapReactiveUserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("123456")
//                .roles("USER")
//                .build();
//
//        return new MapReactiveUserDetailsService(user);
//    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges ->
                exchanges
                        .pathMatchers("/assets/**").permitAll()
                        .pathMatchers("/login").permitAll()
                        .anyExchange().authenticated()
        )
                .formLogin().loginPage("/login")
                .and().logout().logoutUrl("/logout")
                .and().httpBasic()
                .and().csrf().disable();
        return http.build();
    }

}
