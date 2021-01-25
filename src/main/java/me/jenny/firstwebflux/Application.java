package me.jenny.firstwebflux;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import me.jenny.firstwebflux.domain.Customer;
import me.jenny.firstwebflux.domain.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public WebClient webClient() {
        return WebClient.create("http://127.0.0.1:8080");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // For test springframework.data.r2dbc
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * R2DBC 와 MySQL 을 사용하는 부분은 아직 지양해야 할 것 같다.
     * MySQL의 경우, Pivotal 팀 공식 드라이버는 없는듯
     * <ul>
     * <li> Postgres (io.r2dbc:r2dbc-postgresql)
     * <li> H2 (io.r2dbc:r2dbc-h2)
     * <li> Microsoft SQL Server (io.r2dbc:r2dbc-mssql)
     * <li> MySQL (com.github.mirromutth:r2dbc-mysql)
     * <li> jasync-sql MySQL (com.github.jasync-sql:jasync-r2dbc-mysql)
     * </ul>
     *
     * @param connectionFactory H2ConnectionConfiguration{password='REDACTED', properties='{}',
     *                          url='mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE', username='sa'}
     * @return ConnectionFactoryInitializer
     */
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        // spring.datasource.url=jdbc:h2:mem:testdb
        // spring.datasource.driverClassName=org.h2.Driver
        // spring.datasource.username=sa
        // spring.datasource.password=REDACTED
        // spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

        return initializer;
    }

    @Bean
    public CommandLineRunner demo(CustomerRepository repository) {

        return (args) -> {
            // save a few customers
            repository.saveAll(Arrays.asList(new Customer("Jack", "Bauer"),
                    new Customer("Chloe", "O'Brian"),
                    new Customer("Kim", "Bauer"),
                    new Customer("David", "Palmer"),
                    new Customer("Michelle", "Dessler")))
                    .blockLast(Duration.ofSeconds(10));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            repository.findAll().doOnNext(customer -> {
                log.info(customer.toString());
            }).blockLast(Duration.ofSeconds(10));

            log.info("");

            // fetch an individual customer by ID
            repository.findById(1L).doOnNext(customer -> {
                log.info("Customer found with findById(1L):");
                log.info("--------------------------------");
                log.info(customer.toString());
                log.info("");
            }).block(Duration.ofSeconds(10));


            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Bauer").doOnNext(bauer -> {
                log.info(bauer.toString());
            }).blockLast(Duration.ofSeconds(10));

            log.info("");

        };
    }

    /**
     * WebClient Usage GET
     *
     * @see <a href="https://github.com/arungangadharan/myhobbies/blob/968e3c8c04c83a573b89a86296bf4f17a8a6413c/DemoApplication.java">Github example</a>
     */
    @Bean
    public CommandLineRunner commandLineRunnerGET(WebClient webClient) {
        return (args) -> {
            Mono<String> mono = webClient
                    .get()
                    .uri("/bye")
                    .retrieve()
                    .bodyToMono(String.class);

            // Synchronous
            String result = mono.block();
            System.out.println("result: " + result);

            // Asynchronous
            mono.timeout(Duration.ofNanos(1))
                    .subscribe(
                            body -> System.out.println("Response is " + body),
                            e -> System.out.println("timeout!"));
        };
    }

    @Bean
    public CommandLineRunner commandLineRunnerPOST(WebClient webClient) {
        Mono<String> data = Mono.just("mono data!");

        return (args) -> {
            Mono<String> mono = webClient
                    .post()
                    .uri("/bye")
                    .body(data, String.class)
                    .retrieve()
                    .bodyToMono(String.class);

            mono.subscribe(body -> System.out.println("Response is " + body));
        };
    }

    @Bean
    public CommandLineRunner commandLineRunnerPUT(WebClient webClient) {
        return (args) -> {
            Mono<String> monoData = Mono.just("put body");
            Mono<Void> mono = webClient
                    .put()
                    .uri("/bye")
                    .body(monoData, String.class)
                    .retrieve()
                    .onStatus(status -> status == HttpStatus.NOT_FOUND,
                            response -> Mono.just(new UnknownError("custom error")))
                    .bodyToMono(Void.class);

            mono.subscribe(body -> System.out.println("Response is " + body));
        };
    }

    @Bean
    public CommandLineRunner commandLineRunnerDELETE(WebClient webClient) {
        return (args) -> {
            Mono<Void> mono = webClient
                    .delete()
                    .uri("/bye/{id}", 1L)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new UnknownError("custom error")))
                    .bodyToMono(Void.class);

            mono.subscribe(body -> System.out.println("Response is " + body));
        };
    }

}
