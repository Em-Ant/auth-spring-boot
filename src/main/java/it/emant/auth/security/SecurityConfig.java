package it.emant.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import it.emant.auth.security.filters.ApiKeyAuthenticationFilter;
import it.emant.auth.security.filters.JwtAuthorizationFilter;
import it.emant.auth.security.providers.ApiKeyAuthenticationProvider;
import it.emant.auth.service.TokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApiKeyAuthenticationProvider authProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.cors().and().authorizeRequests()
            .antMatchers(HttpMethod.GET, "/auth").permitAll()
            .antMatchers("/db-ui/**").permitAll()
            .antMatchers("/hello").hasRole("USER")
            .anyRequest().authenticated()
            .and()
            .addFilter(new ApiKeyAuthenticationFilter(authenticationManager(), tokenService))
            .addFilterAt(new JwtAuthorizationFilter(tokenService), BasicAuthenticationFilter.class)
            // this disables session creation on Spring Security
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
