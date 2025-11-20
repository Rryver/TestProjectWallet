package com.kolosov.testprojectwallet.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("app")
@Validated
@Getter
@Setter
@NoArgsConstructor
public class AppProperties {

    /**
     * Максимальное значение, на которое можно уменьшить amount у объекта Wallet за одну операцию
     * NULL - ограничений нет
     */
    private Double maxWithdrawInOneTime;
}
