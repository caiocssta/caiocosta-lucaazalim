package me.caio.bungeecord.commands;

import me.caio.bungeecord.Main;
import me.caio.bungeecord.socket.ServerInfo;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Lobby extends Command {
	private Main m;

	public Lobby(Main m) {
		super("lobby");
		this.m = m;
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		ServerInfo lobby = m.getLobby();
		if (lobby == null) {
			player.sendMessage(ChatColor.RED + "Ops... Parece que não há nenhum lobby disponível!");
			return;
		}
		player.connect(m.getProxy().getServerInfo(lobby.getName()));
	}
}
