package com.xnidis.auth.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.xnidis.auth.repo.UserRepo;

import reactor.core.publisher.Mono;

@Service
public class UserService {
	private UserRepo userRepo;

	public UserService(final UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public Mono<UserDetails> findByUsername(String username) {
		return userRepo.findByUsername(username)
			.cast(UserDetails.class);
	}
}
