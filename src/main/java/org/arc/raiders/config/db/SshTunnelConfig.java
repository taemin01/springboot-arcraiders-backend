package org.arc.raiders.config.db;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import jakarta.annotation.PostConstruct;
import jdk.jfr.Description;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Getter
@Log4j2
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SshTunnelConfig {

    private Session session;

    @Value("${SSH_URL}")
    private String sshHost;

    @Value("${SSH_PORT}")
    private Integer sshPort;

    @Value("${SSH_USER}")
    private String sshUser;

    @Value("${DB_URL}")
    private String dbUrl;

    @Value("${DB_PORT}")
    private Integer dbPort;

    // key passphrase == DB_PASSWORD
    @Description("DB Password == key passphrase")
    @Value("${DB_PASSWORD}")
    private String keyPassPhrase;

    @PostConstruct
    public void init() {
        log.info("SSH 접속 사용자: {}", sshUser);

        try {
            // 1. 리소스에서 키 파일 읽기
            InputStream keyInputStream = getClass().getClassLoader().getResourceAsStream("minipc_id_rsa");
            if (keyInputStream == null) {
                throw new FileNotFoundException("minipc_id_rsa not found in classpath");
            }

            byte[] privateKeyBytes = keyInputStream.readAllBytes();

            // 2. JSch 인스턴스 생성
            JSch jsch = new JSch();

            // 3. 패스프레이즈 포함하여 Identity 로 등록
            jsch.addIdentity(
                    "minipc_id_rsa",
                    privateKeyBytes,
                    null,
                    keyPassPhrase.getBytes(StandardCharsets.UTF_8)
            );

            // 로깅용
            JSch.setLogger(new com.jcraft.jsch.Logger() {
                public boolean isEnabled(int level) { return true; }
                public void log(int level, String message) { System.out.println("[JSCH] " + message); }
            });

            // 4. 세션 생성
            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey");

            // 5. 연결
            session.connect();

            // 6. 포트 포워딩
            session.setPortForwardingL(dbPort, dbUrl, dbPort);

            log.info("SSH 터널 연결 완료: localhost:{} → {}:{}", dbPort, dbUrl, dbPort);

        } catch (Exception e) {
            log.error("SSH 터널 설정 실패", e);
        }
    }
}
