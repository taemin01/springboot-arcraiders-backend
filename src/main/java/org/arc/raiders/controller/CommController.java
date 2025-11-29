package org.arc.raiders.controller;

import org.arc.raiders.domain.comm.LoginRequest;
import org.arc.raiders.domain.comm.LoginResponse;
import org.arc.raiders.domain.comm.UserInfo;
import org.arc.raiders.service.comm.CommService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comm")
//@CrossOrigin(origins = "http://localhost:3000") // Vite dev 서버
public class CommController {

    private final CommService commService;

    public CommController(CommService commService) {
        this.commService = commService;
    }

    // ===== DTO =====
    public record SignupRequest(String username, String password) {}
    public record SignupResponse(String id, String username) {}

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            UserInfo user = commService.signup(request.username(), request.password());
            SignupResponse response = new SignupResponse(user.getUserName(), user.getPassword());
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

    // ★ 로그인 API
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = commService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
