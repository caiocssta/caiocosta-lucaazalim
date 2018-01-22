package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Type;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kit implements CommandExecutor {
	private Main m;

	public Kit(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("kit")) {
			if (args.length == 0) {
				p.sendMessage(ChatColor.RED + "Use /kit <nome-do-kit>");
				return false;
			}
			
			if (m.adm.isSpectating(p)) {
				p.sendMessage(ChatColor.RED + "Você não pode escolher kit no modo espectador.");
				return false;
			}

			if (this.m.type == Type.ULTRA) {
				m.kit.setKit(p, args[0]);
				return false;
			}

			if (m.kit.kit1.containsKey(p.getUniqueId()) && m.kit.kit2.containsKey(p.getUniqueId())) {
				p.sendMessage(ChatColor.RED + "Você ja escolheu seus 2 kits.");
				return false;
			}
			
			if (m.kit.kit1.containsKey(p.getUniqueId())) {
				m.kit.setTeamKit(p, args[0], true);
			} else {
				m.kit.setTeamKit(p, args[0], false);
			}
		}
		return false;
	}
}
