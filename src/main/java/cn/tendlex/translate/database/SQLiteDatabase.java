package cn.tendlex.translate.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteDatabase implements DataSourceProvider {

    private final File file;
    private HikariDataSource ds;

    public SQLiteDatabase(File file) {
        this.file = file;
    }

    @Override
    public void initialize() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());
        cfg.setDriverClassName("org.sqlite.JDBC");
        cfg.setMaximumPoolSize(1);
        cfg.setConnectionTimeout(30000);
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
