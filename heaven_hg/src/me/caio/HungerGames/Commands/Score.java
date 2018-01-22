package me.caio.HungerGames.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class Score implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName().equalsIgnoreCase("main")) {
			p.getScoreboard().getObjective("clear").setDisplaySlot(DisplaySlot.SIDEBAR);
			p.sendMessage(ChatColor.YELLOW + "Você desativou a scoreboard, para ativar novamente use: /score");
		} else {
			p.getScoreboard().getObjective("main").setDisplaySlot(DisplaySlot.SIDEBAR);
			p.sendMessage(ChatColor.YELLOW + "Você ativou a scoreboard, para desativar use: /score");
		}
		return false;
	}
}
