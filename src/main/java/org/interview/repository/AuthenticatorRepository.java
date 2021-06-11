package org.interview.repository;

import java.util.Optional;

import org.interview.entity.Authenticator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticatorRepository extends JpaRepository<Authenticator, Long> {

	public Optional<Authenticator> findByValid(Boolean valid);
}
