package raviolz.understory_back.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenFilter tokenFilter;

    public SecurityConfig(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable());

        httpSecurity.formLogin(formLogin -> formLogin.disable());

        httpSecurity.httpBasic(httpBasic -> httpBasic.disable());

        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(STATELESS)
        );

        httpSecurity.authorizeHttpRequests(requests -> requests
                .requestMatchers("/auth/**", "/error").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
        );

        httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}