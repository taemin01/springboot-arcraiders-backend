package org.arc.raiders.service;

import lombok.RequiredArgsConstructor;
import org.arc.raiders.domain.UserInfo;
import org.arc.raiders.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // 추가

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userInfoRepository;

    @Transactional
    public UserInfo signup(String username, String rawPassword) {

        // 1) 중복 체크
        if (userInfoRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
        }

        // 2) 비밀번호 암호화 - 여기서 바로 인스턴스 생성
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);

        // 3) 저장
        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        return userInfoRepository.save(user);
    }
}
