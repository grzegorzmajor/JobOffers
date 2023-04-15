package ovh.major.joboffers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import ovh.major.joboffers.infrastructure.security.jwt.JwtConfigurationProperties;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
@EnableConfigurationProperties(value = {JwtConfigurationProperties.class})
public class JobOffersSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobOffersSpringBootApplication.class, args);
    }
}