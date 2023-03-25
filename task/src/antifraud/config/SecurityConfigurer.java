package antifraud.config;




import antifraud.common.RoleEnum;
import antifraud.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfigurer {

    private final CustomUserDetailsService myUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationEntryPoint restAuthenticationEntryPoint) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers( httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                        .frameOptions()
                        .disable())
                .authenticationProvider(authenticationProvider())
                .exceptionHandling( exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                )
                .authorizeHttpRequests( auth -> {
                        auth.requestMatchers(antMatcher("/h2/**"), antMatcher("/actuator/shutdown"), antMatcher("/api/auth/user")).permitAll()
                                .requestMatchers(antMatcher(HttpMethod.POST,"/api/antifraud/transaction"), antMatcher(HttpMethod.POST,"/api/antifraud/transaction/**")).hasRole(RoleEnum.ROLE_MERCHANT.getText())
                                .requestMatchers(antMatcher("/api/antifraud/suspicious-ip"), antMatcher("/api/antifraud/suspicious-ip/**"), antMatcher("/api/antifraud/stolencard")
                                , antMatcher("/api/antifraud/stolencard/**"), antMatcher(HttpMethod.PUT,"/api/antifraud/transaction"), antMatcher(HttpMethod.GET,"/api/antifraud/history/**")).hasRole(RoleEnum.ROLE_SUPPORT.getText())
                                .requestMatchers(antMatcher("/api/auth/list/**")).hasAnyRole(RoleEnum.ROLE_SUPPORT.getText(), RoleEnum.ROLE_ADMINISTRATOR.getText())
                                .requestMatchers(antMatcher("/api/auth/access/**"), antMatcher("/api/auth/role/**"), antMatcher("/api/auth/user/**")).hasRole(RoleEnum.ROLE_ADMINISTRATOR.getText())
                                .anyRequest().authenticated();
                        }
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }









}
