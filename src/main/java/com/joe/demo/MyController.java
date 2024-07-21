package com.joe.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MyController {

    @Autowired MyService myService;

    @Value("${my.property2}")
    private String myProperty2;

    @GetMapping("/property")
    public String getMyProperty() {
        return myService.getProperty();
    }

    @GetMapping("/property2")
    public String getProperty2() {
        // Add your logic here
        return myProperty2;
    }
}