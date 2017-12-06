package com.hason.dtp.tcc.account.config;

import org.mengyun.tcctransaction.TransactionRepository;
import org.mengyun.tcctransaction.spring.repository.SpringJdbcTransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.sql.DataSource;

/**
 * TCC 事务配置
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/12/5
 */
@Configuration
@ImportResource(locations = "classpath:tcc-transaction.xml")
public class TccConfig {

    @Bean
    public TransactionRepository transactionRepository(
            @Qualifier("tccDataSource") DataSource dataSource) {

        SpringJdbcTransactionRepository repository = new SpringJdbcTransactionRepository();
        repository.setDataSource(dataSource);
        repository.setDomain("ACCOUNT");
        repository.setTbSuffix("_ACCOUNT");
        return repository;
    }

    @ConfigurationProperties(prefix = "spring.datasource.tcc")
    @Bean
    public DataSource tccDataSource() {
        return DataSourceBuilder.create().build();
    }

}
