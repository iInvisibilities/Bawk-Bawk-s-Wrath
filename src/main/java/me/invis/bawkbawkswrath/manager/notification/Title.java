package me.invis.bawkbawkswrath.manager.notification;

import org.bukkit.Bukkit;

public class Title {

    public Title(String content, boolean isSmall) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendTitle(!isSmall ? content : "", isSmall ? content : ""));
    }

}
