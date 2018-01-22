package me.caio.HungerGames.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.caio.HungerGames.Main;
import me.caio.HungerGames.Events.TimeSecondEvent;
import me.caio.HungerGames.NMS.Title;
import me.caio.HungerGames.Team.Enums.TeamColors;
import me.caio.HungerGames.Utils.Feast;
import me.caio.HungerGames.Utils.FinalBattle;
import me.caio.HungerGames.Utils.Minifeast;
import me.caio.HungerGames.Utils.Enum.GameStage;
import me.skater.Events.UpdateEvent;

public class TeamGameListener implements Listener {
	public Main main;
	public Map<UUID, Location> locations = new HashMap<UUID, Location>();

	public TeamGameListener(Main m) {
		this.main = m;

	}

	private void killPlayer(Player p) {

		String pTeam = "Sem Time";
		TeamColors ptc = main.team.getTeam(p);
		if (ptc != null) {
			pTeam = "Time " + main.team.teamNomes.get(ptc);
		}
		String morreu = ChatColor.RED + p.getName() + "(§l" + pTeam + "§r§c)§7";
		String messageDeath = morreu + " eliminado por menos quantia de kills.";
		this.main.removeGamer(p);
		if (!this.main.perm.isMvp(p)) {
			p.kickPlayer("§cVoce morreu!\n" + messageDeath + "\n§aTente novamente na proxima!\n§eAcesse: §bheavenmc.com.br");
			this.main.listener.deathMessage.put(p.getUniqueId(), "Voce perdeu\n" + messageDeath);
		} else if (this.main.perm.isTrial(p)) {
			this.main.listener.deathMessage.put(p.getUniqueId(), "Voce perdeu!\n" + messageDeath);
			this.main.adm.setAdmin(p);
		} else {
			this.main.listener.deathMessage.put(p.getUniqueId(), "Voce perdeu\n" + messageDeath);
			this.main.adm.setYoutuber(p);
		}
	}

	@SuppressWarnings({ "incomplete-switch", "deprecation" })
	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateEvent.UpdateType.SECOND) {
			return;
		}
		switch (this.main.stage) {
		case GAMETIME:
			this.main.GameTimer += 1;
			int kills;
			if (this.main.GameTimer >= 3600) {
				TeamColors t = null;
				kills = 0;
				for (TeamColors tcs : main.team.teamPlayers.keySet()) {
					if (main.team.teamIsAlive(tcs)) {
						if (main.team.getKillsTeam(tcs) > kills) {
							kills = main.team.getKillsTeam(tcs);
							t = tcs;
						}
					}
				}

				for (TeamColors tcs : main.team.teamPlayers.keySet()) {
					if (tcs != t) {
						for (Player player : Bukkit.getOnlinePlayers()) {
							if (!this.main.isNotPlaying(player)) {
								if (main.team.getTeam(player) != null) {
									if (main.team.getTeam(player) == tcs) {
										killPlayer(player);
									}
								}
							}
						}
					}
				}

				this.main.checkWinner();
			}
			if (this.main.GameTimer == 900) {
				Feast feast = new Feast(300, true);
				feast.startCountdown();
			}
			
			if (this.main.GameTimer == 1800) {
				Feast bonusFeast = new Feast(1, false);
				bonusFeast.startCountdown();
				this.main.getServer().broadcastMessage(ChatColor.RED + "Um bonus feast foi spawnado em um local aleatorio do mapa.");
			}
			if (this.main.GameTimer == 2400) {
				this.main.getServer().broadcastMessage(ChatColor.RED + "A partida esta chegando ao fim, Batalha Final em 5 minutos.");
			}
			if (this.main.GameTimer == 2700) {
				new FinalBattle(this.main);
				this.main.getServer().broadcastMessage(ChatColor.RED + "Todos foram teleportados para a Batalha Final!");
			}
			if (this.main.GameTimer == 3300) {
				this.main.getServer().broadcastMessage(ChatColor.RED + "Em 5 minutos, vencerá quem tiver mais kills.");
			}
			if (this.main.GameTimer % 240 == 0) {
				new Minifeast(true);
			}
			break;
		case INVENCIBILITY:
			if (this.main.Invenci.intValue() > 0) {
				if (this.main.Invenci.intValue() % 60 == 0) {
					this.main.getServer().broadcastMessage("§eA invencibilidade acaba em §c" + this.main.getTime(this.main.Invenci).replace("minutos", "§eminutos").replace("segundos", "§esegundos"));
				} else if ((this.main.Invenci.intValue() == 45) || (this.main.Invenci.intValue() == 30) || (this.main.Invenci.intValue() == 15) || (this.main.Invenci.intValue() == 10)
						|| (this.main.Invenci.intValue() <= 5)) {
				} else if ((this.main.Invenci.intValue() == 45) || (this.main.Invenci.intValue() == 30) || (this.main.Invenci.intValue() == 15) || (this.main.Invenci.intValue() == 10)
						|| (this.main.Invenci.intValue() <= 5)) {
					this.main.getServer().broadcastMessage("§eA invencibilidade acaba em §c" + this.main.getTime(this.main.Invenci).replace("minutos", "§eminutos").replace("segundos", "§esegundos"));
				} else if ((this.main.Invenci.intValue() == 45) || (this.main.Invenci.intValue() == 30) || (this.main.Invenci.intValue() == 15) || (this.main.Invenci.intValue() == 10)
						|| (this.main.Invenci.intValue() <= 5)) {
				}
			/*	if ((this.main.Invenci.intValue() == 60) && (this.main.coliseumManager.isConstructed())) {
					this.main.coliseumManager.destroyColiseum();
				}
			*/	main.Invenci = Integer.valueOf(main.Invenci.intValue() - 1);
				this.main.GameTimer += 1;
			} else {
			/*	if (this.main.coliseumManager.isConstructed()) {
					this.main.coliseumManager.destroyColiseum();
				}
			*/	this.main.stage = GameStage.GAMETIME;
				main.getServer().broadcastMessage("§cA invencibilidade acabou. O PvP foi liberado!");
				this.main.getSpawn().getWorld().playSound(this.main.getSpawn(), Sound.ANVIL_LAND, 10.0F, 1.0F);
			//	this.main.getScoreboardManager().updateState();
			//	for (Player p : main.getServer().getOnlinePlayers()) {
			//		this.main.getScoreboardManager().updateKills(p);
			//	}
				if (me.skater.Main.getInstance().globalmute) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chat on");
				}
			}
			break;
		case PREGAME:
			if (this.main.countStarted) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!main.coliseumManager.isInsideColiseum(p)) {
						if (!main.perm.isTrial(p)) {
							main.sendToSpawn(p);
						}
					}
				}
				if (this.main.PreGameTimer > 0) {
					if (this.main.PreGameTimer % 60 == 0) {
						this.main.getServer()
								.broadcastMessage("§eO torneio irá iniciar em §c" + this.main.getTime(Integer.valueOf(this.main.PreGameTimer)).replace("minutos", "§eminutos").replace("segundos", "§esegundos"));
					} else if ((this.main.PreGameTimer == 45) || (this.main.PreGameTimer == 10) || (this.main.PreGameTimer <= 5)) {
						this.main.getServer()
								.broadcastMessage("§eO torneio irá iniciar em §c" + this.main.getTime(Integer.valueOf(this.main.PreGameTimer)).replace("minutos", "§eminutos").replace("segundos", "§esegundos"));
						Title title = new Title("§eTorneio");
						title.setSubtitle(ChatColor.YELLOW + "Iniciando em §c" + this.main.PreGameTimer + " §esegundos");
						title.setStayTime(1);
						title.setTimingsToSeconds();
						title.broadcast();
					}
					if (this.main.PreGameTimer <= 60) {
						if (this.main.PreGameTimer > 30) {
							this.main.coliseumManager.teleportRecursive(this.main.PreGameTimer - 30);
						} else {
							this.main.coliseumManager.teleportOutsidePlayers();
						}
					}
					if (this.main.PreGameTimer == 10) {
						for (Player p : Bukkit.getOnlinePlayers()) {
							if (!this.main.adm.isSpectating(p)) {
								p.setFlying(false);
								p.setAllowFlight(false);
							}
						}
					}
					this.main.PreGameTimer -= 1;
				} else {
					int size = this.main.getServer().getOnlinePlayers().length - this.main.adm.admin.size();
					if (size >= 2) {
						this.main.startGame();
						this.locations.clear();
					} else {
						this.main.getServer().broadcastMessage("§cPoucos jogadores, contagem reiniciada!");
						if (me.skater.Main.getInstance().globalmute) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chat off");
						}
						this.main.PreGameTimer = 60;
					}
				}
			}
			break;
		}
		Bukkit.getPluginManager().callEvent(new TimeSecondEvent());
	}

	public static String getHourTime(Integer i) {
		int minutes = i.intValue() / 60;
		int seconds = i.intValue() % 60;
		String disMinu = (minutes < 10 ? "" : "") + minutes;
		String disSec = (seconds < 10 ? "0" : "") + seconds;
		String formattedTime = disMinu + ":" + disSec;
		return formattedTime;
	}

	public void deathMessage(String message) {
		if (this.main.gamers.size() - 1 == 1)
			this.main.getServer().broadcastMessage(message + "\n§c" + (this.main.gamers.size() - 1) + " jogador restante");
		else
			this.main.getServer().broadcastMessage(message + "\n§c" + (this.main.gamers.size() - 1) + " jogadores restantes");
	}
}
