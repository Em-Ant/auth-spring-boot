package it.emant.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import it.emant.auth.service.TokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.cors().and().authorizeRequests()
            .antMatchers(HttpMethod.GET, "/auth").permitAll()
            .antMatchers("/db-ui/**").permitAll()
            .antMatchers("/hello").hasRole("PONTIFEX")
            .anyRequest().authenticated()
            .and()
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), tokenService))
            // this disables session creation on Spring Security
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable();
        http.headers().frameOptions().disable();           
    }
}
