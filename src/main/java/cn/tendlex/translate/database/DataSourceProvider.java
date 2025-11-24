package cn.tendlex.translate.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSourceProvider {
    void initialize() throws Exception;
    Connection getConnection() throws SQLException;
    void close();
}
