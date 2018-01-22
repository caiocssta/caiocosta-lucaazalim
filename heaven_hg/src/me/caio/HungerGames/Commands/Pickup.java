package me.caio.HungerGames.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.caio.HungerGames.Main;

public class Pickup implements CommandExecutor {
	public Main m;

	public Pickup(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("pickup")) {
			Player p = (Player) sender;
			if (this.m.perm.isMod(p)) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("on")) {
						this.m.pickupItems = true;
						p.sendMessage(ChatColor.GREEN + "Pegar drops ativado.");
					} else if (args[0].equalsIgnoreCase("off")) {
						this.m.pickupItems = false;
						p.sendMessage(ChatColor.RED + "Pegar drops desativado.");
					} else {
						sender.sendMessage(ChatColor.RED + "Use: /pickup on/off");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Use: /pickup on/off");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
			}
			return true;
		}
		return false;
	}
}
