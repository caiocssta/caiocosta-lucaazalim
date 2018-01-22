package me.skater.Commands;

import java.util.ArrayList;

import me.skater.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Staff implements CommandExecutor {
	private Main m;

	public Staff(Main m) {
		this.m = m;
	}

	public static ArrayList<String> chatStaff = new ArrayList<String>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Voce nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("staff")) {
			if (!this.m.perm.isTrial(p)) {
				p.sendMessage(ChatColor.RED + "Voce nao possui permissao.");
				return true;
			}
			if (m.getClanManager().isOnClanChat(p)) {
				p.sendMessage(ChatColor.RED + "Você não pode usar o chat da staff enquanto estiver no chat do clan.");
				return true;

			}
			if (!chatStaff.contains(p.getName())) {
				chatStaff.add(p.getName());
				if (chatStaff.contains(p.getName()))
					p.sendMessage("§aVoce entrou no chat da staff");
			} else {
				if (chatStaff.contains(p.getName()))
					p.sendMessage("§cVoce saiu do chat da staff");
				chatStaff.remove(p.getName());
			}

			return true;
		}
		return false;
	}
}