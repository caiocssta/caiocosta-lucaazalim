package me.caio.HungerGames.Constructors;

public class NovaList {
	private double pitch;
	private double yaw;

	public NovaList(double pitch, double yaw) {
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public double getPitch() {
		return this.pitch;
	}

	public double getYaw() {
		return this.yaw;
	}
}
