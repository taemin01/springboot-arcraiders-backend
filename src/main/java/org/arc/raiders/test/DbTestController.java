package org.arc.raiders.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbTestController {

    private final DbTestRepository dbTestRepository;

    public DbTestController(DbTestRepository dbTestRepository) {
        this.dbTestRepository = dbTestRepository;
    }

    @GetMapping("/api/db-test")
    public String testDb() {
        Integer result = dbTestRepository.testConnection();
        return result != null ? "DB 연결 성공!" : "DB 연결 실패!";
    }
}
