package com.xnidis.auth.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.xnidis.auth.domain.User;

import reactor.core.publisher.Mono;

public interface UserRepo extends ReactiveCrudRepository<User, Long> {
	Mono<User> findByUsername(String name);
}
