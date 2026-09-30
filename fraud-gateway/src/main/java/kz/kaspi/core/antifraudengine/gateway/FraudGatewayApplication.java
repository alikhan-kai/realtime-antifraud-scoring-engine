package kz.kaspi.core.antifraudengine.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FraudGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(FraudGatewayApplication.class, args);
    }
}
