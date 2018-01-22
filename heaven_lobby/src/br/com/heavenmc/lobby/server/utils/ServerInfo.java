package br.com.wombocraft.lobby.server.utils;

import java.net.InetSocketAddress;

public class ServerInfo
  implements Cloneable
{
  public static final int length = 0;
private final String name;
  private int playersOnline = 0;
  private int maxPlayers = 0;
  private String motd = "";
  private boolean online = true;
  private final InetSocketAddress address;
  
  public ServerInfo(String name, InetSocketAddress address)
  {
    this.name = name;
    this.address = address;
  }
  
  public ServerInfo clone()
  {
    try
    {
      return (ServerInfo)super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new InternalError();
    }
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public int getPlayersOnline()
  {
    return this.playersOnline;
  }
  
  public int getMaxPlayers()
  {
    return this.maxPlayers;
  }
  
  public String getMotd()
  {
    return this.motd;
  }
  
  public boolean isOnline()
  {
    return this.online;
  }
  
  public InetSocketAddress getAddress()
  {
    return this.address;
  }
  
  public void setPlayersOnline(int playersOnline)
  {
    this.playersOnline = playersOnline;
  }
  
  public void setMaxPlayers(int maxPlayers)
  {
    this.maxPlayers = maxPlayers;
  }
  
  public void setMotd(String motd)
  {
    this.motd = motd;
  }
  
  public void setOnline(boolean online)
  {
    this.online = online;
  }
}
