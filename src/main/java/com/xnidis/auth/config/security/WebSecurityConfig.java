package com.xnidis.auth.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
	private final AuthenticationManager authenticationManager;
	private final SecurityContextRepository securityContextRepository;

	public WebSecurityConfig(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
		this.authenticationManager = authenticationManager;
		this.securityContextRepository = securityContextRepository;
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
		return httpSecurity
			.exceptionHandling()
			.authenticationEntryPoint(
				(swe, e) ->
					Mono.fromRunnable(
						() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
					)
			)
			.accessDeniedHandler(
				(swe, e) ->
					Mono.fromRunnable(
						() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)
					)
			)
			.and()
			.csrf().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.authenticationManager(authenticationManager)
			.securityContextRepository(securityContextRepository)
			.authorizeExchange()
			.pathMatchers("/", "/sign-in", "/favicon.ico").permitAll()
			//.pathMatchers("/controller").hasRole("ADMIN")
			.anyExchange().authenticated()
			.and()
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
