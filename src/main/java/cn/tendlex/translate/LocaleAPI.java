package cn.tendlex.translate;

import cn.nukkit.Player;
import cn.tendlex.translate.database.DatabaseManager;
import cn.tendlex.translate.event.PlayerLanguageChangeEvent;

import java.util.*;

public class LocaleAPI {

    private static final Set<String> SUPPORTED_LANGUAGES = new HashSet<>(Arrays.asList(
            "ru_RU", "en_US", "uk_UA"
    ));
    private static final String DEFAULT_LANGUAGE = "ru_RU";

    // Кэш языков для игроков (ключ — имя как есть)
    private static final Map<String, String> CACHE = new HashMap<>();

    public static void warmup(Player player) {
        if (player == null || player.getLocale() == null) {
            return;
        }
        String lang = getPlayerLanguage(player);
        CACHE.put(player.getName(), lang);
    }

    public static void unload(Player player) {
        if (player != null) {
            CACHE.remove(player.getName());
        }
    }

    public static String getPlayerLanguage(Player player) {
        if (player == null) {
            return DEFAULT_LANGUAGE;
        }

        String nickname = player.getName();

        if (CACHE.containsKey(nickname)) {
            return CACHE.get(nickname);
        }

        String dbLang = DatabaseManager.getInstance().getPlayerLanguage(nickname);
        if (dbLang != null && SUPPORTED_LANGUAGES.contains(dbLang)) {
            CACHE.put(nickname, dbLang);
            return dbLang;
        }

        if (player.getLocale() != null) {
            String locale = player.getLocale().toString();
            if (SUPPORTED_LANGUAGES.contains(locale)) {
                CACHE.put(nickname, locale);
                return locale;
            }
        }

        CACHE.put(nickname, DEFAULT_LANGUAGE);
        return DEFAULT_LANGUAGE;
    }

    public static void setLanguage(Player player, String lang) {
        if (player == null || !SUPPORTED_LANGUAGES.contains(lang)) {
            return;
        }

        String nickname = player.getName();
        String oldLang = CACHE.getOrDefault(nickname, null);

        DatabaseManager.getInstance().updatePlayerLanguage(nickname, lang);
        CACHE.put(nickname, lang);

        if (oldLang == null || !oldLang.equals(lang)) {
            PlayerLanguageChangeEvent event = new PlayerLanguageChangeEvent(player, oldLang, lang);
            player.getServer().getPluginManager().callEvent(event);
        }
    }


    public static String tr(Player player, String key, Object... replacements) {
        String lang = getPlayerLanguage(player);
        String message = Translate.getInstance().getLanguageManager().getTranslation(lang, key);

        if (message == null) {
            message = key;
        }

        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(replacements[i]));
        }

        if (replacements.length == 1 && replacements[0] instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) replacements[0];
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
        }

        return message;
    }
}
