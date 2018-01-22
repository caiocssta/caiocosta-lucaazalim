package me.skater.multiplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.skater.Main;
import me.skater.Management;

import org.bukkit.scheduler.BukkitRunnable;

public class MultiplierManager extends Management {

	public MultiplierManager(Main main) {
		super(main);
	}

	public int multiplier = 1;

	@Override
	public void onEnable() {
		getPlugin().getCommand("multiplier").setExecutor(new MultiplierCommand(this));
		loadMultiplier();

	}

	@Override
	public void onDisable() {

	}

	public int getActiveMultiplier() {
		return this.multiplier;
	}

	public void changeActiveMultiplier(int i) throws SQLException {
		this.multiplier = i;
		PreparedStatement stmt = getPlugin().mainConnection.prepareStatement("SELECT * FROM `multiplier`;");
		ResultSet result = stmt.executeQuery();
		if (result.next()) {
			stmt.execute("UPDATE `multiplier` SET multiplier='" + i + "';");
		} else {
			stmt.execute("INSERT INTO `multiplier`(`multiplier`) VALUES ('" + i + "')");
		}
		result.close();
		stmt.close();
	}

	public void loadMultiplier() {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					PreparedStatement sql = getPlugin().mainConnection.prepareStatement("SELECT * FROM `multiplier`;");
					ResultSet result = sql.executeQuery();
					if (result.next()) {
						multiplier = result.getInt("multiplier");
					}
					result.close();
					sql.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro ao carregar multiplier");
				}
			}
		}.runTaskAsynchronously(getPlugin());
	}

}
