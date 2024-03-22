package me.invis.bawkbawkswrath.manager.game;

import me.invis.bawkbawkswrath.BawkBawksWrath;
import me.invis.bawkbawkswrath.event.GameEndEvent;
import me.invis.bawkbawkswrath.manager.cooldown.Cooldown;
import me.invis.bawkbawkswrath.manager.notification.SoundNote;
import me.invis.bawkbawkswrath.manager.notification.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.invis.bawkbawkswrath.BawkBawksWrath.PVPRound;

public class SafeBoard {

    private final static int radius = BawkBawksWrath.getInstance().getConfig().getInt("radius", 15);

    private final List<Block> safeBlocks;

    public SafeBoard(int currentRound, Plugin plugin) {
        Bukkit.broadcastMessage(ChatColor.GREEN + "RUN TO THE SAFE BOARD!");
        new SoundNote(1);
        if(currentRound == PVPRound) {
            new SoundNote(.1F);
            Bukkit.broadcastMessage(ChatColor.RED + "PVP IS ENABLED!");
            BawkBawksWrath.getServerWorld().setPVP(true);
        }

        safeBlocks = new ArrayList<>();
        Block[] safeBoardBlocks = new BoardSpawnPoints(
                BawkBawksWrath.getServerWorld(), radius).getResult();
        for (Block safeBoardBlock : safeBoardBlocks) {
            safeBoardBlock.setType(Material.WOOL);
            BlockState state = safeBoardBlock.getState();
            Wool woolData = (Wool) state.getData();
            woolData.setColor(DyeColor.RED);
            state.setData(woolData);
            state.update();

            safeBoardBlock.getLocation().clone().add(0, 4, 0).getBlock().setType(Material.GLASS);

            safeBlocks.add(safeBoardBlock);
        }

        new Cooldown(
                plugin, 8,
                (currentSecond) -> {
                    new Title((7 - currentSecond) + " seconds...", true);
                    if ((7 - currentSecond) <= 3) new SoundNote(.5F);
                },
                () -> {
                    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                        if(!isOnSafeBoard(onlinePlayer)) {
                            BawkBawksWrath.getServerWorld().strikeLightningEffect(onlinePlayer.getLocation());
                            onlinePlayer.setGameMode(GameMode.SPECTATOR);
                        }
                    });

                    boolean isGameOn = true;

                    List<? extends Player> winner = Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).collect(Collectors.toList());
                    if(winner.size() <= 1) {
                        Player w = null;
                        if(winner.size() == 1) {
                            w = winner.get(0);
                            w.sendTitle(ChatColor.GREEN + "You win!", ChatColor.GRAY + "Congratulations!");
                        }
                        Bukkit.getPluginManager().callEvent(
                                new GameEndEvent(w, currentRound)
                        );

                        isGameOn = false;
                        Bukkit.broadcastMessage(ChatColor.RED + "SERVER IS GOING TO RESTART IN 15 SECONDS...");
                        serverRestart(plugin);
                    }

                    safeBlocks.forEach(block -> {
                        block.getLocation().clone().add(0, 4, 0).getBlock().setType(Material.AIR);
                        block.setType(Material.AIR);
                    });
                    safeBlocks.clear();

                    if (isGameOn) new SafeBoard(currentRound+1, plugin);
                }
        );
    }

    private void serverRestart(Plugin plugin) {
        new BukkitRunnable() {

            @Override
            public void run() {
                plugin.getServer().spigot().restart();
            }

        }.runTaskLater(plugin, 300);
    }

    public boolean isOnSafeBoard(Player player) {
        return safeBlocks.stream().map(b -> getXZ(b.getLocation())).anyMatch(l -> l.equals(getXZ(player.getLocation())));
    }

    private Map.Entry<Integer, Integer> getXZ(Location location) {
        return new AbstractMap.SimpleEntry<>(location.getBlock().getX(), location.getBlock().getZ());
    }

}
