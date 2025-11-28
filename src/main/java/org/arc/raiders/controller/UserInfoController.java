package org.arc.raiders.controller;

import org.arc.raiders.domain.comm.UserInfo;
import org.arc.raiders.service.admin.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    // 전체 유저 목록 조회 (비밀번호 제외)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<UserInfo> users = userInfoService.getAllUsers();

        List<Map<String, Object>> response = users.stream()
                .map(this::toResponseMap)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ID로 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userInfoService.getUserById(id)
                .map(user -> ResponseEntity.ok(toResponseMap(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 유저 생성
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            UserInfo createdUser = userInfoService.createUser(request.getUserName(), request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponseMap(createdUser));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 유저 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        try {
            UserInfo updatedUser = userInfoService.updateUser(id, request.getUserName(), request.getPassword());
            return ResponseEntity.ok(toResponseMap(updatedUser));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 유저 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userInfoService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "사용자가 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // UserInfo를 응답용 Map으로 변환 (비밀번호 제외)
    private Map<String, Object> toResponseMap(UserInfo user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("userName", user.getUserName());
        map.put("createdAt", user.getCreatedAt());
        map.put("updatedAt", user.getUpdatedAt());
        return map;
    }

    // 요청 DTO 클래스들
    public static class CreateUserRequest {
        private String userName;
        private String password;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class UpdateUserRequest {
        private String userName;
        private String password;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}


