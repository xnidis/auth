package com.xnidis.auth.config.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager  implements ReactiveAuthenticationManager {
	private final JwtUtil jwtUtil;

	public AuthenticationManager(final JwtUtil jwtUtil) {this.jwtUtil = jwtUtil;}

	@Override
	public Mono<Authentication> authenticate(final Authentication authentication) {
		String authToken = authentication.getCredentials().toString();

		String username;

		try {
			username = jwtUtil.extractUsername(authToken);
		} catch (Exception e) {
			username = null;
		}

		if (username != null && jwtUtil.validateToken(authToken)) {
			Claims claims = jwtUtil.getClaimsFromToken(authToken);
			List<String> role = claims.get("role", List.class);
			List<SimpleGrantedAuthority> authorities = role.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				username,
				null,
				authorities
			);

			return Mono.just(authenticationToken);
		} else {
			return Mono.empty();
		}
	}
}
