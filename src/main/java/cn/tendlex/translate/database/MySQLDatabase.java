package cn.tendlex.translate.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLDatabase implements DataSourceProvider {

    private final String host;
    private final int port;
    private final String db;
    private final String user;
    private final String pass;

    private HikariDataSource ds;

    public MySQLDatabase(String host, int port, String db, String user, String pass) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public void initialize() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setDriverClassName("com.mysql.cj.jdbc.Driver");
        cfg.setMaximumPoolSize(10);
        cfg.setConnectionTimeout(30000);
        cfg.addDataSourceProperty("cachePrepStmts", "true");
        cfg.addDataSourceProperty("prepStmtCacheSize", "250");
        cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(cfg);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public void close() {
        if (ds != null) ds.close();
    }
}
