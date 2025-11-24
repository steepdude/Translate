package cn.tendlex.translate.database;

import cn.nukkit.scheduler.AsyncTask;
import cn.tendlex.translate.Translate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Queries {

    public static void getPlayer(Translate plugin, DatabaseManager db, String playerName, Consumer<Map<String, Object>> callback) {
        plugin.getServer().getScheduler().scheduleAsyncTask(plugin, new AsyncTask() {
            @Override
            public void onRun() {
                Map<String, Object> playerData = null;
                try (Connection conn = db.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM auth WHERE nickname = ?")) {
                    stmt.setString(1, playerName);
                    ResultSet rs = stmt.executeQuery();
                    playerData = new HashMap<>();
                    if (rs.next()) {
                        playerData.put("id", rs.getInt("id"));
                        playerData.put("nickname", rs.getString("nickname"));
                        playerData.put("password", rs.getString("password"));
                        playerData.put("uuid", rs.getString("uuid"));
                        playerData.put("language", rs.getString("language"));
                        playerData.put("ip", rs.getString("ip"));
                        playerData.put("vk_id", rs.getString("vk_id"));
                        playerData.put("tg_id", rs.getString("tg_id"));
                        playerData.put("date", rs.getString("date"));
                        playerData.put("last_join", rs.getString("last_join"));
                    }
                } catch (SQLException e) {
                    plugin.getLogger().error("Failed to get player data: " + e.getMessage());
                }

                Map<String, Object> result = playerData != null && !playerData.isEmpty() ? playerData : null;
                plugin.getServer().getScheduler().scheduleTask(plugin, () -> callback.accept(result));
            }
        });
    }
}
