package br.com.wombocraft.lobby.rank.injector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import br.com.wombocraft.lobby.rank.injector.loaders.LoaderNetUtil;
import br.com.wombocraft.lobby.rank.injector.loaders.LoaderNormal;

public class RegExpMatcher
  implements PermissionMatcher
{
  public static final String RAW_REGEX_CHAR = "$";
  protected static Pattern rangeExpression = Pattern.compile("(\\d+)-(\\d+)");
  private Object patternCache;
  
  public RegExpMatcher()
  {
    Class<?> cacheBuilder = getClassGuava("com.google.common.cache.CacheBuilder");
    Class<?> cacheLoader = getClassGuava("com.google.common.cache.CacheLoader");
    try
    {
      Object obj = cacheBuilder.getMethod("newBuilder", new Class[0]).invoke(null, new Object[0]);
      Method maximumSize = obj.getClass().getMethod("maximumSize", new Class[] { Long.TYPE });
      Object obj2 = maximumSize.invoke(obj, new Object[] { Integer.valueOf(500) });
      Object loader = null;
      if (hasNetUtil()) {
        loader = new LoaderNetUtil();
      } else {
        loader = new LoaderNormal();
      }
      Method build = obj2.getClass().getMethod("build", new Class[] { cacheLoader });
      this.patternCache = build.invoke(obj2, new Object[] { loader });
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public boolean isMatches(String expression, String permission)
  {
    try
    {
      Method get = this.patternCache.getClass().getMethod("get", new Class[] { Object.class });
      get.setAccessible(true);
      Object obj = get.invoke(this.patternCache, new Object[] { expression });
      return ((Pattern)obj).matcher(permission).matches();
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchMethodException e)
    {
      e.printStackTrace();
    }
    catch (SecurityException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  private Class<?> getClassGuava(String str)
  {
    Class<?> clasee = null;
    try
    {
      if (hasNetUtil()) {
        str = "net.minecraft.util." + str;
      }
      clasee = Class.forName(str);
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    return clasee;
  }
  
  private boolean hasNetUtil()
  {
    try
    {
      Class.forName("net.minecraft.util.com.google.common.cache.LoadingCache");
      return true;
    }
    catch (ClassNotFoundException e) {}
    return false;
  }
}
