package com.mygroup.myapp.contoller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.server.util.HttpClientAddressResolver;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;

@Controller("/api")
public class HelloController {

    @Inject
    HttpClientAddressResolver addressResolver;

    @Get(uri = "/hello/{name}", produces = MediaType.TEXT_PLAIN)
    public String hello(@NotBlank String name) {
        return "Hello " + name;
    }

    @Get(uri = "/bye/{name}", produces = MediaType.TEXT_PLAIN)
    public String bye(@NotBlank String name) {
        return "Hello " + name;
    }
}
