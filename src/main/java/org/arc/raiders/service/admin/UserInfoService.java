package org.arc.raiders.service.admin;

import org.arc.raiders.domain.comm.UserInfo;
import org.arc.raiders.repository.admin.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    // 전체 유저 목록 조회
    @Transactional(readOnly = true)
    public List<UserInfo> getAllUsers() {
        return userInfoRepository.findAll();
    }

    // ID로 유저 조회
    @Transactional(readOnly = true)
    public Optional<UserInfo> getUserById(Long id) {
        return userInfoRepository.findById(id);
    }

    // userName으로 유저 조회
    @Transactional(readOnly = true)
    public Optional<UserInfo> getUserByUserName(String userName) {
        return userInfoRepository.findByUserName(userName);
    }

    // 유저 생성
    public UserInfo createUser(String userName, String password) {
        // 중복 체크
        if (userInfoRepository.existsByUserName(userName)) {
            throw new RuntimeException("이미 존재하는 사용자명입니다: " + userName);
        }

        UserInfo userInfo = new UserInfo(userName, password);
        return userInfoRepository.save(userInfo);
    }

    // 유저 정보 수정
    public UserInfo updateUser(Long id, String userName, String password) {
        UserInfo userInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));

        // 다른 사용자가 같은 userName을 사용하는지 체크
        Optional<UserInfo> existingUser = userInfoRepository.findByUserName(userName);
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new RuntimeException("이미 존재하는 사용자명입니다: " + userName);
        }

        userInfo.setUserName(userName);
        if (password != null && !password.isEmpty()) {
            userInfo.setPassword(password);
        }

        return userInfoRepository.save(userInfo);
    }

    // 유저 삭제
    public void deleteUser(Long id) {
        if (!userInfoRepository.existsById(id)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + id);
        }
        userInfoRepository.deleteById(id);
    }

    // 로그인 검증
    @Transactional(readOnly = true)
    public boolean validateLogin(String userName, String password) {
        Optional<UserInfo> userInfo = userInfoRepository.findByUserName(userName);
        return userInfo.isPresent() && userInfo.get().getPassword().equals(password);
    }
}


