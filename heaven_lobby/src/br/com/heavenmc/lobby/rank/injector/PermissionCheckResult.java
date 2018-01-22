package br.com.wombocraft.lobby.rank.injector;

public enum PermissionCheckResult
{
  UNDEFINED(false),  TRUE(true),  FALSE(false);
  
  protected boolean result;
  
  private PermissionCheckResult(boolean result)
  {
    this.result = result;
  }
  
  public boolean toBoolean()
  {
    return this.result;
  }
  
  public String toString()
  {
    return this == UNDEFINED ? "undefined" : Boolean.toString(this.result);
  }
  
  public static PermissionCheckResult fromBoolean(boolean result)
  {
    return result ? TRUE : FALSE;
  }
}
