package net.javaguides.springboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration.
 *
 * Authentication: IN-MEMORY (single admin user).
 * Credentials are read from environment variables APP_USERNAME and APP_PASSWORD.
 *
 * CSRF: Disabled intentionally for this REST+SPA application.
 *   The frontend uses fetch() with JSON, not HTML forms submitted to the backend.
 *   Re-enable CSRF if the application is ever extended with traditional server-side forms.
 *
 * Session: Default Spring Security session-based authentication is used.
 *   This is appropriate for this application type.
 *
 * Public paths: login.html, style.css, app.js, and images are accessible without login.
 *   Everything else requires authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * APP_USERNAME environment variable — the username for the admin account.
     * Defaults to "admin" if not set (acceptable for development only).
     */
    @Value("${APP_USERNAME:admin}")
    private String appUsername;

    /**
     * APP_PASSWORD environment variable — the raw password for the admin account.
     * There is NO default. If this is not set, the application will fail to start
     * with a clear error. This is intentional — do not set a default password.
     */
    @Value("${APP_PASSWORD}")
    private String appPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates the in-memory user store with credentials from environment variables.
     * The raw password is BCrypt-encoded before being stored — it is never stored in plain text.
     * The password value is intentionally NOT logged.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        log.info("Configuring in-memory authentication for user: {}", appUsername);

        var admin = User.withUsername(appUsername)
                .password(encoder.encode(appPassword))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    /**
     * Defines the security filter chain — which URLs are public, which require login,
     * and how login/logout behave.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF disabled: this is a REST API consumed by the same-origin SPA frontend.
            // There are no cross-origin state-changing requests.
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // Allow public access to the login page and static assets required to render it
                .requestMatchers("/login.html", "/style.css", "/app.js", "/images/**", "/login").permitAll()
                // All other requests require the user to be logged in
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login.html")          // Custom login page
                .loginProcessingUrl("/login")      // Spring Security processes POST to this URL
                .defaultSuccessUrl("/index.html", true)  // Redirect here after successful login
                .failureUrl("/login.html?error=true")    // Redirect here on failure
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .invalidateHttpSession(true)       // Destroy session on logout
                .deleteCookies("JSESSIONID")       // Remove session cookie
                .permitAll()
            );

        return http.build();
    }
}