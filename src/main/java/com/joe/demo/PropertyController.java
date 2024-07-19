package com.joe.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PropertyController {

    @Value("${my.property}")
    private String myProperty;

    @GetMapping("/property")
    public String getMyProperty() {
        return myProperty;
    }
}