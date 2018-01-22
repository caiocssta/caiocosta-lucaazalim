package me.skater.Managers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.skater.Main;
import me.skater.Utils.ClassGetter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {
	private Main m;

	public CommandManager(Main m) {
		this.m = m;
		loadCommands(m, "me.skater.Commands");
	}

	private void loadCommands(JavaPlugin plugin, String packageName) {
		try {
			for (Class<?> commandClass : ClassGetter.getClassesForPackage(this.m, packageName)) {
				if (CommandExecutor.class.isAssignableFrom(commandClass)) {
					try {
						CommandExecutor commandListener = null;
						try {
							Constructor<?> con = commandClass.getConstructor(new Class[] { Main.class });
							commandListener = (CommandExecutor) con.newInstance(new Object[] { plugin });
						} catch (Exception ex) {
							commandListener = (CommandExecutor) commandClass.newInstance();
						}
						loadCommand(plugin, commandListener);
					} catch (Exception e) {
						System.out.print("Erro ao carregar o comando " + commandClass.getSimpleName());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadCommand(JavaPlugin owningPlugin, CommandExecutor exc) throws Exception {
		registerCommand(exc.getClass().getSimpleName(), exc);
	}

	private void registerCommand(String name, CommandExecutor exc) throws Exception {
		PluginCommand command = Bukkit.getServer().getPluginCommand(name.toLowerCase());
		if (command == null) {
			Constructor<?> constructor = PluginCommand.class.getDeclaredConstructor(new Class[] { String.class, Plugin.class });
			constructor.setAccessible(true);
			command = (PluginCommand) constructor.newInstance(new Object[] { name, this.m });
		}
		command.setExecutor(exc);
		List<String> list;
		try {
			Field field = exc.getClass().getDeclaredField("aliases");
			if ((field.get(exc) instanceof String[])) {
				list = Arrays.asList((String[]) field.get(exc));
				command.setAliases(list);
			}
		} catch (Exception localException) {
		}
		if (command.getAliases() != null) {
			for (String alias : command.getAliases()) {
				unregisterCommand(alias);
			}
		}
		try {
			Field field = exc.getClass().getDeclaredField("description");
			if ((field != null) && ((field.get(exc) instanceof String))) {
				command.setDescription(ChatColor.translateAlternateColorCodes('&', (String) field.get(exc)));
			}
		} catch (Exception localException1) {
		}
		this.m.rm.getCommandMap().register(name, command);
	}

	@SuppressWarnings("unchecked")
	private void unregisterCommand(String name) {
		try {
			Field known = SimpleCommandMap.class.getDeclaredField("knownCommands");
			Field alias = SimpleCommandMap.class.getDeclaredField("aliases");
			known.setAccessible(true);
			alias.setAccessible(true);
			Map<String, Command> knownCommands = (Map<String, Command>) known.get(this.m.rm.getCommandMap());
			Set<String> aliases = (Set<String>) alias.get(this.m.rm.getCommandMap());
			knownCommands.remove(name.toLowerCase());
			aliases.remove(name.toLowerCase());
		} catch (Exception localException) {
		}
	}
}
