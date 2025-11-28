package org.arc.raiders.service.comm;

import lombok.RequiredArgsConstructor;
import org.arc.raiders.config.JwtTokenProvider;
import org.arc.raiders.domain.comm.UserInfo;
import org.arc.raiders.repository.comm.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  // ★ 직접 생성
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserInfo signup(String username, String rawPassword) {
        if (userInfoRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        return userInfoRepository.save(user);
    }

    @Transactional(readOnly = true)
    public String login(String username, String rawPassword) {

        UserInfo user = userInfoRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtTokenProvider.createToken(user.getUsername());
    }
}