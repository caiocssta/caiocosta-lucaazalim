package br.com.wombocraft.lobby.rank.injector;

public abstract interface PermissionMatcher
{
  public abstract boolean isMatches(String paramString1, String paramString2);
}
