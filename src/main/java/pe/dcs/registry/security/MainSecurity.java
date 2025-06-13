package pe.dcs.registry.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.dcs.registry.repository.IRolDAO;
import pe.dcs.registry.repository.IUsuarioDAO;
import pe.dcs.registry.security.jwt.JwtEntryPoint;
import pe.dcs.registry.security.jwt.JwtProvider;
import pe.dcs.registry.security.jwt.JwtTokenFilter;
import pe.dcs.registry.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MainSecurity {

    final UserDetailsServiceImpl userDetailService;
    final JwtEntryPoint unauthorizedHandler;
    final JwtProvider jwtProvider;

    IUsuarioDAO dataUsuario;
    IRolDAO dataRol;

    public MainSecurity(UserDetailsServiceImpl userDetailService, JwtEntryPoint unauthorizedHandler, JwtProvider jwtProvider) {
        this.userDetailService = userDetailService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {

        return new JwtTokenFilter(jwtProvider, userDetailService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        return new UserDetailsServiceImpl(dataUsuario, dataRol);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationManagerBean() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(unauthorizedHandler));

        httpSecurity
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        httpSecurity.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
