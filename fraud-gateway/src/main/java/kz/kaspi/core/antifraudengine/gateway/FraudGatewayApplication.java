package kz.kaspi.core.antifraudengine.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@SpringBootApplication
@EnableScheduling
public class FraudGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(FraudGatewayApplication.class, args);
    }
}
