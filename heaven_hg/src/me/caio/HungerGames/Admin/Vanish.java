package me.caio.HungerGames.Admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.caio.HungerGames.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Vanish {
	private static HashMap<UUID, VLevel> vanished = new HashMap<UUID, VLevel>();
	private Main m;
	private List<UUID> spectatorsDisabled = new ArrayList<UUID>();

	public Vanish(Main main) {
		this.m = main;
	}

	public void setSpectatorEnabled(Player p, boolean b) {
		if (!b) {
			if (!this.spectatorsDisabled.contains(p.getUniqueId())) {
				this.spectatorsDisabled.add(p.getUniqueId());
			}
		} else {
			this.spectatorsDisabled.remove(p.getUniqueId());
		}
	}

	public void makeVanished(Player p) {
		if (this.m.perm.isOwner(p)) {
			makeVanished(p, VLevel.DONO);
		} else if (this.m.perm.isAdmin(p)) {
			makeVanished(p, VLevel.ADMIN);
		} else if (this.m.perm.isMod(p)) {
			makeVanished(p, VLevel.MODPLUS);
		} else if (this.m.perm.isTrial(p)) {
			makeVanished(p, VLevel.MOD);
		} else if (this.m.perm.isYouTuber(p)) {
			makeVanished(p, VLevel.YOUTUBER);
		} else if (this.m.perm.isMvp(p)) {
			makeVanished(p, VLevel.PRO);
		}
	}

	public void makeVanished(Player p, VLevel level) {
		if (level.equals(VLevel.PRO)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (!player.getName().equals(p.getName())) {
					if (!this.m.perm.isMvp(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!this.m.isNotPlaying(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (this.spectatorsDisabled.contains(player.getUniqueId())) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!player.canSee(p)) {
						player.showPlayer(p);
					}
				}
			}
		} else if (level.equals(VLevel.YOUTUBER)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (!player.getName().equals(p.getName())) {
					if (!this.m.perm.isYouTuber(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!this.m.isNotPlaying(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (this.spectatorsDisabled.contains(player.getUniqueId())) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!player.canSee(p)) {
						player.showPlayer(p);
					}
				}
			}
		} else if (level.equals(VLevel.MOD)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (!player.getName().equals(p.getName())) {
					if (!this.m.perm.isTrial(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!this.m.isNotPlaying(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (this.spectatorsDisabled.contains(player.getUniqueId())) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!player.canSee(p)) {
						player.showPlayer(p);
					}
				}
			}
		} else if (level.equals(VLevel.MODPLUS)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (!player.getName().equals(p.getName())) {
					if (!this.m.perm.isMod(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!this.m.isNotPlaying(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (this.spectatorsDisabled.contains(player.getUniqueId())) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!player.canSee(p)) {
						player.showPlayer(p);
					}
				}
			}
		} else if (level.equals(VLevel.ADMIN)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (!player.getName().equals(p.getName())) {
					if (!this.m.perm.isAdmin(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!this.m.isNotPlaying(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (this.spectatorsDisabled.contains(player.getUniqueId())) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!player.canSee(p)) {
						player.showPlayer(p);
					}
				}
			}
		} else if (level.equals(VLevel.DONO)) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(p);
				if (!player.getName().equals(p.getName())) {
					if (!this.m.perm.isOwner(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!this.m.isNotPlaying(player)) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (this.spectatorsDisabled.contains(player.getUniqueId())) {
						if (player.canSee(p)) {
							player.hidePlayer(p);
						}
					} else if (!player.canSee(p)) {
						player.showPlayer(p);
					}
				}
			}
		}
		vanished.put(p.getUniqueId(), level);
	}

	public boolean isVanished(Player p) {
		return (vanished.containsKey(p.getUniqueId())) && (!((VLevel) vanished.get(p.getUniqueId())).equals(VLevel.PLAYER));
	}

	public VLevel getPlayerLevel(Player p) {
		return (VLevel) vanished.get(p.getUniqueId());
	}

	public void updateVanished() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (isVanished(p)) {
				makeVanished(p, (VLevel) vanished.get(p.getUniqueId()));
			} else {
				makeVisible(p);
			}
		}
	}

	public void makeVisible(Player p) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!player.canSee(p)) {
				player.showPlayer(p);
			}
		}
		vanished.put(p.getUniqueId(), VLevel.PLAYER);
	}
}
