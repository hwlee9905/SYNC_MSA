package com.simple.book.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
	
	public static Messa() {
		
	}
	
	
	@Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authz) -> authz
            	.requestMatchers("/",
            			         "/user/signup",
            			         "/page/*",
            			         "/login/proc"
            			          ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/page/login")
                    .usernameParameter("userId")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/")
                    .loginProcessingUrl("/login/proc")
                    .failureHandler(new AuthFailureHandler())
                    .permitAll()
            )
            .logout(logout -> logout
                    .permitAll());
            //.httpBasic(withDefaults());
        return http.build();
    }

}