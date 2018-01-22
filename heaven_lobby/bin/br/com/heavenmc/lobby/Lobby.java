package br.com.wombocraft.lobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import br.com.wombocraft.lobby.nms.packets.Injector;
import br.com.wombocraft.lobby.utils.json.JSONException;
import br.com.wombocraft.lobby.utils.json.JSONObject;
import net.minecraft.util.com.google.common.io.ByteArrayDataInput;
import net.minecraft.util.com.google.common.io.ByteStreams;

public class Lobby extends JavaPlugin implements PluginMessageListener {

	private static Lobby _lobby;
	private static LobbyManager _lobbyManager;

	public void onLoad() {
		Injector.inject();
	}

	public void onEnable() {
		_lobby = this;
		_lobbyManager = new LobbyManager(_lobby);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}

	public void onDisable() {
		getLobbyManager().getMysqlManager().getMysqlConnection().SQLdisconnect();
		getLobbyManager().getPermissionManager().disable();
	}

	public static LobbyManager getLobbyManager() {
		return _lobbyManager;
	}

	public static Lobby getLobby() {
		return _lobby;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player arg1, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (!subchannel.equals("players")) {
			return;
		}
		String data = in.readUTF();
		try {
			JSONObject j = new JSONObject(data);
			getLobbyManager().getServerManager().setPvPPlayers(j.getInt("PVP"));
			getLobbyManager().getServerManager().setHGPlayers(j.getInt("HG"));
			getLobbyManager().getServerManager().setTHGPlayers(j.getInt("THG"));
			getLobbyManager().getServerManager().setLobbyPlayers(j.getInt("LB"));
			getLobbyManager().getServerManager().setLobbyPlayers(j.getInt("UHG"));
			getLobbyManager().getServerManager().updateTotalPlayer();
			getLobbyManager().getScoreboardManager().updateOnlinePlayer();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
