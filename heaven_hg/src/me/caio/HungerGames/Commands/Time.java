package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Type;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Time implements CommandExecutor {

	public Main m;

	public Time(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("team") | label.equalsIgnoreCase("time")) {
			if (m.type != Type.TEAM) {
				return true;
			}
			if (!m.team.hasTeam(p)) {
				p.sendMessage(ChatColor.RED + "Voce nao esta em um Time");
				return true;
			}
			if (!m.listener.chatTime.contains(p.getUniqueId())) {
				m.listener.chatTime.add(p.getUniqueId());
				if (m.listener.chatTime.contains(p.getUniqueId()))
					p.sendMessage("§aVoce entrou no chat do seu Time");
			} else {
				if (m.listener.chatTime.contains(p.getUniqueId()))
					p.sendMessage("§cVoce saiu do chat do seu Time");
				m.listener.chatTime.remove(p.getUniqueId());
			}

			return true;
		}
		return false;
	}
}