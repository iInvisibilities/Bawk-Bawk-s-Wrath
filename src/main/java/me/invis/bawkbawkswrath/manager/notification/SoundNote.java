package me.invis.bawkbawkswrath.manager.notification;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class SoundNote {

    public SoundNote(float pitch) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_PIANO, 1, pitch));
    }

}
