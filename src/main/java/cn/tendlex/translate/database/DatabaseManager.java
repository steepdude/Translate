package cn.tendlex.translate.database;

import cn.nukkit.plugin.PluginBase;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final PluginBase plugin;
    private HikariDataSource dataSource;

    public DatabaseManager(PluginBase plugin, String type) {
        this.plugin = plugin;
        instance = this;

        if (type.equalsIgnoreCase("mysql")) {
            initializeMySQL();
        } else {
            initializeSQLite();
        }
        createTable();
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    private void initializeMySQL() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/auth_db?useSSL=false");
        config.setUsername("root");
        config.setPassword("password");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        this.dataSource = new HikariDataSource(config);
    }

    private void initializeSQLite() {
        HikariConfig config = new HikariConfig();

        String dbPath = "C:/Users/gedfi/Desktop/bedrocktendlex/lobby/plugins/AuthTL/auth.db";
        String url = "jdbc:sqlite:" + dbPath;

        config.setJdbcUrl(url);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(10);
        config.setPoolName("Translate-SQLite");

        this.dataSource = new HikariDataSource(config);
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS auth (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nickname TEXT NOT NULL UNIQUE," +
                "password TEXT," +
                "uuid TEXT," +
                "language TEXT," +
                "ip TEXT," +
                "date DATETIME," +
                "last_join DATETIME)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException ignored) {
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public String getPlayerLanguage(String nickname) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT language FROM auth WHERE nickname = ?")) {
            stmt.setString(1, nickname);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("language");
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    public void updatePlayerLanguage(String nickname, String lang) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE auth SET language = ? WHERE nickname = ?")) {
            stmt.setString(1, lang);
            stmt.setString(2, nickname);
            stmt.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
