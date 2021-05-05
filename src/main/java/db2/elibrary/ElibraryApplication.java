package db2.elibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ComponentScan("db2.elibrary.*")
@SpringBootApplication
public class ElibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElibraryApplication.class, args);
    }

}
