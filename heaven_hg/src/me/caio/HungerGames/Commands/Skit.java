package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Managers.SimpleKit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Skit implements CommandExecutor {
	public Main m;

	public Skit(Main m) {
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
		if (label.equalsIgnoreCase("skit")) {
			if (args.length > 0) {
				if (args.length == 2) {
					String kit = args[1];
					if (args[0].equalsIgnoreCase("create")) {
						SimpleKit.addKit(p, kit, new SimpleKit(p));
						return true;
					}
					if (args[0].equalsIgnoreCase("apply")) {
						SimpleKit.applyKit(p, kit, null);
						return true;
					}
					if (args[0].equalsIgnoreCase("remove")) {
						SimpleKit.removeKit(p, kit);
						return true;
					}
				}
				if (args.length == 3) {
					Player target = Bukkit.getPlayer(args[2]);
					if (target == null) {
						p.sendMessage(ChatColor.RED + "Player offline.");
						return true;
					}
					if (args[0].equalsIgnoreCase("apply")) {
						String kit = args[1];
						SimpleKit.applyKit(p, kit, target);
						return true;
					}
				}
			}
			p.sendMessage(ChatColor.RED + "Use: /skit (create | apply | remove) <kit> (player)");
			return true;
		}
		return false;
	}
}
