package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Drops implements CommandExecutor {
	public Main m;

	public Drops(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("drops")) {
			Player p = (Player) sender;
			if (this.m.perm.isMod(p)) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("on")) {
						this.m.drops = true;
				//		Bukkit.broadcastMessage(ChatColor.GREEN.toString() + "Drop de itens ativado.");
						p.sendMessage(ChatColor.GREEN.toString() + "Drop de itens ativado.");
					} else if (args[0].equalsIgnoreCase("off")) {
						this.m.drops = false;
						//	Bukkit.broadcastMessage(ChatColor.RED.toString() + "Drop de itens desativado.");
						p.sendMessage(ChatColor.RED.toString() + "Drop de itens desativado.");
					} else {
						sender.sendMessage(ChatColor.RED + "Use: /toggledrops on/off)");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Use: /toggledrops on/off)");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
			}
			return true;
		}
		return false;
	}
}
