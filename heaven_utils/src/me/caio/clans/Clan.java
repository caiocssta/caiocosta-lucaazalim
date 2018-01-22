package me.skater.clans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.skater.clans.MemberType.Member;

public class Clan {

	private HashMap<UUID, Member> participants;
	private String name;
	private String tag;
	private ClanManager manager;
	private ClanStatus status;

	public Clan(ClanManager manager, String name, String tag, ClanStatus status) {
		this.manager = manager;
		this.name = name;
		this.tag = tag;
		this.participants = new HashMap<>();
		this.status = status;
	}

	public Clan(ClanManager manager) {
		this.manager = manager;
	}

	public void setStatus(ClanStatus status) {
		this.status = status;
	}

	public ClanStatus getStatus() {
		return this.status;
	}

	public void setOwner(UUID owner) {
		this.participants.put(owner, Member.OWNER);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void promote(UUID uuid) {
		if (isAdmin(uuid)) {
			return;
		}
		this.participants.put(uuid, Member.ADMIN);
	}

	public void demote(UUID uuid) {
		if (isAdmin(uuid)) {
			this.participants.put(uuid, Member.NORMAL);
		}
	}

	public boolean isOwner(UUID uuid) {
		if (this.participants.get(uuid) == Member.OWNER) {
			return true;
		}
		return false;
	}

	public boolean isAdmin(UUID uuid) {
		if (this.participants.get(uuid) == Member.ADMIN) {
			return true;
		}
		return false;
	}

	public boolean isMember(UUID uuid) {
		if (this.participants.get(uuid) == Member.NORMAL) {
			return true;
		}
		return false;
	}

	public boolean isParticipant(UUID uuid) {
		if (this.participants.containsKey(uuid)) {
			return true;
		}
		return false;
	}

	public void addPlayer(UUID uuid, Member member) {
		if (this.participants == null) {
			this.participants = new HashMap<>();
		}
		this.participants.put(uuid, member);
	}

	public void removePlayer(UUID uuid) {
		if (this.participants == null) {
			this.participants = new HashMap<>();
		}
		if (this.participants.containsKey(uuid)) {
			participants.remove(uuid);
		}
	}

	public UUID getOwner() {
		for (UUID uuid : participants.keySet()) {
			Member m = this.participants.get(uuid);
			if (m == Member.OWNER) {
				return uuid;
			}
		}
		return null;
	}

	public String getName() {
		return this.name;
	}

	public String getTag() {
		return this.tag;
	}

	public int getTotalMembers() {
		if (participants == null) {
			return 0;
		}
		return participants.size();
	}

	public int getAdmins() {
		int i = 0;
		for (UUID uuid : participants.keySet()) {
			Member m = this.participants.get(uuid);
			if (m == Member.ADMIN) {
				i++;
			}
		}
		return i;
	}

	public List<UUID> getPlayersUUID() {
		ArrayList<UUID> uuids = new ArrayList<>();
		for (UUID uuid : participants.keySet()) {
			uuids.add(uuid);
		}
		return uuids;
	}

	public Member getPlayerMember(UUID uuid) {
		return this.participants.get(uuid);
	}

	public ClanManager getManager() {
		return this.manager;
	}

}
