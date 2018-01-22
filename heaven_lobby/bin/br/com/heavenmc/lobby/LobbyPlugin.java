package br.com.wombocraft.lobby;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import br.com.wombocraft.lobby.comando.ComandoManager;
import br.com.wombocraft.lobby.comando.IComando;
import br.com.wombocraft.lobby.mysql.MysqlManager;
import br.com.wombocraft.lobby.utils.data.NautHashMap;
import br.com.wombocraft.lobby.utils.time.UtilTime;

public class LobbyPlugin {

	protected JavaPlugin _plugin;
	protected NautHashMap<String, IComando> _commands;
	protected String _nomeModulo = "Default";
	protected LobbyManager _lbm;

	public LobbyPlugin(String modulo, LobbyManager lbm) {
		this._plugin = Lobby.getPlugin(Lobby.class);
		this._commands = new NautHashMap<>();
		this._nomeModulo = modulo;
		this._lbm = lbm;

		onEnable();
	}

	public PluginManager GetPluginManager() {
		return this._plugin.getServer().getPluginManager();
	}

	public BukkitScheduler GetScheduler() {
		return this._plugin.getServer().getScheduler();
	}

	public JavaPlugin GetPlugin() {
		return this._plugin;
	}

	public void RegisterEvents(Listener listener) {
		this._plugin.getServer().getPluginManager().registerEvents(listener, this._plugin);
	}

	public final void onEnable() {
		long epoch = System.currentTimeMillis();
		Log("Iniciando...");
		Enable();
		AddCommands();
		Log("Iniciando em " + UtilTime.convertString(System.currentTimeMillis() - epoch, 1, UtilTime.TimeUnit.FIT)
				+ ".");
	}

	public final void onDisable() {
		Disable();

		Log("Desabilitado.");
	}

	public void Enable() {

	}

	public void Disable() {
	}

	public LobbyManager getLobbyManager() {
		return this._lbm;
	}

	public MysqlManager getMysqlManager() {
		return this._lbm.getMysqlManager();
	}

	public void AddCommands() {
	}

	public final void AddCommand(IComando command) {
		ComandoManager.inicializar(this._lbm).addComando(command);
	}

	public final void RemoveCommand(IComando command) {
		ComandoManager.inicializar(this._lbm).removerComando(command);
	}

	protected void Log(String message) {
		System.out.println(this._nomeModulo + " > " + message);
	}

	public final String GetName() {
		return this._nomeModulo;
	}

}
