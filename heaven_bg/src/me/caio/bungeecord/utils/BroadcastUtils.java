package me.caio.bungeecord.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import me.caio.bungeecord.Main;

public class BroadcastUtils {
	
	  private HashMap <Integer, String> messages;
	  public String tag = "§6§lHeavenMC";

	  private Main m;
	  
	  public BroadcastUtils(Main m) {
		 this.m = m;
		 this.messages = new HashMap<>();
		 this.loadBroadcasts();
	  }
	  
	  
	  public void loadBroadcasts() {
		  m.getProxy().getScheduler().runAsync(m, new Runnable() {
				@Override
				public void run() {
					    messages.clear();
					try {
						 PreparedStatement stmt = null;
					      ResultSet result = null;
					        stmt = m.mainConnection.prepareStatement("SELECT * FROM broadcast;");
					        result = stmt.executeQuery();
					        while (result.next()) {
					        messages.put(result.getInt("ID"), result.getString("message"));
					        }
					      result.close();
					      stmt.close();
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Erro ao carregar broadcast");
					}
				}
			});		  
	  }
	  
	  public void startBroadcast() {
		  m.getProxy().getScheduler().schedule(m, new Runnable() {
				@Override
				public void run() {
				broadcast();
			}
		}, 0, 2, TimeUnit.MINUTES);
	  }
	  
	  
	  
	  @SuppressWarnings("deprecation")
	public void broadcast() {
		  for(ProxiedPlayer p : m.getProxy().getPlayers()) {
				p.sendMessage("");
				StringUtils.sendCenteredMessage(p, tag);
				StringUtils.sendCenteredMessage(p, getRandomMessage());
				p.sendMessage("");

			} 
	  }
	  
	  public void reloadBroadcast() {
		  m.getProxy().getScheduler().runAsync(m, new Runnable() {
				@Override
				public void run() {
					    messages.clear();
					try {
						 PreparedStatement stmt = null;
					      ResultSet result = null;
					        stmt = m.mainConnection.prepareStatement("SELECT * FROM broadcast;");
					        result = stmt.executeQuery();
					        while (result.next()) {
						    messages.put(result.getInt("ID"), result.getString("message"));
					        }
					      result.close();
					      stmt.close();
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Erro ao carregar broadcast");
					}
				}
			});		  
	  }
	  
	  
	  public void addMessage(String name) {
		m.connect.SQLQuerySyncNoLock("INSERT INTO `broadcast`(`message`) VALUES ('"+name+"');");

	  }
	  
	  public void removeMessage(int id) {
			m.connect.SQLQuerySyncNoLock("DELETE FROM `broadcast` WHERE ID='"+id+"';");

		  }
	  
	  @SuppressWarnings("deprecation")
	public void getActiveMessages(ProxiedPlayer p) {
		  for(Integer i : messages.keySet()) {
		  String server = messages.get(i);
		  p.sendMessage("ID: " + i + " " +server);
		  }
	  }
	  
	  public String getRandomMessage() {
		  if(this.messages.size() == 0) {
			  return "";
		  }
		  ArrayList<String> msg = new ArrayList<>();
		  for(String s : messages.values()) {
		  msg.add(s);
		  }
		  return msg.get(new Random().nextInt(messages.size()));
	  }


}
