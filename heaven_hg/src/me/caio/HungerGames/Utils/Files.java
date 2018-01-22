package me.caio.HungerGames.Utils;

import java.io.File;

import me.caio.HungerGames.Main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Files {
	private Main main;
	public FileConfiguration kit;

	public Files(Main m) {
		this.main = m;
		File kitFile = new File(this.main.getDataFolder(), "kit.yml");
		File dataFolder = this.main.getDataFolder();
		if (!dataFolder.exists()) {
			dataFolder.mkdir();
		}
		if (!kitFile.exists()) {
			kitFile.getParentFile().mkdirs();
			this.main.copy(this.main.getResource("kit.yml"), kitFile);
		}
		this.kit = YamlConfiguration.loadConfiguration(new File(this.main.getDataFolder(), "kit.yml"));
	}
}
