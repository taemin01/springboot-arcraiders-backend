package org.arc.raiders.controller;

import org.arc.raiders.domain.UserInfo;
import org.arc.raiders.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000") // Vite dev 서버
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ===== DTO =====
    public record SignupRequest(String username, String password) {}
    public record SignupResponse(Integer id, String username) {}

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            UserInfo user = userService.signup(request.username(), request.password());
            SignupResponse response = new SignupResponse(user.getId(), user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // 중복 아이디 등 비즈니스 오류
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            // 그 외 서버 오류
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입 처리 중 오류가 발생했습니다.");
        }
    }
}
