package com.example.springbootexample;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/*
curl -H "Accept: application/json" -H "Content-type: application/json" \
-X POST -d '{"name":"Corey", "type":"Volume Create"}' http://localhost:8080/api/process
 */

@RestController
public class ProcessController {
    @RequestMapping(
            value = "/api/process",
            method = RequestMethod.POST,
            consumes="application/json")
    public void process(@RequestBody Map<String, Object> payload)
            throws Exception {
        System.out.println(payload);
    }
}
