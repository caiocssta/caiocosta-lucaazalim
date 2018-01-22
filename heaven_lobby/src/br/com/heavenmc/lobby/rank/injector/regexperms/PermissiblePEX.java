package br.com.wombocraft.lobby.rank.injector.regexperms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import br.com.wombocraft.lobby.rank.PermissionManager;
import br.com.wombocraft.lobby.rank.injector.FieldReplacer;
import br.com.wombocraft.lobby.rank.injector.PermissionCheckResult;

@SuppressWarnings({ "rawtypes", "unchecked" })

public class PermissiblePEX extends PermissibleBase {
	private static final FieldReplacer<PermissibleBase, Map> PERMISSIONS_FIELD = new FieldReplacer(
			PermissibleBase.class, "permissions", Map.class);
	private static final FieldReplacer<PermissibleBase, List> ATTACHMENTS_FIELD = new FieldReplacer(
			PermissibleBase.class, "attachments", List.class);
	private static final Method CALC_CHILD_PERMS_METH;
	private final Map<String, PermissionAttachmentInfo> permissions;
	private final List<PermissionAttachment> attachments;

	static {
		try {
			CALC_CHILD_PERMS_METH = PermissibleBase.class.getDeclaredMethod("calculateChildPermissions",
					new Class[] { Map.class, Boolean.TYPE, PermissionAttachment.class });
		} catch (NoSuchMethodException e) {
			throw new ExceptionInInitializerError(e);
		}
		CALC_CHILD_PERMS_METH.setAccessible(true);
	}

	private static final AtomicBoolean LAST_CALL_ERRORED = new AtomicBoolean(false);
	protected final Player player;
	protected final PermissionManager plugin;
	private Permissible previousPermissible = null;
	protected final Map<String, PermissionCheckResult> cache = new ConcurrentHashMap();
	private final Object permissionsLock = new Object();

	public PermissiblePEX(Player player, PermissionManager plugin) {
		super(player);
		this.player = player;
		this.plugin = plugin;
		this.permissions = new LinkedHashMap() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unused")
			public PermissionAttachmentInfo put(String k, PermissionAttachmentInfo v) {
				PermissionAttachmentInfo existing = (PermissionAttachmentInfo) get(k);
				if (existing != null) {
					return existing;
				}
				return (PermissionAttachmentInfo) super.put(k, v);
			}
		};
		PERMISSIONS_FIELD.set(this, this.permissions);
		this.attachments = ((List) ATTACHMENTS_FIELD.get(this));
		recalculatePermissions();
	}

	public Permissible getPreviousPermissible() {
		return this.previousPermissible;
	}

	public void setPreviousPermissible(Permissible previousPermissible) {
		this.previousPermissible = previousPermissible;
	}

	@SuppressWarnings("incomplete-switch")
	public boolean hasPermission(String permission) {
		PermissionCheckResult res = permissionValue(permission);
		switch (res) {
		case TRUE:
		case UNDEFINED:
			return res.toBoolean();
		}
		if (super.isPermissionSet(permission)) {
			boolean ret = super.hasPermission(permission);
			return ret;
		}
		Permission perm = this.player.getServer().getPluginManager().getPermission(permission);
		return perm == null ? Permission.DEFAULT_PERMISSION.getValue(this.player.isOp())
				: perm.getDefault().getValue(this.player.isOp());
	}

	@SuppressWarnings("incomplete-switch")
	public boolean hasPermission(Permission permission) {
		PermissionCheckResult res = permissionValue(permission.getName());
		switch (res) {
		case TRUE:
		case UNDEFINED:
			return res.toBoolean();
		}
		if (super.isPermissionSet(permission.getName())) {
			boolean ret = super.hasPermission(permission);
			return ret;
		}
		return permission.getDefault().getValue(this.player.isOp());
	}

	public void recalculatePermissions() {
		if ((this.cache != null) && (this.permissions != null) && (this.attachments != null)) {
			synchronized (this.permissionsLock) {
				clearPermissions();
				this.cache.clear();
				PermissionAttachment attach;
				for (ListIterator<PermissionAttachment> it = this.attachments.listIterator(this.attachments.size()); it
						.hasPrevious();) {
					attach = (PermissionAttachment) it.previous();
					calculateChildPerms(attach.getPermissions(), false, attach);
				}
				for (Permission p : this.player.getServer().getPluginManager().getDefaultPermissions(isOp())) {
					this.permissions.put(p.getName(),
							new PermissionAttachmentInfo(this.player, p.getName(), null, true));
					calculateChildPerms(p.getChildren(), false, null);
				}
			}
		}
	}

	protected void calculateChildPerms(Map<String, Boolean> children, boolean invert, PermissionAttachment attachment) {
		try {
			CALC_CHILD_PERMS_METH.invoke(this, new Object[] { children, Boolean.valueOf(invert), attachment });
		} catch (IllegalAccessException localIllegalAccessException) {
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isPermissionSet(String permission) {
		return (super.isPermissionSet(permission)) || (permissionValue(permission) != PermissionCheckResult.UNDEFINED);
	}

	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		synchronized (this.permissionsLock) {
			return new LinkedHashSet(this.permissions.values());
		}
	}

	private PermissionCheckResult checkSingle(String expression, String permission, boolean value) {
		if (this.plugin.getPermissionMatcher().isMatches(expression, permission)) {
			PermissionCheckResult res = PermissionCheckResult.fromBoolean(value);
			return res;
		}
		return PermissionCheckResult.UNDEFINED;
	}

	protected PermissionCheckResult permissionValue(String permission) {
		try {
			Validate.notNull(permission, "Permissions being checked must not be null!");
			permission = permission.toLowerCase();
			PermissionCheckResult res = (PermissionCheckResult) this.cache.get(permission);
			if (res != null) {
				return res;
			}
			res = PermissionCheckResult.UNDEFINED;
			PermissionAttachmentInfo pai;
			synchronized (this.permissionsLock) {
				for (Iterator localIterator = this.permissions.values().iterator(); localIterator.hasNext();) {
					pai = (PermissionAttachmentInfo) localIterator.next();
					if ((res = checkSingle(pai.getPermission(), permission,
							pai.getValue())) != PermissionCheckResult.UNDEFINED) {
						break;
					}
				}
			}
			if (res == PermissionCheckResult.UNDEFINED) {
				for (Map.Entry<String, Boolean> ent : this.plugin.getRegexPerms().getPermissionList()
						.getParents(permission)) {
					if ((res = permissionValue((String) ent.getKey())) != PermissionCheckResult.UNDEFINED) {
						res = PermissionCheckResult
								.fromBoolean(!(res.toBoolean() ^ ((Boolean) ent.getValue()).booleanValue()));
						break;
					}
				}
			}
			this.cache.put(permission, res);
			LAST_CALL_ERRORED.set(false);
			return res;
		} catch (Throwable t) {
			if (LAST_CALL_ERRORED.compareAndSet(false, true)) {
				t.printStackTrace();
			}
		}
		return PermissionCheckResult.UNDEFINED;
	}
}
