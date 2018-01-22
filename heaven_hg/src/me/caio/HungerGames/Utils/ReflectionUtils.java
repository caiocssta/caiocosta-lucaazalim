package me.caio.HungerGames.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public class ReflectionUtils {
	private static String preClassB = "org.bukkit.craftbukkit";
	private static String preClassM = "net.minecraft.server";
	private static boolean forge = false;

	static {
		if (Bukkit.getServer() != null) {
			if ((Bukkit.getVersion().contains("MCPC")) || (Bukkit.getVersion().contains("Forge"))) {
				forge = true;
			}
			Server server = Bukkit.getServer();
			Class<?> bukkitServerClass = server.getClass();
			String[] pas = bukkitServerClass.getName().split("\\.");
			if (pas.length == 5) {
				String verB = pas[3];
				preClassB = preClassB + "." + verB;
			}
			try {
				Method getHandle = bukkitServerClass.getDeclaredMethod("getHandle", new Class[0]);
				Object handle = getHandle.invoke(server, new Object[0]);
				Class<?> handleServerClass = handle.getClass();
				pas = handleServerClass.getName().split("\\.");
				if (pas.length == 5) {
					String verM = pas[3];
					preClassM = preClassM + "." + verM;
				}
			} catch (Exception localException) {
			}
		}
	}

	public static boolean isForge() {
		return forge;
	}

	public static RefClass getRefClass(String... classes) {
		String[] arrayOfString = classes;
		int j = classes.length;
		for (int i = 0; i < j; i++) {
			String className = arrayOfString[i];
			try {
				className = className.replace("{cb}", preClassB).replace("{nms}", preClassM).replace("{nm}", "net.minecraft");
				return getRefClass(Class.forName(className));
			} catch (ClassNotFoundException localClassNotFoundException) {
			}
		}
		throw new RuntimeException("no class found");
	}

	public static RefClass getRefClass(Class<?> clazz) {
		return new RefClass(clazz);
	}

	public static class RefClass {
		private final Class<?> clazz;

		public Class<?> getRealClass() {
			return this.clazz;
		}

		private RefClass(Class<?> clazz) {
			this.clazz = clazz;
		}

		public boolean isInstance(Object object) {
			return this.clazz.isInstance(object);
		}

		/* Error */
		public ReflectionUtils.RefMethod getMethod(String name, Object... types) {
			return null;
			// Byte code:
			// 0: aload_2
			// 1: arraylength
			// 2: anewarray 29 java/lang/Class
			// 5: astore_3
			// 6: iconst_0
			// 7: istore 4
			// 9: aload_2
			// 10: dup
			// 11: astore 8
			// 13: arraylength
			// 14: istore 7
			// 16: iconst_0
			// 17: istore 6
			// 19: goto +74 -> 93
			// 22: aload 8
			// 24: iload 6
			// 26: aaload
			// 27: astore 5
			// 29: aload 5
			// 31: instanceof 29
			// 34: ifeq +18 -> 52
			// 37: aload_3
			// 38: iload 4
			// 40: iinc 4 1
			// 43: aload 5
			// 45: checkcast 29 java/lang/Class
			// 48: aastore
			// 49: goto +41 -> 90
			// 52: aload 5
			// 54: instanceof 1
			// 57: ifeq +21 -> 78
			// 60: aload_3
			// 61: iload 4
			// 63: iinc 4 1
			// 66: aload 5
			// 68: checkcast 1
			// me/flame/HungerGames/Utils/ReflectionUtils$RefClass
			// 71: invokevirtual 36
			// me/flame/HungerGames/Utils/ReflectionUtils$RefClass:getRealClass
			// ()Ljava/lang/Class;
			// 74: aastore
			// 75: goto +15 -> 90
			// 78: aload_3
			// 79: iload 4
			// 81: iinc 4 1
			// 84: aload 5
			// 86: invokevirtual 38 java/lang/Object:getClass
			// ()Ljava/lang/Class;
			// 89: aastore
			// 90: iinc 6 1
			// 93: iload 6
			// 95: iload 7
			// 97: if_icmplt -75 -> 22
			// 100: new 41 me/flame/HungerGames/Utils/ReflectionUtils$RefMethod
			// 103: dup
			// 104: aload_0
			// 105: getfield 13
			// me/flame/HungerGames/Utils/ReflectionUtils$RefClass:clazz
			// Ljava/lang/Class;
			// 108: aload_1
			// 109: aload_3
			// 110: invokevirtual 43 java/lang/Class:getMethod
			// (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
			// 113: aconst_null
			// 114: invokespecial 46
			// me/flame/HungerGames/Utils/ReflectionUtils$RefMethod:<init>
			// (Ljava/lang/reflect/Method;Lme/flame/HungerGames/Utils/ReflectionUtils$RefMethod;)V
			// 117: areturn
			// 118: astore 5
			// 120: new 41 me/flame/HungerGames/Utils/ReflectionUtils$RefMethod
			// 123: dup
			// 124: aload_0
			// 125: getfield 13
			// me/flame/HungerGames/Utils/ReflectionUtils$RefClass:clazz
			// Ljava/lang/Class;
			// 128: aload_1
			// 129: aload_3
			// 130: invokevirtual 49 java/lang/Class:getDeclaredMethod
			// (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
			// 133: aconst_null
			// 134: invokespecial 46
			// me/flame/HungerGames/Utils/ReflectionUtils$RefMethod:<init>
			// (Ljava/lang/reflect/Method;Lme/flame/HungerGames/Utils/ReflectionUtils$RefMethod;)V
			// 137: areturn
			// 138: astore_3
			// 139: new 52 java/lang/RuntimeException
			// 142: dup
			// 143: aload_3
			// 144: invokespecial 54 java/lang/RuntimeException:<init>
			// (Ljava/lang/Throwable;)V
			// 147: athrow
			// Line number table:
			// Java source line #134 -> byte code offset #0
			// Java source line #135 -> byte code offset #6
			// Java source line #136 -> byte code offset #9
			// Java source line #137 -> byte code offset #29
			// Java source line #138 -> byte code offset #37
			// Java source line #139 -> byte code offset #52
			// Java source line #140 -> byte code offset #60
			// Java source line #142 -> byte code offset #78
			// Java source line #136 -> byte code offset #90
			// Java source line #145 -> byte code offset #100
			// Java source line #146 -> byte code offset #118
			// Java source line #147 -> byte code offset #120
			// Java source line #149 -> byte code offset #138
			// Java source line #150 -> byte code offset #139
			// Local variable table:
			// start length slot name signature
			// 0 148 0 this RefClass
			// 0 148 1 name String
			// 0 148 2 types Object[]
			// 5 125 3 classes Class[]
			// 138 6 3 e Exception
			// 7 73 4 i int
			// 27 58 5 e Object
			// 118 3 5 ignored java.lang.NoSuchMethodException
			// 17 81 6 i int
			// 14 84 7 j int
			// 11 12 8 arrayOfObject Object[]
			// Exception table:
			// from to target type
			// 100 117 118 java/lang/NoSuchMethodException
			// 0 117 138 java/lang/Exception
			// 118 137 138 java/lang/Exception
		}

		/* Error */
		public ReflectionUtils.RefConstructor getConstructor(Object... types) {
			return null;
			// Byte code:
			// 0: aload_1
			// 1: arraylength
			// 2: anewarray 29 java/lang/Class
			// 5: astore_2
			// 6: iconst_0
			// 7: istore_3
			// 8: aload_1
			// 9: dup
			// 10: astore 7
			// 12: arraylength
			// 13: istore 6
			// 15: iconst_0
			// 16: istore 5
			// 18: goto +71 -> 89
			// 21: aload 7
			// 23: iload 5
			// 25: aaload
			// 26: astore 4
			// 28: aload 4
			// 30: instanceof 29
			// 33: ifeq +17 -> 50
			// 36: aload_2
			// 37: iload_3
			// 38: iinc 3 1
			// 41: aload 4
			// 43: checkcast 29 java/lang/Class
			// 46: aastore
			// 47: goto +39 -> 86
			// 50: aload 4
			// 52: instanceof 1
			// 55: ifeq +20 -> 75
			// 58: aload_2
			// 59: iload_3
			// 60: iinc 3 1
			// 63: aload 4
			// 65: checkcast 1
			// me/flame/HungerGames/Utils/ReflectionUtils$RefClass
			// 68: invokevirtual 36
			// me/flame/HungerGames/Utils/ReflectionUtils$RefClass:getRealClass
			// ()Ljava/lang/Class;
			// 71: aastore
			// 72: goto +14 -> 86
			// 75: aload_2
			// 76: iload_3
			// 77: iinc 3 1
			// 80: aload 4
			// 82: invokevirtual 38 java/lang/Object:getClass
			// ()Ljava/lang/Class;
			// 85: aastore
			// 86: iinc 5 1
			// 89: iload 5
			// 91: iload 6
			// 93: if_icmplt -72 -> 21
			// 96: new 80
			// me/flame/HungerGames/Utils/ReflectionUtils$RefConstructor
			// 99: dup
			// 100: aload_0
			// 101: getfield 13
			// me/flame/HungerGames/Utils/ReflectionUtils$RefClass:clazz
			// Ljava/lang/Class;
			// 104: aload_2
			// 105: invokevirtual 82 java/lang/Class:getConstructor
			// ([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
			// 108: aconst_null
			// 109: invokespecial 85
			// me/flame/HungerGames/Utils/ReflectionUtils$RefConstructor:<init>
			// (Ljava/lang/reflect/Constructor;Lme/flame/HungerGames/Utils/ReflectionUtils$RefConstructor;)V
			// 112: areturn
			// 113: astore 4
			// 115: new 80
			// me/flame/HungerGames/Utils/ReflectionUtils$RefConstructor
			// 118: dup
			// 119: aload_0
			// 120: getfield 13
			// me/flame/HungerGames/Utils/ReflectionUtils$RefClass:clazz
			// Ljava/lang/Class;
			// 123: aload_2
			// 124: invokevirtual 88 java/lang/Class:getDeclaredConstructor
			// ([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
			// 127: aconst_null
			// 128: invokespecial 85
			// me/flame/HungerGames/Utils/ReflectionUtils$RefConstructor:<init>
			// (Ljava/lang/reflect/Constructor;Lme/flame/HungerGames/Utils/ReflectionUtils$RefConstructor;)V
			// 131: areturn
			// 132: astore_2
			// 133: new 52 java/lang/RuntimeException
			// 136: dup
			// 137: aload_2
			// 138: invokespecial 54 java/lang/RuntimeException:<init>
			// (Ljava/lang/Throwable;)V
			// 141: athrow
			// Line number table:
			// Java source line #165 -> byte code offset #0
			// Java source line #166 -> byte code offset #6
			// Java source line #167 -> byte code offset #8
			// Java source line #168 -> byte code offset #28
			// Java source line #169 -> byte code offset #36
			// Java source line #170 -> byte code offset #50
			// Java source line #171 -> byte code offset #58
			// Java source line #173 -> byte code offset #75
			// Java source line #167 -> byte code offset #86
			// Java source line #176 -> byte code offset #96
			// Java source line #177 -> byte code offset #113
			// Java source line #178 -> byte code offset #115
			// Java source line #180 -> byte code offset #132
			// Java source line #181 -> byte code offset #133
			// Local variable table:
			// start length slot name signature
			// 0 142 0 this RefClass
			// 0 142 1 types Object[]
			// 5 119 2 classes Class[]
			// 132 6 2 e Exception
			// 7 70 3 i int
			// 26 55 4 e Object
			// 113 3 4 ignored java.lang.NoSuchMethodException
			// 16 78 5 i int
			// 13 81 6 j int
			// 10 12 7 arrayOfObject Object[]
			// Exception table:
			// from to target type
			// 96 112 113 java/lang/NoSuchMethodException
			// 0 112 132 java/lang/Exception
			// 113 131 132 java/lang/Exception
		}

		@SuppressWarnings("rawtypes")
		public ReflectionUtils.RefMethod findMethod(Object... types) {
			Class[] classes = new Class[types.length];
			int t = 0;
			for (Object e : types) {
				if ((e instanceof Class)) {
					classes[(t++)] = ((Class<?>) e);
				} else if ((e instanceof RefClass)) {
					classes[(t++)] = ((RefClass) e).getRealClass();
				} else {
					classes[(t++)] = e.getClass();
				}
			}
			List<Method> methods = new ArrayList<Method>();
			Collections.addAll(methods, this.clazz.getMethods());
			Collections.addAll(methods, this.clazz.getDeclaredMethods());
			for (Method m : methods) {
				Class[] methodTypes = m.getParameterTypes();
				if (methodTypes.length == classes.length) {
					int i = 0;
					if ((i < classes.length) && (classes.equals(methodTypes))) {
						return new ReflectionUtils.RefMethod(m);
					}
				}
			}
			throw new RuntimeException("no such method");
		}

		@SuppressWarnings("unused")
		public ReflectionUtils.RefMethod findMethodByName(String... names) {
			List<Method> methods = new ArrayList<Method>();
			Collections.addAll(methods, this.clazz.getMethods());
			Collections.addAll(methods, this.clazz.getDeclaredMethods());
			int j;
			int i;
			for (Iterator<Method> localIterator = methods.iterator(); localIterator.hasNext();) {
				Method m = (Method) localIterator.next();
				String[] arrayOfString;
				j = (arrayOfString = names).length;
				i = 0;
				String name = arrayOfString[i];
				if (m.getName().equals(name)) {
					return new ReflectionUtils.RefMethod(m);
				}
				i++;
			}
			throw new RuntimeException("no such method");
		}

		public ReflectionUtils.RefMethod findMethodByReturnType(RefClass type) {
			return findMethodByReturnType(type.clazz);
		}

		public ReflectionUtils.RefMethod findMethodByReturnType(Class<?> type) {
			if (type == null) {
				type = Void.TYPE;
			}
			List<Method> methods = new ArrayList<Method>();
			Collections.addAll(methods, this.clazz.getMethods());
			Collections.addAll(methods, this.clazz.getDeclaredMethods());
			for (Method m : methods) {
				if (type.equals(m.getReturnType())) {
					return new ReflectionUtils.RefMethod(m);
				}
			}
			throw new RuntimeException("no such method");
		}

		@SuppressWarnings("rawtypes")
		public ReflectionUtils.RefConstructor findConstructor(int number) {
			List<Constructor> constructors = new ArrayList<Constructor>();
			Collections.addAll(constructors, this.clazz.getConstructors());
			Collections.addAll(constructors, this.clazz.getDeclaredConstructors());
			for (Constructor<?> m : constructors) {
				if (m.getParameterTypes().length == number) {
					return new ReflectionUtils.RefConstructor(m);
				}
			}
			throw new RuntimeException("no such constructor");
		}

		public ReflectionUtils.RefField getField(String name) {
			try {
				return new ReflectionUtils.RefField(this.clazz.getField(name));
			} catch (NoSuchFieldException ignored) {
				try {
					return new ReflectionUtils.RefField(this.clazz.getDeclaredField(name));
				} catch (NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		}

		public ReflectionUtils.RefField findField(RefClass type) {
			return findField(type.clazz);
		}

		public ReflectionUtils.RefField findField(Class<?> type) {
			if (type == null) {
				type = Void.TYPE;
			}
			List<Field> fields = new ArrayList<Field>();
			Collections.addAll(fields, this.clazz.getFields());
			Collections.addAll(fields, this.clazz.getDeclaredFields());
			for (Field f : fields) {
				if (type.equals(f.getType())) {
					return new ReflectionUtils.RefField(f);
				}
			}
			throw new RuntimeException("no such field");
		}
	}

	public static class RefMethod {
		private final Method method;

		public Method getRealMethod() {
			return this.method;
		}

		public ReflectionUtils.RefClass getRefClass() {
			return new ReflectionUtils.RefClass(this.method.getDeclaringClass());
		}

		public ReflectionUtils.RefClass getReturnRefClass() {
			return new ReflectionUtils.RefClass(this.method.getReturnType());
		}

		private RefMethod(Method method) {
			this.method = method;
			method.setAccessible(true);
		}

		public RefExecutor of(Object e) {
			return new RefExecutor(e);
		}

		public Object call(Object... params) {
			try {
				return this.method.invoke(null, params);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public class RefExecutor {
			Object e;

			public RefExecutor(Object e) {
				this.e = e;
			}

			public Object call(Object... params) {
				try {
					return ReflectionUtils.RefMethod.this.method.invoke(this.e, params);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static class RefConstructor {
		private final Constructor<?> constructor;

		public Constructor<?> getRealConstructor() {
			return this.constructor;
		}

		public ReflectionUtils.RefClass getRefClass() {
			return new ReflectionUtils.RefClass(this.constructor.getDeclaringClass());
		}

		private RefConstructor(Constructor<?> constructor) {
			this.constructor = constructor;
			constructor.setAccessible(true);
		}

		public Object create(Object... params) {
			try {
				return this.constructor.newInstance(params);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class RefField {
		private Field field;

		public Field getRealField() {
			return this.field;
		}

		public ReflectionUtils.RefClass getRefClass() {
			return new ReflectionUtils.RefClass(this.field.getDeclaringClass());
		}

		public ReflectionUtils.RefClass getFieldRefClass() {
			return new ReflectionUtils.RefClass(this.field.getType());
		}

		private RefField(Field field) {
			this.field = field;
			field.setAccessible(true);
		}

		public RefExecutor of(Object e) {
			return new RefExecutor(e);
		}

		public class RefExecutor {
			Object e;

			public RefExecutor(Object e) {
				this.e = e;
			}

			public void set(Object param) {
				try {
					ReflectionUtils.RefField.this.field.set(this.e, param);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			public Object get() {
				try {
					return ReflectionUtils.RefField.this.field.get(this.e);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
