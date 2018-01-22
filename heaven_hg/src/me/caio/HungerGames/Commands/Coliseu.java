package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Coliseu implements CommandExecutor {
	public Main m;

	public Coliseu(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("coliseu")) {
			Player p = (Player) sender;
			if (this.m.perm.isAdmin(p)) {
				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Use: /coliseu spawn/destroy");
					return true;
				}
				String comando = args[0];
				if (comando.equalsIgnoreCase("spawn")) {
				//	this.m.coliseumManager.spawnColiseum();
				//	p.sendMessage(ChatColor.GREEN + "Coliseu spawnado");
				//	return true;
				}
				if (comando.equalsIgnoreCase("destroy")) {
				//	this.m.coliseumManager.destroyColiseum();
				//	p.sendMessage(ChatColor.RED + "Coliseu destruido");
				//	return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
			}
			return true;
		}
		return false;
	}
}
