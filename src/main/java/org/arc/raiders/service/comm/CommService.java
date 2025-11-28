package org.arc.raiders.service.comm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.arc.raiders.config.JwtTokenProvider;
import org.arc.raiders.domain.comm.UserInfo;
import org.arc.raiders.repository.comm.CommRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class CommService {

    private final CommRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  // ★ 직접 생성
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserInfo signup(String userName, String rawPassword) {
        if (userInfoRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserInfo user = new UserInfo();
        user.setUserName(userName);
        user.setPassword(encodedPassword);

        return userInfoRepository.save(user);
    }

    @Transactional(readOnly = true)
    public String login(String username, String rawPassword) {

        UserInfo user = userInfoRepository.findByUserName(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtTokenProvider.createToken(user.getUserName());
    }
}