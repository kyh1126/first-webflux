package me.jenny.firstwebflux.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

//@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RouterFunctionConfigTest {

    @Autowired
    WebTestClient testClient;

    @Test
    void nameRouterFunction() {
        testClient.get().uri("/bye")
                .accept(MediaType.TEXT_PLAIN).exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(it -> Assertions.assertThat(it.getResponseBody()).isEqualTo("bye!"));
    }
}
