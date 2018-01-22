package br.com.wombocraft.lobby.rank.injector.regexperms;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import br.com.wombocraft.lobby.rank.injector.FieldReplacer;

@SuppressWarnings({ "unchecked", "rawtypes" })

public class PermissionList extends HashMap<String, Permission> {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private static FieldReplacer<PluginManager, Map> INJECTOR;
	private static final Map<Class<?>, FieldReplacer<Permission, Map>> CHILDREN_MAPS = new HashMap();
	private final Multimap<String, Map.Entry<String, Boolean>> childParentMapping = Multimaps
			.synchronizedMultimap(HashMultimap.<String, Map.Entry<String, Boolean>> create());

	public PermissionList() {
	}

	public PermissionList(Map<? extends String, ? extends Permission> existing) {
		super(existing);
	}

	private FieldReplacer<Permission, Map> getFieldReplacer(Permission perm) {
		FieldReplacer<Permission, Map> ret = (FieldReplacer) CHILDREN_MAPS.get(perm.getClass());
		if (ret == null) {
			ret = new FieldReplacer(perm.getClass(), "children", Map.class);
			CHILDREN_MAPS.put(perm.getClass(), ret);
		}
		return ret;
	}

	private void removeAllChildren(String perm) {
		for (Iterator<Map.Entry<String, Map.Entry<String, Boolean>>> it = this.childParentMapping.entries()
				.iterator(); it.hasNext();) {
			if (((String) ((Map.Entry) ((Map.Entry) it.next()).getValue()).getKey()).equals(perm)) {
				it.remove();
			}
		}
	}

	private class NotifyingChildrenMap extends LinkedHashMap<String, Boolean> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final Permission perm;

		public NotifyingChildrenMap(Permission perm) {
			super();
			this.perm = perm;
		}

		public Boolean remove(Object perm) {
			removeFromMapping(String.valueOf(perm));
			return (Boolean) super.remove(perm);
		}

		private void removeFromMapping(String child) {
			for (Iterator<Map.Entry<String, Boolean>> it = PermissionList.this.childParentMapping.get(child)
					.iterator(); it.hasNext();) {
				if (((String) ((Map.Entry) it.next()).getKey()).equals(this.perm.getName())) {
					it.remove();
				}
			}
		}

		public Boolean put(String perm, Boolean val) {
			PermissionList.this.childParentMapping.put(perm, new AbstractMap.SimpleEntry(this.perm.getName(), val));
			return (Boolean) super.put(perm, val);
		}

		public void clear() {
			PermissionList.this.removeAllChildren(this.perm.getName());
			super.clear();
		}
	}

	public static PermissionList inject(PluginManager manager) {
		if (INJECTOR == null) {
			INJECTOR = new FieldReplacer(manager.getClass(), "permissions", Map.class);
		}
		Map existing = (Map) INJECTOR.get(manager);

		PermissionList list = new PermissionList(existing);
		INJECTOR.set(manager, list);
		return list;
	}

	public Permission put(String k, Permission v) {
		for (Map.Entry<String, Boolean> ent : v.getChildren().entrySet()) {
			this.childParentMapping.put((String) ent.getKey(),
					new AbstractMap.SimpleEntry(v.getName(), (Boolean) ent.getValue()));
		}
		FieldReplacer<Permission, Map> repl = getFieldReplacer(v);
		repl.set(v, new NotifyingChildrenMap(v));
		return (Permission) super.put(k, v);
	}

	public Permission remove(Object k) {
		Permission ret = (Permission) super.remove(k);
		if (ret != null) {
			removeAllChildren(k.toString());
			getFieldReplacer(ret).set(ret, new LinkedHashMap(ret.getChildren()));
		}
		return ret;
	}

	public void clear() {
		this.childParentMapping.clear();
		super.clear();
	}

	public Collection<Map.Entry<String, Boolean>> getParents(String permission) {
		return this.childParentMapping.get(permission.toLowerCase());
	}
}
