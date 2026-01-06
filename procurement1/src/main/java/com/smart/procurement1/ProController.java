package com.smart.procurement1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProController {
    @GetMapping("/hello")
    String sayHelloWorld()
    {
        return "HelloWord";
    }
}
