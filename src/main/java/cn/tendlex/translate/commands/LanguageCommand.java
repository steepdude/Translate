package cn.tendlex.translate.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.tendlex.translate.LocaleAPI;
import cn.tendlex.translate.Translate;

import java.io.File;

public class LanguageCommand extends Command {

    public static final int LANGUAGE_FORM_ID = 777;

    public LanguageCommand() {
        super("lang", "Change your language or reload translations", "/lang [reload]");
        setPermission("locale.lang");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.isOp() && !sender.hasPermission("locale.reload")) {
                sender.sendMessage("§cYou don't have permission to do this!");
                return true;
            }

            Translate plugin = Translate.getInstance();

            plugin.reloadConfig();

            plugin.getLanguageManager().loadLanguagesFromFolder(
                    new File(plugin.getDataFolder(), "lang")
            );

            sender.sendMessage("§a[Translate] Config and languages reloaded!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only for players.");
            return true;
        }
        Player p = (Player) sender;

        FormWindowSimple form = new FormWindowSimple(
                "🌍 Select Language",
                "Выберите язык / Choose language / Оберіть мову"
        );

        form.addButton(new ElementButton("Русский (ru_RU)",
                new ElementButtonImageData("url", "https://flagcdn.com/w40/ru.png")));
        form.addButton(new ElementButton("English (en_US)",
                new ElementButtonImageData("url", "https://flagcdn.com/w40/us.png")));
        form.addButton(new ElementButton("Українська (uk_UA)",
                new ElementButtonImageData("url", "https://flagcdn.com/w40/ua.png")));

        p.showFormWindow(form, LANGUAGE_FORM_ID);
        return true;
    }

    public static void handleForm(Player player, FormWindowSimple form) {
        if (form.getResponse() == null) return;

        FormResponseSimple response = form.getResponse();
        int buttonId = response.getClickedButtonId();

        String code = null;
        switch (buttonId) {
            case 0 -> code = "ru_RU";
            case 1 -> code = "en_US";
            case 2 -> code = "uk_UA";
        }

        if (code != null && Translate.getInstance().getLanguageManager().isSupported(code)) {
            LocaleAPI.setLanguage(player, code);
            player.sendMessage(LocaleAPI.tr(player, "lang.changed", code));
        }
    }
}
