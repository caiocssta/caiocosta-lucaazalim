package br.com.wombocraft.lobby.rank.injector.regexperms;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.PluginManager;

import com.google.common.collect.Sets;

import br.com.wombocraft.lobby.Lobby;
import br.com.wombocraft.lobby.rank.injector.FieldReplacer;

@SuppressWarnings({ "unchecked", "rawtypes" })

public class PEXPermissionSubscriptionMap extends HashMap<String, Map<Permissible, Boolean>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3815816386187051557L;
	private static FieldReplacer<PluginManager, Map> INJECTOR;
	private static final AtomicReference<PEXPermissionSubscriptionMap> INSTANCE = new AtomicReference();
	private final Lobby plugin;
	private final PluginManager manager;

	private PEXPermissionSubscriptionMap(Lobby plugin, PluginManager manager,
			Map<String, Map<Permissible, Boolean>> backing) {
		super(backing);
		this.plugin = plugin;
		this.manager = manager;
	}

	public static PEXPermissionSubscriptionMap inject(Lobby plugin, PluginManager manager) {
		PEXPermissionSubscriptionMap map = (PEXPermissionSubscriptionMap) INSTANCE.get();
		if (map != null) {
			return map;
		}
		if (INJECTOR == null) {
			INJECTOR = new FieldReplacer(manager.getClass(), "permSubs", Map.class);
		}
		Map<String, Map<Permissible, Boolean>> backing = (Map) INJECTOR.get(manager);
		if ((backing instanceof PEXPermissionSubscriptionMap)) {
			return (PEXPermissionSubscriptionMap) backing;
		}
		PEXPermissionSubscriptionMap wrappedMap = new PEXPermissionSubscriptionMap(plugin, manager, backing);
		if (INSTANCE.compareAndSet(null, wrappedMap)) {
			INJECTOR.set(manager, wrappedMap);
			return wrappedMap;
		}
		return (PEXPermissionSubscriptionMap) INSTANCE.get();
	}

	public void uninject() {
		if (INSTANCE.compareAndSet(this, null)) {
			Map<String, Map<Permissible, Boolean>> unwrappedMap = new HashMap(size());
			for (Map.Entry<String, Map<Permissible, Boolean>> entry : entrySet()) {
				if ((entry.getValue() instanceof PEXSubscriptionValueMap)) {
					unwrappedMap.put((String) entry.getKey(), ((PEXSubscriptionValueMap) entry.getValue()).backing);
				}
			}
			INJECTOR.set(this.manager, unwrappedMap);
		}
	}

	@Override
	public Map<Permissible, Boolean> get(Object key) {
		if (key == null) {
			return null;
		}

		Map<Permissible, Boolean> result = super.get(key);
		if (result == null) {
			result = new PEXSubscriptionValueMap((String) key, new WeakHashMap<Permissible, Boolean>());
			super.put((String) key, result);
		} else if (!(result instanceof PEXSubscriptionValueMap)) {
			result = new PEXSubscriptionValueMap((String) key, result);
			super.put((String) key, result);
		}
		return result;
	}

	public Map<Permissible, Boolean> put(String key, Map<Permissible, Boolean> value) {
		if (!(value instanceof PEXSubscriptionValueMap)) {
			value = new PEXSubscriptionValueMap(key, value);
		}
		return (Map) super.put(key, value);
	}

	public class PEXSubscriptionValueMap implements Map<Permissible, Boolean> {
		private final String permission;
		private final Map<Permissible, Boolean> backing;

		public PEXSubscriptionValueMap(String permission, Map<Permissible, Boolean> backing) {
			this.permission = permission;
			this.backing = backing;
		}

		public int size() {
			return this.backing.size();
		}

		public boolean isEmpty() {
			return this.backing.isEmpty();
		}

		public boolean containsKey(Object key) {
			return (this.backing.containsKey(key))
					|| (((key instanceof Permissible)) && (((Permissible) key).isPermissionSet(this.permission)));
		}

		public boolean containsValue(Object value) {
			return this.backing.containsValue(value);
		}

		public Boolean put(Permissible key, Boolean value) {
			return (Boolean) this.backing.put(key, value);
		}

		public Boolean remove(Object key) {
			return (Boolean) this.backing.remove(key);
		}

		public void putAll(Map<? extends Permissible, ? extends Boolean> m) {
			this.backing.putAll(m);
		}

		public void clear() {
			this.backing.clear();
		}

		public Boolean get(Object key) {
			if ((key instanceof Permissible)) {
				Permissible p = (Permissible) key;
				if (p.isPermissionSet(this.permission)) {
					return Boolean.valueOf(p.hasPermission(this.permission));
				}
			}
			return (Boolean) this.backing.get(key);
		}

		@SuppressWarnings("deprecation")
		public Set<Permissible> keySet() {
			Object players = PEXPermissionSubscriptionMap.this.plugin.getServer().getOnlinePlayers();
			int size = 0;
			try {
				if (players.getClass().isAssignableFrom(Collection.class)) {
					size = ((Collection) players).size();
				} else {
					size = ((Player[]) players).length;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Set<Permissible> pexMatches = new HashSet(size);
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission(this.permission)) {
					pexMatches.add(player);
				}
			}
			return Sets.union(pexMatches, this.backing.keySet());
		}

		public Collection<Boolean> values() {
			return this.backing.values();
		}

		public Set<Map.Entry<Permissible, Boolean>> entrySet() {
			return this.backing.entrySet();
		}
	}
}
