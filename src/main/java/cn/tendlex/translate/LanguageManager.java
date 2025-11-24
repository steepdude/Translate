package cn.tendlex.translate;

import cn.nukkit.utils.Config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private final Map<String, Map<String, String>> translations = new HashMap<>();
    private String defaultLanguage = "ru_RU";

    public void setDefaultLanguage(String code) {
        this.defaultLanguage = code;
    }

    public void loadLanguagesFromFolder(File folder) {
        translations.clear();
        if (!folder.exists()) folder.mkdirs();

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            String code = file.getName().substring(0, file.getName().length() - 4);
            Config cfg = new Config(file, Config.YAML);
            Map<String, Object> raw = cfg.getAll();
            Map<String, String> flat = new HashMap<>();
            flatten("", raw, flat);
            translations.put(code, flat);
        }
    }

    @SuppressWarnings("unchecked")
    private void flatten(String prefix, Map<String, Object> src, Map<String, String> out) {
        if (src == null) return;
        for (Map.Entry<String, Object> e : src.entrySet()) {
            String key = prefix.isEmpty() ? e.getKey() : prefix + "." + e.getKey();
            Object val = e.getValue();
            if (val instanceof Map) {
                flatten(key, (Map<String, Object>) val, out);
            } else {
                out.put(key, String.valueOf(val));
            }
        }
    }

    public boolean isSupported(String code) {
        return translations.containsKey(code);
    }

    public String getTranslation(String lang, String key) {
        String v = get(translations.get(lang), key);
        if (v != null) return v;

        if (!lang.equals(defaultLanguage)) {
            v = get(translations.get(defaultLanguage), key);
            if (v != null) return v;
        }
        return null;
    }

    private String get(Map<String, String> map, String key) {
        return map == null ? null : map.get(key);
    }
}
