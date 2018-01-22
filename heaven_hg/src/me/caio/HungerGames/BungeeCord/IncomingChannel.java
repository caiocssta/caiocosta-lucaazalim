package me.caio.HungerGames.BungeeCord;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class IncomingChannel implements PluginMessageListener {
	private me.caio.HungerGames.Main main;

	public IncomingChannel(me.caio.HungerGames.Main main) {
		this.main = main;
	}

	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("GetServer")) {
			String servername = in.readUTF();
			this.main.ipAddress = servername;
			for (Player p : Bukkit.getOnlinePlayers()) {
				this.main.sendTabToPlayer(p);
				main.getScoreboardManager().updateIp();
			}
		}
	}
}
