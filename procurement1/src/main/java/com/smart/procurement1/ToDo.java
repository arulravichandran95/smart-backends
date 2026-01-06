package com.smart.procurement1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToDo
{
    @GetMapping("/")
    String getToDo()
    {
        return "toDo";
    }

}
