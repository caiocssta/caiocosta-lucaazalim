package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Build implements CommandExecutor {
	public Main m;

	public Build(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("build")) {
			Player p = (Player) sender;
			if (this.m.perm.isMod(p)) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("on")) {
						this.m.breakBlocks = true;
						this.m.placeBlocks = true;
						Bukkit.broadcastMessage(ChatColor.GREEN + "Construção de blocos ativada.");
					} else if (args[0].equalsIgnoreCase("off")) {
						this.m.breakBlocks = false;
						this.m.placeBlocks = false;
						Bukkit.broadcastMessage(ChatColor.RED + "Construção de blocos desativada.");
					} else {
						sender.sendMessage(ChatColor.RED + "Use: /build on/off");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Use: /build on/off");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
			}
			return true;
		}
		return false;
	}
}
