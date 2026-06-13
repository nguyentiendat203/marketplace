// package vn.datnguy3n.marketplace.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.domain.AuditorAware;
// import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// import java.util.Optional;

// @Configuration
// @EnableJpaAuditing(auditorAwareRef = "auditorAware")
// public class AuditingConfig {

//     @Bean
//     public AuditorAware<String> auditorAware() {
//         // TODO: replace with SecurityContextHolder principal once Spring Security is wired
//         return () -> Optional.of("system");
//     }
// }
