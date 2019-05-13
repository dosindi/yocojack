package com.nyakundid;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by nyakundid
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
public class Application implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(Application.class);

    }

    @Override
    public void run(String... args) throws Exception {


    }

    //TODO 
    /*
        
        1. parse TestCase
		2. rule1	(Over 21)
		3. rule2	(Tie scores)
		4. rule3	(Rank by Suits)
        
     */
}
