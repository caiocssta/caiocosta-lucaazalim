package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Break implements CommandExecutor {
	public Main m;

	public Break(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("break")) {
			Player p = (Player) sender;
			if (this.m.perm.isMod(p)) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("on")) {
						this.m.breakBlocks = true;
						Bukkit.broadcastMessage(ChatColor.GREEN + "Quebramento de blocos ativado.");
					} else if (args[0].equalsIgnoreCase("off")) {
						this.m.breakBlocks = false;
						Bukkit.broadcastMessage(ChatColor.RED + "Quebramento de blocos desativado.");
					} else {
						sender.sendMessage(ChatColor.RED + "Use: /break on/off");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Use: /break on/off");
				}
			} else {
				p.sendMessage(ChatColor.RED + "Você não possui permissão.");
			}
			return true;
		}
		return false;
	}
}
