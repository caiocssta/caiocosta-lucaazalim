package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Dano implements CommandExecutor {
	public Main m;

	public Dano(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("dano")) {
			Player p = (Player) sender;
			if (this.m.perm.isMod(p)) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("on")) {
						this.m.dano = true;
						Bukkit.broadcastMessage(ChatColor.GREEN + "Dano ativado.");
					} else if (args[0].equalsIgnoreCase("off")) {
						this.m.dano = false;
						Bukkit.broadcastMessage(ChatColor.RED + "Dano desativado.");
					} else {
						sender.sendMessage(ChatColor.RED + "Use: /dano on/off");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Use: /dano on/off");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
			}
			return true;
		}
		return false;
	}
}
