package org.hackaton.oracle.churninsight.config;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

    // ============================
    // 1) db_security
    // ============================
    @Primary
    @Bean(name = "securityDataSource")
    @ConfigurationProperties(prefix = "spring.security-datasource")
    public DataSource securityDataSource() {
        return DataSourceBuilder.create().build();
    }

    // ============================
    // 2) db_usuarios
    // ============================
    @Bean(name = "usuariosDataSource")
    @ConfigurationProperties(prefix = "spring.usuarios-datasource")
    public DataSource usuariosDataSource() {
        return DataSourceBuilder.create().build();
    }

    // ============================
    // 2) db_ml_operations
    // ============================
    @Bean(name = "mlOperationsDataSource")
    @ConfigurationProperties(prefix = "spring.mloperations-datasource")
    public DataSource mlOperationsDataSource() {
        return DataSourceBuilder.create().build();
    }
}
