package org.arc.raiders.repository.admin;

import org.arc.raiders.domain.comm.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    // userName으로 사용자 조회
    Optional<UserInfo> findByUserName(String userName);

    // userName 존재 여부 확인
    boolean existsByUserName(String userName);
}


