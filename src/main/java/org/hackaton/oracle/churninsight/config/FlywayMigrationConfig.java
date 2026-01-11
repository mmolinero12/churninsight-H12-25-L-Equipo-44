package org.hackaton.oracle.churninsight.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayMigrationConfig {

    // ============================
    //   1) PROPIEDADES — SECURITY
    // ============================
    @Value("${flyway.security.locations}")
    private String securityLocations;

    @Value("${flyway.security.table-name}")
    private String securityTable;

    // ============================
    //   2) PROPIEDADES — USUARIOS
    // ============================
    @Value("${flyway.usuarios.locations}")
    private String usuariosLocations;

    @Value("${flyway.usuarios.table-name}")
    private String usuariosTable;

    // ============================
    //   3) PROPIEDADES — ML_OPERATIONS
    // ============================
    @Value("${flyway.mloperations.locations}")
    private String mlOperationsLocations;

    @Value("${flyway.mloperations.table-name}")
    private String mlOperationsTable;



    // ============================
    //   BEAN 1 — DB_SECURITY
    // ============================
    @Bean
    public Flyway flywaySecurity(@Qualifier("securityDataSource") DataSource securityDS) {
        Flyway flyway = Flyway.configure()
                .dataSource(securityDS)
                .locations(securityLocations.split(","))
                .table(securityTable)
                .load();
        flyway.migrate();
        return flyway;
    }

    // ============================
    //   BEAN 2 — DB_USUARIOS
    // ============================
    @Bean
    public Flyway flywayUsuarios(@Qualifier("usuariosDataSource") DataSource usuariosDS) {
        Flyway flyway = Flyway.configure()
                .dataSource(usuariosDS)
                .locations(usuariosLocations.split(","))
                .table(usuariosTable)
                .load();
        flyway.migrate();
        return flyway;
    }

    // ============================
    //   BEAN 3 — DB_ML_OPERATIONS
    // ============================
    @Bean
    public Flyway flywayMlOperations(@Qualifier("mlOperationsDataSource") DataSource mlOperationsDS) {
        Flyway flyway = Flyway.configure()
                .dataSource(mlOperationsDS)
                .locations(mlOperationsLocations.split(","))
                .table(mlOperationsTable)
                .baselineOnMigrate(true)    // Requerido para que se lleve a cabo el punto 9 de application.properties
                .load();
        flyway.migrate();
        return flyway;
    }


}
