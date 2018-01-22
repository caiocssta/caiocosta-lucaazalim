package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Forcekit implements CommandExecutor {
	public Main m;

	public Forcekit(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce tem que ser um Player");
		}
		Player p = (Player) sender;
		if (!this.m.perm.isMod(p)) {
			sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
			return true;
		}
		if (label.equalsIgnoreCase("forcekit")) {
			if ((args.length > 0) && (args.length == 2)) {
				String player = args[0];
				String kitName = args[1];
				if (player.equalsIgnoreCase("all")) {
					if (kitName.equalsIgnoreCase("none")) {
						this.m.kit.removePlayerKitAll();
						Bukkit.broadcastMessage(ChatColor.GREEN + "Todos os kits foram resetados.");
						return true;
					}
					if (!this.m.kit.isKit(kitName)) {
						p.sendMessage(ChatColor.RED + "Kit invalido.");
						return true;
					}
					this.m.kit.setForcedKitAll(kitName);
					Bukkit.broadcastMessage(ChatColor.GREEN + "O Kit " + kitName.toUpperCase() + " foi aplicado em todos.");
					return true;
				}
				if (kitName.equalsIgnoreCase("none")) {
					Player targetPlayer = this.m.getServer().getPlayer(player);
					if (targetPlayer == null) {
						p.sendMessage(ChatColor.RED + "Jogador offline.");
						return true;
					}
					this.m.kit.removePlayerKit(targetPlayer);
					p.sendMessage(ChatColor.GREEN + "Você removeu o kit de " + targetPlayer.getName());
					targetPlayer.sendMessage(ChatColor.RED + "Seu kit foi removido por " + p.getName());
					return true;
				}
				Player targetPlayer = this.m.getServer().getPlayer(player);
				if (targetPlayer == null) {
					p.sendMessage(ChatColor.RED + "Jogador offline.");
					return true;
				}
				if (!this.m.kit.isKit(kitName)) {
					p.sendMessage(ChatColor.RED + "Kit invalido.");
					return true;
				}
				this.m.kit.setForcedKit(targetPlayer, kitName);
				p.sendMessage(ChatColor.GREEN + "Você aplicou o kit " + this.m.kit.getKitName(kitName) + " em " + targetPlayer.getName());
				targetPlayer.sendMessage(ChatColor.GREEN + p.getName() + " aplicou o kit " + this.m.kit.getKitName(kitName) + " em você.");
				return true;
			}
			p.sendMessage(ChatColor.RED + "Use: /forcekit player/all kit/none");
			return true;
		}
		return false;
	}
}
