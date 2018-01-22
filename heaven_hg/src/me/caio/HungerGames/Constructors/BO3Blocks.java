package me.caio.HungerGames.Constructors;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BO3Blocks {
	private int x;
	private int y;
	private int z;
	private Material type;
	private byte data;

	@SuppressWarnings("deprecation")
	public BO3Blocks(Block b) {
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
		this.type = b.getType();
		this.data = b.getData();
	}

	public BO3Blocks(int x, int y, int z, Material type, byte data) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.data = data;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public Material getType() {
		return this.type;
	}

	public byte getData() {
		return this.data;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof BO3Blocks)) {
			return false;
		}
		BO3Blocks bo3 = (BO3Blocks) obj;
		if (bo3.type != this.type) {
			return false;
		}
		if (bo3.data != this.data) {
			return false;
		}
		if (bo3.x != this.x) {
			return false;
		}
		if (bo3.y != this.y) {
			return false;
		}
		if (bo3.z != this.z) {
			return false;
		}
		return true;
	}
}
