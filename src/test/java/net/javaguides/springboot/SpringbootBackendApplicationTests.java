package net.javaguides.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Smoke test — verifies the Spring application context loads without errors.
 *
 * Uses test-specific properties to avoid requiring a live PostgreSQL database.
 * The in-memory H2 database is NOT used here because we have PostgreSQL-specific
 * dialect configured. Instead, we override the datasource to use the Spring
 * auto-configuration "none" DDL mode and skip the real DB connection in unit test scope.
 *
 * If you want full integration tests with a real DB, run the application manually
 * and test via the browser/Postman.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "DB_PASSWORD=testpassword",
        "APP_PASSWORD=testpassword",
        "APP_USERNAME=testadmin"
})
class SpringbootBackendApplicationTests {

    @Test
    void contextLoads() {
        // If this test passes, the Spring context started successfully.
        // All beans were wired correctly, security config was applied, etc.
    }
}
