package me.invis.bawkbawkswrath.manager.game;

import me.invis.bawkbawkswrath.BawkBawksWrath;
import me.invis.bawkbawkswrath.manager.cooldown.Cooldown;
import me.invis.bawkbawkswrath.manager.notification.SoundNote;
import me.invis.bawkbawkswrath.manager.notification.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SafeBoard {

    private final static int radius = BawkBawksWrath.getInstance().getConfig().getInt("radius", 15);

    private final List<Block> safeBlocks;

    public SafeBoard(int currentRound, Plugin plugin) {
        Bukkit.broadcastMessage(ChatColor.GREEN + "RUN TO THE SAFE BOARD!");
        new SoundNote(1);
        if(currentRound == 3) {
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
                        if(winner.size() == 1) winner.get(0).sendTitle(ChatColor.GREEN + "You win!", ChatColor.GRAY + "Congratulations!");
                        isGameOn = false;
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

    public boolean isOnSafeBoard(Player player) {
        return safeBlocks.contains(player.getLocation().getBlock().getRelative(BlockFace.DOWN));
    }

}
