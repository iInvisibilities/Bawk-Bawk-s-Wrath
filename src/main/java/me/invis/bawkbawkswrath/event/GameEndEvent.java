package me.invis.bawkbawkswrath.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    private final Player winner;

    private final int rounds;

    /*
     * @param winner if all players lost the winner will be null
     */
    public GameEndEvent(Player winner, int rounds) {
        this.winner = winner;
        this.rounds = rounds;
    }

    public Player getWinner() {
        return winner;
    }

    public int getRounds() {
        return rounds;
    }
}
