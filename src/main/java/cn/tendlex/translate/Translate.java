package cn.tendlex.translate;

import cn.nukkit.plugin.PluginBase;
import cn.tendlex.translate.commands.LanguageCommand;
import cn.tendlex.translate.database.DatabaseManager;

import java.io.File;

public class Translate extends PluginBase {

    private static Translate instance;
    private LanguageManager languageManager;
    private DatabaseManager databaseManager;

    public static Translate getInstance() {
        return instance;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void onEnable() {
        instance = this;

        // кфгшки
        saveDefaultConfig();
        saveResource("lang/ru_RU.yml", false);
        saveResource("lang/en_US.yml", false);
        saveResource("lang/uk_UA.yml", false);

        // инициализируем бд
        String dbType = getConfig().getString("database.type", "sqlite");
        databaseManager = new DatabaseManager(this, dbType);

        // файлы локализации
        languageManager = new LanguageManager();
        languageManager.setDefaultLanguage(getConfig().getString("default-language", "ru_RU"));
        languageManager.loadLanguagesFromFolder(new File(getDataFolder(), "lang"));


        // Команда /lang
        getServer().getCommandMap().register("lang", new LanguageCommand());

        // Листенер для кэша
        getServer().getPluginManager().registerEvents(new CacheListener(), this);

        getLogger().info("Локализация включена!");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("Локализация выключена!");
    }
}
