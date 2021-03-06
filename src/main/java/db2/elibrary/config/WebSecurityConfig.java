package db2.elibrary.config;

import db2.elibrary.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthFilter authFilter;
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void setAuthFilter(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Autowired
    public void setUserDetailsService(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // ??????CSRF
                .csrf().disable()
                // ??????SESSION
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // ??????URL
                .authorizeRequests()
                .antMatchers("/auth").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("auth/login/unencoded").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/search/**").permitAll()
                .antMatchers("/admin/page").hasIpAddress("127.0.0.1")
                .antMatchers("/wish/**").hasAnyRole("USER", "STAFF", "ADMIN")
                .anyRequest().authenticated();

        httpSecurity
                // JWT????????????
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                // ?????????
                .headers()
                .cacheControl();
    }
}
