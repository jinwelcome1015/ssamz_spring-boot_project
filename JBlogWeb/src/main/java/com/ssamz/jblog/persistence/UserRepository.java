package com.ssamz.jblog.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssamz.jblog.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	// SELECT * FROM user WHERE username = ?1;
	Optional<User> findByUsername(String username);
}
