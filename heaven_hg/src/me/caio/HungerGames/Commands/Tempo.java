package me.caio.HungerGames.Commands;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Utils.DateUtils;
import me.caio.HungerGames.Utils.Enum.GameStage;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tempo implements CommandExecutor {
	public Main m;

	public Tempo(Main m) {
		this.m = m;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Apenas jogadores.");
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("tempo")) {
			if (!this.m.perm.isMod(p)) {
				p.sendMessage(ChatColor.RED + "Você não possui permissão.");
				return true;
			}
			if (args.length == 0) {
				p.sendMessage(ChatColor.RED + "Use: /tempo <tempo>");
				return true;
			}
			long time;
			try {
				time = DateUtils.parseDateDiff(args[0], true);
			} catch (Exception e) {
				p.sendMessage(ChatColor.RED + "Formato invalido.");
				return true;
			}
			int seconds = (int) Math.floor((time - System.currentTimeMillis()) / 1000L);
			if (this.m.stage == GameStage.PREGAME) {
				this.m.PreGameTimer = seconds;
				p.sendMessage(ChatColor.RED + "Tempo de pregame mudado para " + this.m.getTime(Integer.valueOf(seconds)));
				return true;
			}
			if (this.m.stage == GameStage.INVENCIBILITY) {
				this.m.Invenci = Integer.valueOf(seconds);
				p.sendMessage(ChatColor.RED + "Tempo de invencibilidade mudado para " + this.m.getTime(Integer.valueOf(seconds)));
				return true;
			}
			if (this.m.stage == GameStage.GAMETIME) {
				this.m.GameTimer = seconds;
				p.sendMessage(ChatColor.RED + "Tempo de jogo mudado para " + this.m.getTime(Integer.valueOf(seconds)));
				return true;
			}
		}
		return false;
	}
}
