package com.example.securityjwttemplate.config;

import com.example.securityjwttemplate.filter.JwtAuthenticationFilter;
import com.example.securityjwttemplate.handler.JwtAccessDeniedHandler;
import com.example.securityjwttemplate.handler.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // injected as internal config bean
    private PasswordEncoder passwordEncoder;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

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
                /*
                 * .addFilter(Filter filter, Class<Filter> anotherFilter)
                 *   - register filter at the same spot
                 * .addFilterBefore(Filter filter, Class<Filter> anotherFilter)
                 *   - register custom filter before the other specified one
                 * .addFilterAfter(Filter filter, Class<Filter> anotherFilter)
                 *   - register filters after the specified one
                 */
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        // authenticationEntryPoint - handles invalid/missing JWT
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        // authenticationEntryPoint - handles improper access role
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedOriginPatterns(List.of("https://my-frontend-app.com"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder pe) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(pe);
        return provider;
    }
}
