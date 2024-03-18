package me.invis.bawkbawkswrath.manager.player;

import me.invis.bawkbawkswrath.BawkBawksWrath;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

public class PlayersSpread {

    public PlayersSpread(Server server, Collection<? extends Player> players) {
        Location center = BawkBawksWrath.getServerWorld().getSpawnLocation();

        server.dispatchCommand(
                server.getConsoleSender(),
                "spreadplayers " + center.getX() + " " + center.getZ() + " 2 5 false " + getNames(players)
        );
    }

    private String getNames(Collection<? extends Player> players) {
        return players.stream().map(Player::getName).collect(Collectors.joining(" "));
    }

}
