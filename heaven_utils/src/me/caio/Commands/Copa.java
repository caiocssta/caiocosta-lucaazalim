package me.skater.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.caio.HungerGames.Main;

public class Copa implements CommandExecutor {
	@SuppressWarnings("unused")
	private Main m;

	public Copa(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		/*if (label.equalsIgnoreCase("copa")) {
			if ((sender instanceof Player) && !this.m.perm.isAdmin((Player) sender)) {
				sender.sendMessage(ChatColor.RED + "Você não possui permissão.");
				return false;
			}
			String playerid = args[0];
			Player p = Bukkit.getPlayer(playerid);
			if (p != null) {
				playerid = p.getUniqueId().toString();
			} else {
				if (!isValidMD5(playerid)) {
					sender.sendMessage("§cUUID invalido: " + playerid);
					return false;
				}
			}
			final String g = args[1];
			final String tuuid = playerid.replaceAll("-", "");
			new BukkitRunnable() {
				@Override
				public void run() {
					try {
						String query = "INSERT INTO `copahg`(`uuid`, `grupo`) VALUES ('" + tuuid + "','" + g.toUpperCase() + "');";
						PreparedStatement stmt = me.skater.Main.getInstance().mainConnection.prepareStatement(query);
						stmt.executeUpdate();
						stmt.close();
						sender.sendMessage("§aVocê adiciou um ingresso para §b" + tuuid + " §ano grupo §b" + g.toUpperCase());
					} catch (SQLException ex) {
						System.out.println("Erro ao setar grupo de " + tuuid);
						ex.printStackTrace();
					}
				}
			}.runTaskAsynchronously(JavaPlugin.getPlugin(Main.class));
		}*/
		return false;
	}
	
	public boolean isValidMD5(String s) {
		return s.matches("[a-fA-F0-9]{32}");
	}
}
