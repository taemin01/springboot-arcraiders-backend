package org.arc.raiders.service;

import lombok.RequiredArgsConstructor;
import org.arc.raiders.domain.UserInfo;
import org.arc.raiders.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // 2) 비밀번호 암호화 (빈 주입 대신 직접 생성)
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 3) 저장
        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        return userInfoRepository.save(user);
    }
}
