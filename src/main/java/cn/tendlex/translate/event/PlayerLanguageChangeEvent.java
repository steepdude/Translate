package cn.tendlex.translate.event;

import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.Player;

public class PlayerLanguageChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final String oldLanguage;
    private final String newLanguage;

    public PlayerLanguageChangeEvent(Player player, String oldLanguage, String newLanguage) {
        this.player = player;
        this.oldLanguage = oldLanguage;
        this.newLanguage = newLanguage;
    }

    public String getOldLanguage() {
        return oldLanguage;
    }

    public String getNewLanguage() {
        return newLanguage;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
