package com.example.securityjwttemplate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                /*
                 * disable csrf protection (common for Stateless APIs)
                 */
                .csrf(customizer -> customizer
                        .disable()
                )
                /*
                 * requestMatchers(*HttpMethod method*, String... pattern) - define URL patterns to secure
                 * permitAll - allow all users (authentication and not)
                 * denyAll - deny all users
                 * authenticated - allow only authenticated users
                 * hasRole("ADMIN") - allow only users with specific role (e.g. ROLE_ADMIN)
                 * hasAnyRole("A", "B") - allow any role in the list
                 */
                .authorizeHttpRequests(request -> request
                        .requestMatchers("api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                /*
                 * ALWAYS - always create session
                 * STATELESS - no session in any case
                 *  NEVER - never create but use if one already exists
                 *  IF_REQUIRED (default) - create only if needed (e.g. on login)
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }
}
