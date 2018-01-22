package me.skater.titles;

import java.util.ArrayList;
import java.util.UUID;

import me.skater.Enums.ServerType;

import org.bukkit.scheduler.BukkitRunnable;

public class PlayerTitle {
	private UUID uuid;
	private int activetitle;
	private TitleManager manager;
	private ArrayList<Integer> titles;

	public PlayerTitle(TitleManager manager, UUID uuid) {
		this(manager, uuid, 0);
	}

	public PlayerTitle(TitleManager manager, UUID uuid, int activetitle) {
		this.titles = new ArrayList<>();
		this.manager = manager;
		this.uuid = uuid;
		this.activetitle = activetitle;

	}

	public int getActiveTitle() {
		return this.activetitle;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public ArrayList<Integer> getTitles() {
		return this.titles;
	}

	public int addTitleNoSave(int id) {
		if (!titles.contains(id)) {
			this.titles.add(id);
		}
		return id;
	}

	private void save() {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					PlayerTitle.this.manager.savePlayerTitle(PlayerTitle.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(this.manager.getPlugin());
	}

	public Title getTitle() {
		return Title.getTitleByID(activetitle);
	}

	public int addTitle(int id) {
		if (!titles.contains(id)) {
			this.titles.add(id);
			save();
		}
		return id;
	}

	public int changeTitle(int id) {
		this.activetitle = id;
		save();
		if (manager.getPlugin().type == ServerType.HG) {
		}
		return id;
	}

}
