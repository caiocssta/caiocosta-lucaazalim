package me.skater.Commands;

import me.skater.Main;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Changelog implements CommandExecutor {
	private Main m;

	public Changelog(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("changelog")) {
			if ((sender instanceof Player)) {
				Player player = (Player) sender;
				if (args.length == 0) {
					if (m.perm.isAdmin(player)) {
						m.getChangelog().getActiveMessages(player);
					} else {
						m.getChangelog().sendChangelogMessage(player);
					}
					return false;
				}
				if (args.length > 0) {
					if (!m.perm.isAdmin(player)) {
						player.sendMessage(ChatColor.RED + "Você não possui permissão.");
						return false;
					}
				}
				if (args[0].equalsIgnoreCase("remove")) {
					m.getChangelog().removeMessage(Integer.parseInt(args[1]));
					player.sendMessage(ChatColor.RED + "Mensagem removida.");

				}

				if (args[0].equalsIgnoreCase("reset")) {
					m.getChangelog().resetChangelog();
					player.sendMessage(ChatColor.RED + "Changelog resetado.");
				}
				if (args[0].equalsIgnoreCase("add")) {
					StringBuilder str = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						str.append(args[i]).append(" ");
					}
					m.getChangelog().addMessage(ChatColor.translateAlternateColorCodes('&', str.toString()));
					player.sendMessage(ChatColor.GREEN + "Mensagem adicionada.");
				}
			}

		}
		return false;
	}
}