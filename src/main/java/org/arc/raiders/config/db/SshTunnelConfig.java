package org.arc.raiders.config.db;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import jakarta.annotation.PostConstruct;
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

    // 서버(Postgres) 포트 (5432)
    @Value("${DB_PORT}")
    private Integer remoteDbPort;

    // 로컬 터널 포트 (55432)
    @Value("${DB_TUNNEL_PORT}")
    private Integer localTunnelPort;

    // 키 패스프레이즈 (DB_PASSWORD와 동일하게 사용)
    @Value("${DB_PASSWORD}")
    private String keyPassPhrase;

    @PostConstruct
    public void init() {
        log.info("SSH 접속 사용자: {}", sshUser);
        try {
            // 1. classpath에서 개인키 읽기
            InputStream keyInputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("minipc_id_rsa");
            if (keyInputStream == null) {
                throw new FileNotFoundException("minipc_id_rsa not found in classpath");
            }
            byte[] privateKeyBytes = keyInputStream.readAllBytes();

            // 2. JSch 설정
            JSch jsch = new JSch();
            jsch.addIdentity(
                    "minipc_id_rsa",
                    privateKeyBytes,
                    null,
                    keyPassPhrase.getBytes(StandardCharsets.UTF_8)
            );

            JSch.setLogger(new com.jcraft.jsch.Logger() {
                @Override
                public boolean isEnabled(int level) {
                    return true;
                }

                @Override
                public void log(int level, String message) {
                    System.out.println("[JSCH] " + message);
                }
            });

            // 3. SSH 세션 생성 및 접속
            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey");
            session.connect();
            log.info("SSH 세션 연결 완료: {}@{}:{}", sshUser, sshHost, sshPort);

            // 4. 포트 포워딩
            // [로컬] localhost:localTunnelPort -> [서버] localhost:remoteDbPort(Postgres)
            session.setPortForwardingL(localTunnelPort, "localhost", remoteDbPort);
            log.info("SSH 터널 연결 완료: localhost:{} → localhost:{} (원격 기준)",
                    localTunnelPort, remoteDbPort);

        } catch (Exception e) {
            log.error("SSH 터널 설정 실패", e);
        }
    }
}
