package com.xnidis.auth.rest;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.xnidis.auth.config.security.JwtUtil;
import com.xnidis.auth.domain.User;
import com.xnidis.auth.models.SignRequest;
import com.xnidis.auth.services.UserService;

import reactor.core.publisher.Mono;

@RestController
public class UserController {
	private final static ResponseEntity<Object> UNAUTHORIZED =
		ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	private final UserService userService;
	private final JwtUtil jwtUtil;

	public UserController(final UserService userService, final JwtUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/sign-in")
	public Mono<ResponseEntity<?>> sigIn(@RequestBody SignRequest signRequest) {

		return userService.findByUsername(signRequest.username())
			.cast(User.class)
			.map(userDetails ->
				Objects.equals(signRequest.password(), userDetails.getPassword())
					? ResponseEntity.ok(jwtUtil.generateToken(userDetails))
					: UNAUTHORIZED
			).defaultIfEmpty(UNAUTHORIZED);
	}




}
