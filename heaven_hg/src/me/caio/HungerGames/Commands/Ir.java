package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ir implements CommandExecutor {
	public Main m;

	public Ir(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ir")) {
			Player p = (Player) sender;
			if (!this.m.adm.isSpectating(p)) {
				p.sendMessage(ChatColor.RED + "Comando apenas para espectadores.");
				return true;
			}
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "Use: /ir player");
			} else if (args.length == 1) {
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				if (targetPlayer == null) {
					p.sendMessage(ChatColor.RED + "Jogador offline.");
					return true;
				}
				player.teleport(targetPlayer);
			}
		}
		return false;
	}
}
