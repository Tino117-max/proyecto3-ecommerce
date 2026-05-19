package com.proyecto3.ecommerce.config;

import com.proyecto3.ecommerce.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Configuracion central de Spring Security.
 *
 * Rutas publicas:  /, /catalogo, /registro, /css/**, /js/**, /img/**
 * Rutas ADMIN:     /admin/**
 * Rutas CLIENTE:   /cliente/**
 * Login:           /login (GET muestra formulario, POST procesa)
 * Logout:          /logout
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * BCrypt con strength 10 para cifrar contrasenas.
     * Nunca se almacena la contrasena en texto plano.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Filtro principal de seguridad.
     * Define que rutas requieren autenticacion y con que rol.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Autorizacion de rutas
            .authorizeHttpRequests(auth -> auth
                // Recursos estaticos y paginas publicas
                .requestMatchers("/", "/catalogo", "/registro", "/login",
                                 "/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
                // Solo ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Clientes autenticados
                .requestMatchers("/cliente/**").hasAnyRole("CLIENTE", "ADMIN")
                // Todo lo demas requiere autenticacion
                .anyRequest().authenticated()
            )

            // Configuracion del formulario de login
            .formLogin(form -> form
                .loginPage("/login")                        // URL del formulario personalizado
                .loginProcessingUrl("/login")               // URL que procesa POST del form
                .usernameParameter("email")                 // Nombre del campo email en el form
                .passwordParameter("password")              // Nombre del campo password
                .successHandler(authenticationSuccessHandler()) // Redirige segun rol
                .failureUrl("/login?error=true")            // Redirige si falla
                .permitAll()
            )

            // Configuracion del logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            // Recordar sesion (Remember Me)
            .rememberMe(remember -> remember
                .key("proyecto3-ecommerce-secret-key")
                .tokenValiditySeconds(86400) // 24 horas
                .rememberMeParameter("remember-me")
            );

        return http.build();
    }

    /**
     * Manejador de exito: redirige segun el rol del usuario.
     * ADMIN -> /admin/dashboard
     * CLIENTE -> /cliente/home
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                response.sendRedirect("/admin/dashboard");
            } else {
                response.sendRedirect("/cliente/home");
            }
        };
    }
}
