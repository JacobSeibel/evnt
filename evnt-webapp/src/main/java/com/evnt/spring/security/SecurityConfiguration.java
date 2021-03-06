package com.evnt.spring.security;

import com.evnt.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Configuration
@Order(200)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(userDetailService).and().authenticationProvider(authenticationProvider);
    }

    /**
     * Configure Spring Security for this application.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                // permit access to any resource, access restrictions are handled at the level of Vaadin views
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                // disable CSRF (Cross-Site Request Forgery) since Vaadin implements its own mechanism for this
                .csrf().disable()
                // let Vaadin be responsible for creating and managing its own sessions
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
        // @formatter:on
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, exception) -> {
            final Throwable cause = exception.getCause();
            log.error("An error occurred: {}", exception.getClass().getName());
            exception.printStackTrace();
            if (cause != null) {
                log.error("        Caused by: {}", cause.getClass().getName());
            }
            response.sendRedirect("/error");
        };
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            UserDetails user = userDetailService.loadUserByUsername((String)authentication.getPrincipal());
            if(BCrypt.checkpw((String)authentication.getCredentials(), user.getPassword())) {
                return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), user.getAuthorities());
            }
            return authentication;
        };
    }
}
