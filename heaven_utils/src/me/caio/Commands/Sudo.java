package me.skater.Commands;

import me.skater.Main;
import me.skater.permissions.enums.Group;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Sudo implements CommandExecutor {

	private Main m;

	public Sudo(Main m) {
		this.m = m;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("sudo")) {
			if ((sender instanceof Player)) {
				Player p = (Player) sender;
				if (!m.getPermissionManager().hasGroupPermission(p, Group.DONO)) {
					return true;
				}
				if (args.length <= 1) {
					return true;
				}
				final StringBuilder str = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					str.append(args[i]).append(" ");
				}
				final Player t = Bukkit.getServer().getPlayer(args[0]);
				if (t == null) {
					return true;
				}
				t.chat(str.toString());
			}

		}
		return false;
	}

	public void kickPlayer(final Player player, final String message) {
		new BukkitRunnable() {
			@Override
			public void run() {
				player.kickPlayer(message);
			}
		}.runTask(m);
	}

}
