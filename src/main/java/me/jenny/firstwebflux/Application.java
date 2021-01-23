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

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
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
}
