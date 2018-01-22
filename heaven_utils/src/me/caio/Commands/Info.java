package me.skater.Commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.skater.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Info implements CommandExecutor {
	public Main m;

	public Info(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("info")) {
			final Player p = (Player) sender;
			if (!this.m.perm.isTrial(p)) {
				p.sendMessage(ChatColor.RED + "Voce nao possui permissao");
				return true;
			}
			if (args.length == 0) {
				new BukkitRunnable() {

					@Override
					public void run() {
						try {
							p.sendMessage(getInfo(p.getName()));
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}.runTaskAsynchronously(m);
				return true;
			} else if (args.length == 1) {
				new BukkitRunnable() {

					@Override
					public void run() {
						try {
							p.sendMessage(getInfo(args[0]));
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}.runTaskAsynchronously(m);
				return true;
			}
		}
		return false;
	}

	private String getInfo(String name) throws SQLException {
		StringBuilder string = new StringBuilder();
		if (m.name.getUUID(name) == null) {
			return "§cO player " + name + " não existe.";
		}
		string.append("§f§m---------------------");
		string.append("\n");
		String uuid = m.name.getUUID(name);
		string.append("§6• §fNick§8: §e" + name);
		string.append("\n");
		string.append("§6• §fUUID§8: §e" + uuid);
		string.append("\n");
		string.append(" ");
		string.append("\n");
		string.append("§6• §6§lCompras§8:");
		string.append("\n");
		PreparedStatement stmt = m.mainConnection.prepareStatement("SELECT * FROM wb_loja.iw4_game_account_package WHERE player='" + uuid + "';");
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			string.append("§7- §e" + result.getString("name") + " §c[" + result.getString("date_added").substring(0, 10) + "]\n");
		}
		stmt.close();
		result.close();
		string.append("\n");
		string.append("§f§m---------------------");
		return string.toString();
	}
}
