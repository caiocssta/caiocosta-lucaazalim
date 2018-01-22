package me.caio.HungerGames.Copa;

public class Grupo {

	private int group_id;
	private String group_name;

	public Grupo(int groupID, String groupName) {
		this.group_id = groupID;
		this.group_name = groupName;
	}

	public String getNome() {
		return group_name;
	}

	public int getID() {
		return this.group_id;
	}

}
