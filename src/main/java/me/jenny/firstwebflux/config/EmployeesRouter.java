package me.jenny.firstwebflux.config;

import me.jenny.firstwebflux.handler.EmployeesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static me.jenny.firstwebflux.constants.EmployeeConstants.EMPLOYEE_FUNCTIONAL_END_POINT_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class EmployeesRouter {

    /*@Bean
    public RouterFunction<ServerResponse> employeesRoute(EmployeesHandler employeesHandler) {
        return RouterFunctions
                .route(GET(EMPLOYEE_FUNCTIONAL_END_POINT_V1).and(accept(APPLICATION_JSON)),
                        employeesHandler::getAllEmployees)
                .andRoute(GET(EMPLOYEE_FUNCTIONAL_END_POINT_V1 + "/{id}").and(accept(APPLICATION_JSON)),
                        employeesHandler::getOneEmployee)
                .andRoute(POST(EMPLOYEE_FUNCTIONAL_END_POINT_V1).and(accept(APPLICATION_JSON)),
                        employeesHandler::createEmployee)
                .andRoute(DELETE(EMPLOYEE_FUNCTIONAL_END_POINT_V1 + "/{id}").and(accept(APPLICATION_JSON)),
                        employeesHandler::deleteEmployee)
                .andRoute(PUT(EMPLOYEE_FUNCTIONAL_END_POINT_V1 + "/{id}").and(accept(APPLICATION_JSON)),
                        employeesHandler::updateEmployee);
    }*/
}