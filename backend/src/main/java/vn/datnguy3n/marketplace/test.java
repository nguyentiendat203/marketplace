package vn.datnguy3n.marketplace;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class test {
    @GetMapping("/test")
    public String getMethodName() {
        return "Hello Spring Boot!";
    }
}
