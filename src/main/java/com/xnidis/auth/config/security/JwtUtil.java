package com.xnidis.auth.config.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xnidis.auth.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.expiration}")
	private String expirationTime;

	public String extractUsername(String authToken) {
		return getClaimsFromToken(authToken)
			.getSubject();
	}

	public Claims getClaimsFromToken(String authToken) {
		String key = Base64.getEncoder().encodeToString(secret.getBytes());
		return Jwts.parser()
			.setSigningKey(key)
			.parseClaimsJws(authToken)
			.getBody();
	}

	public boolean validateToken(String authToken) {
		return getClaimsFromToken(authToken)
			.getExpiration()
			.after(new Date());
	}

	public String generateToken(User user) {
		HashMap<String, Object> claims = new HashMap<>();
//		claims.put("role", List.of(user.getRole()));

		long expirationSeconds = Long.parseLong(expirationTime);
		Date creationDate = new Date();
		Date expirationDate = new Date(creationDate.getTime() + expirationSeconds * 1000);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuer("JWT")
			.setSubject(user.getUsername())
			.setIssuedAt(creationDate)
			.setExpiration(expirationDate)
		//	.signWith(Keys.hmacShaKeyFor(secret.getBytes()))
			.signWith(
				SignatureAlgorithm.HS256,
				TextCodec.BASE64.decode(secret)
			)
			.compact();
	}
}
