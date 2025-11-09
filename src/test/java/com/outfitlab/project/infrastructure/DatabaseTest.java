package com.outfitlab.project.infrastructure;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class DatabaseTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void entityManagerIsOpen() {
        assertThat(entityManager.isOpen()).isTrue();
    }
}

