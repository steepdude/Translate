package cn.tendlex.translate;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.window.FormWindowSimple;
import cn.tendlex.translate.commands.LanguageCommand;

public class CacheListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        LocaleAPI.warmup(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        LocaleAPI.unload(e.getPlayer());
    }

    @EventHandler
    public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();

        if (event.getWindow() instanceof FormWindowSimple form) {
            int formId = event.getFormID();
            if (formId == LanguageCommand.LANGUAGE_FORM_ID) {
                LanguageCommand.handleForm(player, form);
            }
        }
    }
}
