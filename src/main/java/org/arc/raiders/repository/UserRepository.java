package org.arc.raiders.repository;

import org.arc.raiders.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo, Integer> {

    Optional<UserInfo> findByUsername(String username);

    boolean existsByUsername(String username);
}
