package com.kolosov.testprojectwallet;

import com.kolosov.testprojectwallet.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class TestProjectWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestProjectWalletApplication.class, args);
    }

}
