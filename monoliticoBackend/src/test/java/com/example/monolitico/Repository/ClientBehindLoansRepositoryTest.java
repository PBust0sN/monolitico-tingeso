package com.example.monolitico.Repository;

import com.example.monolitico.Entities.ClientBehindLoansEntity;
import com.example.monolitico.Repositories.ClientBehindLoansRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClientBehindLoansRepositoryTest {

    @Autowired
    private ClientBehindLoansRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindLoansByClientIdBehind() {
        // Arrange
        ClientBehindLoansEntity e1 = new ClientBehindLoansEntity(null, 10L, 1L);
        ClientBehindLoansEntity e2 = new ClientBehindLoansEntity(null, 20L, 1L);
        ClientBehindLoansEntity e3 = new ClientBehindLoansEntity(null, 30L, 2L);

        entityManager.persist(e1);
        entityManager.persist(e2);
        entityManager.persist(e3);
        entityManager.flush(); // fuerza sincronización con la BD

        // Act
        List<Long> result = repository.findLoansByClientIdBehind(1L);

        // Assert
        assertThat(result)
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(10L, 20L);
    }

    @Test
    void testFindLoansByClientIdBehind_NoResults() {
        // Arrange
        ClientBehindLoansEntity record1 = new ClientBehindLoansEntity(null, 201L, 3L);
        ClientBehindLoansEntity record2 = new ClientBehindLoansEntity(null, 202L, 4L);

        entityManager.persist(record1);
        entityManager.persist(record2);
        entityManager.flush();

        // Act
        List<Long> loans = repository.findLoansByClientIdBehind(99L);

        // Assert
        assertThat(loans).isEmpty();
    }
}
