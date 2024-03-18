package me.invis.bawkbawkswrath.signature;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SignatureCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(new String[] {
                ChatColor.GREEN + "This plugin has been made by Invisibilities",
                ChatColor.GRAY + "   Make sure to check out my github at https://github.com/iInvisibilities!"
        });
        return true;
    }
}
