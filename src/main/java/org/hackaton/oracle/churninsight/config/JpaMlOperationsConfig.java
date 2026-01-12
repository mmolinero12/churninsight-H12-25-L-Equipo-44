package org.hackaton.oracle.churninsight.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.hackaton.oracle.churninsight.domain.ml.repository",
        entityManagerFactoryRef = "mlOperationsEntityManagerFactory",
        transactionManagerRef = "mlOperationsTransactionManager"
)
public class JpaMlOperationsConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean mlOperationsEntityManagerFactory(
            @Qualifier("mlOperationsDataSource") DataSource dataSource,
            JpaProperties jpaProperties) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("org.hackaton.oracle.churninsight.domain.ml.entity");
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(jpaProperties.getProperties());
        factory.setPersistenceUnitName("mlOperationsPU");

        return factory;
    }

    @Bean(name = "mlOperationsTransactionManager")
    public PlatformTransactionManager mlOperationsTransactionManager(
            @Qualifier("mlOperationsEntityManagerFactory")
            EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
