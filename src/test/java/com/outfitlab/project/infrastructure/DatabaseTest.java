package com.outfitlab.project.infrastructure;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
public class DatabaseTest {

    @Test
    void databaseIsReachable() throws Exception {
        DataSource dataSource = DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5433/outfitlab")
                .username("outfitlab")
                .password("outfitlab123")
                .driverClassName("org.postgresql.Driver")
                .build();

        boolean connected = false;
        int retries = 10; // hasta 10 intentos
        for (int i = 1; i <= retries; i++) {
            try (Connection conn = dataSource.getConnection()) {
                connected = conn.isValid(2);
                if (connected) {
                    System.out.println("✅ Conectado a la base de datos en intento " + i);
                    break;
                }
            } catch (SQLException e) {
                System.out.println("⏳ Esperando que la BDD esté lista... intento " + i);
                TimeUnit.SECONDS.sleep(3);
            }
        }

        assertThat(connected)
                .as("La base de datos PostgreSQL no respondió tras varios intentos")
                .isTrue();
    }
}

