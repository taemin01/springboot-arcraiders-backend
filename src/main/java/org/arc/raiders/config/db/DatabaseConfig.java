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
    private String dbHost;

    @Value("${DB_TUNNEL_PORT}")
    private int dbPort;

    @Value("${DB_NAME}")
    private String dbName;

    @Value("${DB_USERNAME}")
    private String dbUser;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Bean
    @Primary
    @DependsOn("sshTunnelConfig")
    public DataSource dataSource() {
        waitForSshTunnel();

        String jdbcUrl = String.format(
                "jdbc:postgresql://%s:%d/%s?sslmode=disable",
                dbHost, dbPort, dbName
        );
        log.info("DataSource 생성: {}", jdbcUrl);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        config.setDriverClassName("org.postgresql.Driver");

        // 풀 설정 (원하시면 값 조정)
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30_000);
        config.setIdleTimeout(600_000);
        config.setMaxLifetime(1_800_000);

        return new HikariDataSource(config);
    }

    private void waitForSshTunnel() {
        int waited = 0;
        while (sshTunnelConfig.getSession() == null
                || !sshTunnelConfig.getSession().isConnected()) {
            try {
                Thread.sleep(1_000);
                waited++;
                log.debug("SSH 터널 대기 중... ({}초)", waited);
                if (waited > 60) {
                    throw new RuntimeException("SSH 터널 연결 대기 시간 초과");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("SSH 터널 대기 중 인터럽트 발생", e);
            }
        }
        log.info("SSH 터널 준비 완료, DataSource 생성 진행");
    }
}
