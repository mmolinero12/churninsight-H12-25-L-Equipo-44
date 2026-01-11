package org.hackaton.oracle.churninsight.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.hackaton.oracleone.churninsight.domain.auth.repository",
        entityManagerFactoryRef = "securityEntityManagerFactory",
        transactionManagerRef = "securityTransactionManager"
)
public class JpaSecurityConfig {

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean securityEntityManagerFactory(
            @Qualifier("securityDataSource") DataSource dataSource,
            JpaProperties jpaProperties) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("org.hackaton.oracleone.churninsight.domain.auth.entity");
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(jpaProperties.getProperties());
        factory.setPersistenceUnitName("securityPU");

        return factory;
    }

    @Primary
    @Bean
    public PlatformTransactionManager securityTransactionManager(
            @Qualifier("securityEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean factory) {

        return new JpaTransactionManager(
                Objects.requireNonNull(factory.getObject())
        );
    }
}
