package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Togglekit implements CommandExecutor {
	public Main m;

	public Togglekit(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("togglekit")) {
			Player p = (Player) sender;
			if (this.m.perm.isMod(p)) {
				if (args.length == 2) {
					String kitName = args[0];
					if (kitName.equalsIgnoreCase("all")) {
						if (args[1].equalsIgnoreCase("on")) {
							this.m.kit.enableKitAll();
							Bukkit.broadcastMessage(ChatColor.GREEN + "Todos os kits foram ativados.");
						} else if (args[1].equalsIgnoreCase("off")) {
							this.m.kit.disableKitAll();
							Bukkit.broadcastMessage(ChatColor.RED + "Todos os kits foram desativados.");
						}
					} else {
						if (!this.m.kit.isKit(kitName)) {
							p.sendMessage(ChatColor.RED + "Kit invalido.");
							return true;
						}
						kitName = this.m.kit.getKitName(kitName);
						if (args[1].equalsIgnoreCase("on")) {
							this.m.kit.enableKit(kitName.toLowerCase());
							Bukkit.broadcastMessage(ChatColor.GREEN.toString() + "O Kit " + kitName + " foi ativado!");
						} else if (args[1].equalsIgnoreCase("off")) {
							this.m.kit.disableKit(kitName.toLowerCase());
							Bukkit.broadcastMessage(ChatColor.RED.toString() + "O Kit " + kitName + " foi desativado!");
						}
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Use: /kitsability kit/todos on/off");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
			}
			return true;
		}
		return false;
	}
}
