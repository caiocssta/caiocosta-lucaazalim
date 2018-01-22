package br.com.wombocraft.lobby.utils;

import java.text.DecimalFormat;

public class Utils {
	public static String replaceMoney(Integer coins) {
		DecimalFormat df = new DecimalFormat("###,###.##");
		return df.format(coins);
	}
}
