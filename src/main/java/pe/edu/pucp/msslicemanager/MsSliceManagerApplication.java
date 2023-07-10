package pe.edu.pucp.msslicemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MsSliceManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSliceManagerApplication.class, args);
    }

}
