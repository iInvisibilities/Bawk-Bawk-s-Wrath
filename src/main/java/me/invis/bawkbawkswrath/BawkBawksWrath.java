package me.invis.bawkbawkswrath;

import me.invis.bawkbawkswrath.manager.cooldown.Cooldown;
import me.invis.bawkbawkswrath.manager.game.SafeBoard;
import me.invis.bawkbawkswrath.manager.notification.SoundNote;
import me.invis.bawkbawkswrath.manager.notification.Title;
import me.invis.bawkbawkswrath.manager.player.PlayersSpread;
import me.invis.bawkbawkswrath.signature.SignatureCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class BawkBawksWrath extends JavaPlugin implements Listener {

    private static BawkBawksWrath instance;
    private static World serverWorld;

    private boolean hasStarted = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        serverWorld = Bukkit.getWorld(getConfig().getString("world"));
        serverWorld.setPVP(false);

        Bukkit.getOnlinePlayers().forEach(player -> player.setGameMode(GameMode.ADVENTURE));
        getCommand("whomadethis").setExecutor(new SignatureCommand());
    }

    public static World getServerWorld() {
        return serverWorld;
    }

    private void start() {
        new PlayersSpread(Bukkit.getServer(), Bukkit.getOnlinePlayers());
        new Cooldown(
                this, 10,
                (currentSecond) -> {
                    new Title((9 - currentSecond) + " seconds before the game starts...", true);
                    if ((9 - currentSecond) <= 3) new SoundNote(.5F);
                },
                () -> {
                    hasStarted = true;

                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.RED + "  - BAWK BAWK IS ANGRY - ");
                    Bukkit.broadcastMessage("");

                    new SafeBoard(1, this);
                }
        );
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        int online = Bukkit.getOnlinePlayers().size();
        event.setJoinMessage(ChatColor.GREEN + event.getPlayer().getDisplayName() + " has joined! (" + online + "/" + getConfig().getInt("min-players-to-start-game") + ")");
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        if(online > getConfig().getInt("min-players-to-start-game") || hasStarted) {
            event.setJoinMessage(event.getPlayer().getDisplayName() + ChatColor.GRAY + " has started spectating the game!");
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        } else if (online == getConfig().getInt("min-players-to-start-game")) {
            Bukkit.broadcastMessage(ChatColor.GRAY + "The game starts in 10 seconds!");
            start();
        }
    }

    public static BawkBawksWrath getInstance() {
        return instance;
    }

}
