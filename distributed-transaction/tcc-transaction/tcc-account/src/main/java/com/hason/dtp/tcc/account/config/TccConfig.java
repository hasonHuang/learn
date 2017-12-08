package com.hason.dtp.tcc.account.config;

import com.hason.dtp.tcc.account.config.properties.TccDataSourceProperties;
import org.mengyun.tcctransaction.TransactionRepository;
import org.mengyun.tcctransaction.serializer.KryoTransactionSerializer;
import org.mengyun.tcctransaction.serializer.ObjectSerializer;
import org.mengyun.tcctransaction.spring.repository.SpringJdbcTransactionRepository;
import org.mengyun.tcctransaction.spring.support.TccApplicationContext;
import org.mengyun.tcctransaction.support.BeanFactory;
import org.mengyun.tcctransaction.support.BeanFactoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan(basePackageClasses = {BeanFactory.class, TccApplicationContext.class})
public class TccConfig {

    @Autowired
    private TccDataSourceProperties properties;

    @Bean
    public TransactionRepository transactionRepository(
            ObjectSerializer<?> serializer) {

        SpringJdbcTransactionRepository repository = new SpringJdbcTransactionRepository();
        repository.setDataSource(tccDataSource());
        repository.setDomain("ACCOUNT");
        repository.setTbSuffix("_ACCOUNT");
        repository.setSerializer(serializer);
        return repository;
    }

    // 使用 @Bean，避免 JPA 使用了 tcc 的数据源
//    @Bean
    public DataSource tccDataSource() {
        return DataSourceBuilder.create()
                .type(properties.getType())
                .driverClassName(properties.getDriverClassName())
                .url(properties.getUrl())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();
    }

    @Bean
    public ObjectSerializer<?> objectSerializer() {
        return new KryoTransactionSerializer();
    }

    // 源码 TccBeanPostProcessor 无法注入 BeanFactory, 手动配置注入
    @Bean
    public Integer beanFactory(BeanFactory beanFactory) {
        BeanFactoryAdapter.setBeanFactory(beanFactory);
        return 1;
    }

}
