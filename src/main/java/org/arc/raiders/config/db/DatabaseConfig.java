package org.arc.raiders.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class DatabaseConfig {
    private final SshTunnelConfig sshTunnelConfig;

    @Value("${DB_URL}")
    private String dbUrl;

    @Value("${DB_USERNAME}")
    private String dbUser;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Value("${DB_PORT}")
    private int dbPort;

    @Value("${DB_NAME}")
    private String dbName;

    @Bean
    @Primary
    @DependsOn("sshTunnelConfig") // SSH 터널 설정 후 생성
    public DataSource dataSource() {
        // SSH 터널이 준비될 때까지 대기
        waitForSshTunnel();

        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", dbUrl, dbPort, dbName);

        log.info("DataSource 생성: {}", jdbcUrl);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        config.setDriverClassName("org.postgresql.Driver");

        // 연결 풀 설정
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }

    private void waitForSshTunnel() {
        int waitTime = 0;

        while (!sshTunnelConfig.getSession().isConnected()) {
            try {
                Thread.sleep(2000);
                waitTime++;
                log.debug("SSH 터널 대기 중... ({}초)", waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("SSH 터널 대기 중 인터럽트 발생", e);
            }
        }

        if (!sshTunnelConfig.getSession().isConnected()) {
            throw new RuntimeException("SSH 터널 연결 실패 - 시간 초과");
        }

        log.info("SSH 터널 준비 완료, DataSource 생성 진행");
    }
}
