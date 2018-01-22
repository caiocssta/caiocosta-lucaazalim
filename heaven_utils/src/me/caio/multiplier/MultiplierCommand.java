package me.skater.multiplier;

import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MultiplierCommand implements CommandExecutor {
	private MultiplierManager manager;
	private ReentrantLock lock;

	public MultiplierCommand(MultiplierManager manager) {
		this.manager = manager;
		this.lock = new ReentrantLock();

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		final Player p = (Player) sender;
		if (label.equalsIgnoreCase("multiplier")) {
			if (manager.getPlugin().getPermissions().isAdmin(p)) {
				if (args.length < 1) {
					p.sendMessage(ChatColor.GREEN + "Multiplicador atual: " + ChatColor.YELLOW + manager.getActiveMultiplier());
					p.sendMessage(ChatColor.RED + "Use /multiplier <numero> para mudar.");
					return false;
				}
				final Integer i;
				try {
					i = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					p.sendMessage(ChatColor.RED + "Apenas numeros.");
					return false;
				}
				this.lock.lock();
				new BukkitRunnable() {
					@Override
					public void run() {
						try {
							manager.changeActiveMultiplier(i);
							p.sendMessage(ChatColor.GREEN + "Multiplicador alterado para: " + ChatColor.YELLOW + i + "X");
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "Falha em alterar multiplicador.");
							e.printStackTrace();
						} finally {
							new BukkitRunnable() {
								@Override
								public void run() {
									lock.unlock();
								}
							}.runTask(manager.getPlugin());
						}
					}
				}.runTaskAsynchronously(this.manager.getPlugin());
				return true;
			}

		}
		return false;
	}
}