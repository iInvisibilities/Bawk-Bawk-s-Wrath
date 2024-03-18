package me.invis.bawkbawkswrath.manager.game;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Random;

public class BoardSpawnPoints {

    private final Block[] result;

    public BoardSpawnPoints(World world, int radius) {
        this.result = new Block[9];
        Block center = getRandomBlock(world, radius);

        int i = 0;
        for (int xMod = -1; xMod <= 1; xMod++) {
            for (int zMod = -1; zMod <= 1; zMod++) {
                result[i] = center.getRelative(xMod, 0, zMod);
                i++;
            }
        }
    }

    public Block[] getResult() {
        return result;
    }

    private int randomInt(Random random, int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public Block getRandomBlock(World world, int radius) {
        Random random = new Random();
        return world.getSpawnLocation().clone().add(randomInt(random, -radius, radius), 0, randomInt(random, -radius, radius)).getBlock();
    }

}
