package org.arc.raiders.repository.comm;

import org.arc.raiders.domain.comm.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommRepository extends JpaRepository<UserInfo, Integer> {

    Optional<UserInfo> findByUserName(String username);

    boolean existsByUserName(String username);
}
