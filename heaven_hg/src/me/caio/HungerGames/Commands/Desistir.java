package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Desistir implements CommandExecutor {
	private Main m;

	public Desistir(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("desistir")) {
			if (this.m.stage == GameStage.PREGAME) {
				p.sendMessage(ChatColor.RED + "Você não pode desistir de uma batalha que não iniciou.");
				return true;
			}
			if (this.m.stage == GameStage.WINNER) {
				return true;
			}
			if (m.listener.desistiu.contains(p.getUniqueId())) {
				return false;
			}
			if (p.getGameMode() == GameMode.CREATIVE) {
				p.setGameMode(GameMode.SURVIVAL);
			}
			m.listener.desistiu.add(p.getUniqueId());
			p.damage(9999);
			return true;
		}
		return false;
	}
}
