package br.com.wombocraft.lobby.nms;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import br.com.wombocraft.lobby.nms.packets.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;

public class TabListManager
{
  public static void broadcastHeader(String header)
  {
    broadcastHeaderAndFooter(header, null);
  }
  
  public static void broadcastFooter(String footer)
  {
    broadcastHeaderAndFooter(null, footer);
  }
  
  @SuppressWarnings("deprecation")
public static void broadcastHeaderAndFooter(String header, String footer)
  {
    for (Player player : Bukkit.getOnlinePlayers()) {
      setHeaderAndFooter(player, header, footer);
    }
  }
  
  public static void setHeader(Player p, String header)
  {
    setHeaderAndFooter(p, header, null);
  }
  
  public static void setFooter(Player p, String footer)
  {
    setHeaderAndFooter(p, null, footer);
  }
  
  public static void setHeaderAndFooter(Player p, String rawHeader, String rawFooter)
  {
    CraftPlayer player = (CraftPlayer)p;
    if (player.getHandle().playerConnection.networkManager.getVersion() < 47) {
      return;
    }
    IChatBaseComponent header = ChatSerializer.a(TextConverter.convert(rawHeader));
    IChatBaseComponent footer = ChatSerializer.a(TextConverter.convert(rawFooter));
    PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(header);
    try
    {
      Field field = packet.getClass().getDeclaredField("b");
      field.setAccessible(true);
      field.set(packet, footer);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      player.getHandle().playerConnection.sendPacket(packet);
    }
  }
  
  private static class TextConverter
  {
    public static String convert(String text)
    {
      if ((text == null) || (text.length() == 0)) {
        return "\"\"";
      }
      int len = text.length();
      StringBuilder sb = new StringBuilder(len + 4);
      
      sb.append('"');
      for (int i = 0; i < len; i++)
      {
        char c = text.charAt(i);
        switch (c)
        {
        case '"': 
        case '\\': 
          sb.append('\\');
          sb.append(c);
          break;
        case '/': 
          sb.append('\\');
          sb.append(c);
          break;
        case '\b': 
          sb.append("\\b");
          break;
        case '\t': 
          sb.append("\\t");
          break;
        case '\n': 
          sb.append("\\n");
          break;
        case '\f': 
          sb.append("\\f");
          break;
        case '\r': 
          sb.append("\\r");
          break;
        default: 
          if (c < ' ')
          {
            String t = "000" + Integer.toHexString(c);
            sb.append("\\u").append(t.substring(t.length() - 4));
          }
          else
          {
            sb.append(c);
          }
          break;
        }
      }
      sb.append('"');
      return sb.toString();
    }
    
    @SuppressWarnings("unused")
	public static String setPlayerName(Player player, String text)
    {
      return text.replaceAll("(?i)\\{PLAYER\\}", player.getName());
    }
  }
}
