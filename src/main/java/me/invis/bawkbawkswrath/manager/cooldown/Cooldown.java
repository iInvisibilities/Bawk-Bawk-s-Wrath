package me.invis.bawkbawkswrath.manager.cooldown;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class Cooldown {

    public Cooldown(Plugin plugin, int seconds, Consumer<Integer> eachSecond, Runnable onceDone) {
        new BukkitRunnable() {
            int timer = 1;
            @Override
            public void run() {
                eachSecond.accept(timer);

                timer++;

                if (timer >= seconds) {
                    onceDone.run();
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0, 20);
    }

}
