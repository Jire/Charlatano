//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.openapi.util.io;

import com.intellij.Patches;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileAttributes;
import com.intellij.openapi.util.io.win32.FileInfo;
import com.intellij.openapi.util.io.win32.IdeaWin32;
import com.intellij.util.ArrayUtil;
import com.intellij.util.BitUtil;
import com.intellij.util.SystemProperties;
import com.intellij.util.containers.ContainerUtil;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileSystemUtil {
	private static final Logger LOG = Logger.getInstance("#com.intellij.openapi.util.io.FileSystemUtil");
	@NotNull
	private static FileSystemUtil.Mediator ourMediator = getMediator();
	
	private static FileSystemUtil.Mediator getMediator() {
		boolean forceNio2 = SystemProperties.getBooleanProperty("idea.io.use.nio2", false);
		boolean forceFallback = SystemProperties.getBooleanProperty("idea.io.use.fallback", false);
		Throwable error = null;
		if(!forceNio2 && !forceFallback) {
			if(SystemInfo.isWindows && IdeaWin32.isAvailable()) {
				try {
					return check(new FileSystemUtil.IdeaWin32MediatorImpl());
				} catch (Throwable var6) {
					error = var6;
				}
			} else if(SystemInfo.isLinux || SystemInfo.isMac || SystemInfo.isSolaris || SystemInfo.isFreeBSD) {
				try {
					return check(new FileSystemUtil.JnaUnixMediatorImpl());
				} catch (Throwable var5) {
					error = var5;
				}
			}
		}
		
		if(!forceFallback && SystemInfo.isJavaVersionAtLeast("1.7") && !"1.7.0-ea".equals(SystemInfo.JAVA_VERSION)) {
			try {
				return check(new FileSystemUtil.Nio2MediatorImpl());
			} catch (Throwable var4) {
				error = var4;
			}
		}
		
		if(!forceFallback) {
			//LOG.warn("Failed to load filesystem access layer: " + SystemInfo.OS_NAME + ", " + SystemInfo.JAVA_VERSION + ", nio2=" + forceNio2, error);
		}
		
		return new FileSystemUtil.FallbackMediatorImpl();
	}
	
	private static FileSystemUtil.Mediator check(FileSystemUtil.Mediator mediator) throws Exception {
		String quickTestPath = SystemInfo.isWindows?"C:\\":"/";
		mediator.getAttributes(quickTestPath);
		return mediator;
	}
	
	private FileSystemUtil() {
	}
	
	@Nullable
	public static FileAttributes getAttributes(@NotNull String path) {
		try {
			return ourMediator.getAttributes(path);
		} catch (Exception var2) {
			//LOG.warn(var2);
			return null;
		}
	}
	
	@Nullable
	public static FileAttributes getAttributes(@NotNull File file) {
		return getAttributes(file.getPath());
	}
	
	public static boolean isSymLink(@NotNull String path) {
		if(!SystemInfo.areSymLinksSupported) {
			return false;
		} else {
			FileAttributes attributes = getAttributes(path);
			return attributes != null && attributes.isSymLink();
		}
	}
	
	public static boolean isSymLink(@NotNull File file) {
		return isSymLink(file.getAbsolutePath());
	}
	
	public static boolean clonePermissionsToExecute(@NotNull String source, @NotNull String target) {
		try {
			return ourMediator.clonePermissions(source, target, true);
		} catch (Exception var3) {
			//LOG.warn(var3);
			return false;
		}
	}
	
	private static class FallbackMediatorImpl extends FileSystemUtil.Mediator {
		private final Object myFileSystem;
		private final Method myGetBooleanAttributes;
		
		private FallbackMediatorImpl() {
			super();
			
			Object fileSystem;
			Method getBooleanAttributes;
			try {
				Field t = File.class.getDeclaredField("fs");
				t.setAccessible(true);
				fileSystem = t.get((Object)null);
				getBooleanAttributes = fileSystem.getClass().getMethod("getBooleanAttributes", new Class[]{File.class});
				getBooleanAttributes.setAccessible(true);
			} catch (Throwable var4) {
				fileSystem = null;
				getBooleanAttributes = null;
			}
			
			this.myFileSystem = fileSystem;
			this.myGetBooleanAttributes = getBooleanAttributes;
		}
		
		protected FileAttributes getAttributes(@NotNull String path) throws Exception {
			File file = new File(path);
			boolean isSpecial;
			boolean isHidden;
			boolean isWritable;
			if(this.myFileSystem != null) {
				int isDirectory = ((Integer)this.myGetBooleanAttributes.invoke(this.myFileSystem, new Object[]{file})).intValue();
				if(isDirectory != 0) {
					isSpecial = BitUtil.isSet(isDirectory, 4);
					isHidden = !BitUtil.isSet(isDirectory, 2) && !BitUtil.isSet(isDirectory, 4);
					isWritable = BitUtil.isSet(isDirectory, 8) && !isWindowsRoot(path);
					boolean isWritable1 = SystemInfo.isWindows && isSpecial || file.canWrite();
					return new FileAttributes(isSpecial, isHidden, false, isWritable, file.length(), file.lastModified(), isWritable1);
				}
			} else if(file.exists()) {
				boolean var8 = file.isDirectory();
				isSpecial = !var8 && !file.isFile();
				isHidden = file.isHidden() && !isWindowsRoot(path);
				isWritable = SystemInfo.isWindows && var8 || file.canWrite();
				return new FileAttributes(var8, isSpecial, false, isHidden, file.length(), file.lastModified(), isWritable);
			}
			
			return null;
		}
		
		private static boolean isWindowsRoot(String p) {
			return SystemInfo.isWindows && p.length() >= 2 && p.length() <= 3 && Character.isLetter(p.charAt(0)) && p.charAt(1) == 58;
		}
		
		protected boolean clonePermissions(@NotNull String source, @NotNull String target, boolean onlyPermissionsToExecute) throws Exception {
			if(SystemInfo.isUnix) {
				File srcFile = new File(source);
				File dstFile = new File(target);
				return !onlyPermissionsToExecute && !dstFile.setWritable(srcFile.canWrite(), true)?false:dstFile.setExecutable(srcFile.canExecute(), true);
			} else {
				return false;
			}
		}
	}
	
	private static class JnaUnixMediatorImpl extends FileSystemUtil.Mediator {
		private static final int[] LINUX_32 = new int[]{16, 44, 72, 24, 28};
		private static final int[] LINUX_64 = new int[]{24, 48, 88, 28, 32};
		private static final int[] LNX_PPC32 = new int[]{16, 48, 80, 24, 28};
		private static final int[] LNX_PPC64;
		private static final int[] LNX_ARM32;
		private static final int[] BSD_32;
		private static final int[] BSD_64;
		private static final int[] SUN_OS_32;
		private static final int[] SUN_OS_64;
		private final int[] myOffsets;
		private final int myUid;
		private final int myGid;
		private final boolean myCoarseTs;
		
		private JnaUnixMediatorImpl() throws Exception {
			super();
			this.myCoarseTs = SystemProperties.getBooleanProperty("idea.io.coarse.ts", false);
			if(SystemInfo.isLinux) {
				if("arm".equals(SystemInfo.OS_ARCH)) {
					if(!SystemInfo.is32Bit) {
						throw new IllegalStateException("AArch64 architecture is not supported");
					}
					
					this.myOffsets = LNX_ARM32;
				} else if("ppc".equals(SystemInfo.OS_ARCH)) {
					this.myOffsets = SystemInfo.is32Bit?LNX_PPC32:LNX_PPC64;
				} else {
					this.myOffsets = SystemInfo.is32Bit?LINUX_32:LINUX_64;
				}
			} else if(SystemInfo.isMac | SystemInfo.isFreeBSD) {
				this.myOffsets = SystemInfo.is32Bit?BSD_32:BSD_64;
			} else {
				if(!SystemInfo.isSolaris) {
					throw new IllegalStateException("Unsupported OS/arch: " + SystemInfo.OS_NAME + "/" + SystemInfo.OS_ARCH);
				}
				
				this.myOffsets = SystemInfo.is32Bit?SUN_OS_32:SUN_OS_64;
			}
			
			Native.register(FileSystemUtil.JnaUnixMediatorImpl.LibC.class, "c");
			Native.register(SystemInfo.isLinux?FileSystemUtil.JnaUnixMediatorImpl.LinuxLibC.class:FileSystemUtil.JnaUnixMediatorImpl.UnixLibC.class, "c");
			this.myUid = FileSystemUtil.JnaUnixMediatorImpl.LibC.getuid();
			this.myGid = FileSystemUtil.JnaUnixMediatorImpl.LibC.getgid();
		}
		
		protected FileAttributes getAttributes(@NotNull String path) throws Exception {
			Memory buffer = new Memory(256L);
			int res = SystemInfo.isLinux?FileSystemUtil.JnaUnixMediatorImpl.LinuxLibC.__lxstat64(1, path, buffer):FileSystemUtil.JnaUnixMediatorImpl.UnixLibC.lstat(path, buffer);
			if(res != 0) {
				return null;
			} else {
				int mode = this.getModeFlags(buffer) & '\uffff';
				boolean isSymlink = (mode & '\uf000') == 40960;
				if(isSymlink) {
					if(!loadFileStatus(path, buffer)) {
						return FileAttributes.BROKEN_SYMLINK;
					}
					
					mode = this.getModeFlags(buffer) & '\uffff';
				}
				
				boolean isDirectory = (mode & '\uf000') == 16384;
				boolean isSpecial = !isDirectory && (mode & '\uf000') != 32768;
				long size = buffer.getLong((long)this.myOffsets[1]);
				long mTime1 = SystemInfo.is32Bit?(long)buffer.getInt((long)this.myOffsets[2]):buffer.getLong((long)this.myOffsets[2]);
				long mTime2 = this.myCoarseTs?0L:(SystemInfo.is32Bit?(long)buffer.getInt((long)(this.myOffsets[2] + 4)):buffer.getLong((long)(this.myOffsets[2] + 8)));
				long mTime = mTime1 * 1000L + mTime2 / 1000000L;
				boolean writable = this.ownFile(buffer)?(mode & 146) != 0:FileSystemUtil.JnaUnixMediatorImpl.LibC.access(path, 2) == 0;
				return new FileAttributes(isDirectory, isSpecial, isSymlink, false, size, mTime, writable);
			}
		}
		
		private static boolean loadFileStatus(String path, Memory buffer) {
			return (SystemInfo.isLinux?FileSystemUtil.JnaUnixMediatorImpl.LinuxLibC.__xstat64(1, path, buffer):FileSystemUtil.JnaUnixMediatorImpl.UnixLibC.stat(path, buffer)) == 0;
		}
		
		protected boolean clonePermissions(@NotNull String source, @NotNull String target, boolean onlyPermissionsToExecute) throws Exception {
			Memory buffer = new Memory(256L);
			if(!loadFileStatus(source, buffer)) {
				return false;
			} else {
				int sourcePermissions = this.getModeFlags(buffer) & 511;
				int permissions;
				if(onlyPermissionsToExecute) {
					if(!loadFileStatus(target, buffer)) {
						return false;
					}
					
					int targetPermissions = this.getModeFlags(buffer) & 511;
					permissions = targetPermissions & -74 | sourcePermissions & 73;
				} else {
					permissions = sourcePermissions;
				}
				
				return FileSystemUtil.JnaUnixMediatorImpl.LibC.chmod(target, permissions) == 0;
			}
		}
		
		private int getModeFlags(Memory buffer) {
			return SystemInfo.isLinux?buffer.getInt((long)this.myOffsets[0]):buffer.getShort((long)this.myOffsets[0]);
		}
		
		private boolean ownFile(Memory buffer) {
			return buffer.getInt((long)this.myOffsets[3]) == this.myUid && buffer.getInt((long)this.myOffsets[4]) == this.myGid;
		}
		
		static {
			LNX_PPC64 = LINUX_64;
			LNX_ARM32 = LNX_PPC32;
			BSD_32 = new int[]{8, 48, 32, 12, 16};
			BSD_64 = new int[]{8, 72, 40, 12, 16};
			SUN_OS_32 = new int[]{20, 48, 64, 28, 32};
			SUN_OS_64 = new int[]{16, 40, 64, 24, 28};
		}
		
		private static class LinuxLibC {
			private LinuxLibC() {
			}
			
			static native int __lxstat64(int var0, String var1, Pointer var2);
			
			static native int __xstat64(int var0, String var1, Pointer var2);
		}
		
		private static class UnixLibC {
			private UnixLibC() {
			}
			
			static native int lstat(String var0, Pointer var1);
			
			static native int stat(String var0, Pointer var1);
		}
		
		private static class LibC {
			private LibC() {
			}
			
			static native int getuid();
			
			static native int getgid();
			
			static native int chmod(String var0, int var1);
			
			static native int access(String var0, int var1);
		}
	}
	
	private static class IdeaWin32MediatorImpl extends FileSystemUtil.Mediator {
		private IdeaWin32 myInstance;
		
		private IdeaWin32MediatorImpl() {
			super();
			this.myInstance = IdeaWin32.getInstance();
		}
		
		protected FileAttributes getAttributes(@NotNull String path) throws Exception {
			FileInfo fileInfo = this.myInstance.getInfo(path);
			return fileInfo != null?fileInfo.toFileAttributes():null;
		}
	}
	
	private static class Nio2MediatorImpl extends FileSystemUtil.Mediator {
		private final Object myDefaultFileSystem;
		private final Method myGetPath;
		private final Method myIsSymbolicLink;
		private final Object myLinkOptions;
		private final Object myNoFollowLinkOptions;
		private final Method myReadAttributes;
		private final Method mySetAttribute;
		private final Method myToMillis;
		private final String mySchema;
		
		private Nio2MediatorImpl() throws Exception {
			super();
			
			assert Patches.USE_REFLECTION_TO_ACCESS_JDK7;
			
			this.myDefaultFileSystem = Class.forName("java.nio.file.FileSystems").getMethod("getDefault", new Class[0]).invoke((Object)null, new Object[0]);
			Class fsClass = Class.forName("java.nio.file.FileSystem");
			this.myGetPath = fsClass.getMethod("getPath", new Class[]{String.class, String[].class});
			this.myGetPath.setAccessible(true);
			Class pathClass = Class.forName("java.nio.file.Path");
			Class filesClass = Class.forName("java.nio.file.Files");
			this.myIsSymbolicLink = filesClass.getMethod("isSymbolicLink", new Class[]{pathClass});
			this.myIsSymbolicLink.setAccessible(true);
			Class linkOptClass = Class.forName("java.nio.file.LinkOption");
			this.myLinkOptions = Array.newInstance(linkOptClass, 0);
			this.myNoFollowLinkOptions = Array.newInstance(linkOptClass, 1);
			Array.set(this.myNoFollowLinkOptions, 0, linkOptClass.getField("NOFOLLOW_LINKS").get((Object)null));
			Class linkOptArrClass = this.myLinkOptions.getClass();
			this.myReadAttributes = filesClass.getMethod("readAttributes", new Class[]{pathClass, String.class, linkOptArrClass});
			this.myReadAttributes.setAccessible(true);
			this.mySetAttribute = filesClass.getMethod("setAttribute", new Class[]{pathClass, String.class, Object.class, linkOptArrClass});
			this.mySetAttribute.setAccessible(true);
			Class fileTimeClass = Class.forName("java.nio.file.attribute.FileTime");
			this.myToMillis = fileTimeClass.getMethod("toMillis", new Class[0]);
			this.myToMillis.setAccessible(true);
			this.mySchema = SystemInfo.isWindows?"dos:*":"posix:*";
		}
		
		protected FileAttributes getAttributes(@NotNull String path) throws Exception {
			try {
				Object e = this.myGetPath.invoke(this.myDefaultFileSystem, new Object[]{path, ArrayUtil.EMPTY_STRING_ARRAY});
				Map var15 = (Map)this.myReadAttributes.invoke((Object)null, new Object[]{e, this.mySchema, this.myNoFollowLinkOptions});
				boolean isSymbolicLink = ((Boolean)var15.get("isSymbolicLink")).booleanValue();
				if(isSymbolicLink) {
					try {
						var15 = (Map)this.myReadAttributes.invoke((Object)null, new Object[]{e, this.mySchema, this.myLinkOptions});
					} catch (InvocationTargetException var13) {
						Throwable isOther = var13.getCause();
						if(isOther != null && "java.nio.file.NoSuchFileException".equals(isOther.getClass().getName())) {
							return FileAttributes.BROKEN_SYMLINK;
						}
					}
				}
				
				boolean isDirectory = ((Boolean)var15.get("isDirectory")).booleanValue();
				boolean var16 = ((Boolean)var15.get("isOther")).booleanValue();
				long size = ((Long)var15.get("size")).longValue();
				long lastModified = ((Long)this.myToMillis.invoke(var15.get("lastModifiedTime"), new Object[0])).longValue();
				boolean isWritable;
				if(SystemInfo.isWindows) {
					isWritable = (new File(path)).getParent() == null?false:((Boolean)var15.get("hidden")).booleanValue();
					boolean isWritable1 = isDirectory || !((Boolean)var15.get("readonly")).booleanValue();
					return new FileAttributes(isDirectory, var16, isSymbolicLink, isWritable, size, lastModified, isWritable1);
				} else {
					isWritable = (new File(path)).canWrite();
					return new FileAttributes(isDirectory, var16, isSymbolicLink, false, size, lastModified, isWritable);
				}
			} catch (InvocationTargetException var14) {
				Throwable cause = var14.getCause();
				if(!(cause instanceof IOException) && (cause == null || !"java.nio.file.InvalidPathException".equals(cause.getClass().getName()))) {
					throw var14;
				} else {
					//FileSystemUtil.LOG.debug(cause);
					return null;
				}
			}
		}
		
		protected boolean clonePermissions(@NotNull String source, @NotNull String target, boolean onlyPermissionsToExecute) throws Exception {
			if(SystemInfo.isUnix) {
				Object sourcePath = this.myGetPath.invoke(this.myDefaultFileSystem, new Object[]{source, ArrayUtil.EMPTY_STRING_ARRAY});
				Object targetPath = this.myGetPath.invoke(this.myDefaultFileSystem, new Object[]{target, ArrayUtil.EMPTY_STRING_ARRAY});
				Collection sourcePermissions = this.getPermissions(sourcePath);
				Collection targetPermissions = this.getPermissions(targetPath);
				if(sourcePermissions != null && targetPermissions != null) {
					if(onlyPermissionsToExecute) {
						HashSet permissionsToSet = ContainerUtil.newHashSet();
						Iterator var9 = targetPermissions.iterator();
						
						Object permission;
						while(var9.hasNext()) {
							permission = var9.next();
							if(!permission.toString().endsWith("_EXECUTE")) {
								permissionsToSet.add(permission);
							}
						}
						
						var9 = sourcePermissions.iterator();
						
						while(var9.hasNext()) {
							permission = var9.next();
							if(permission.toString().endsWith("_EXECUTE")) {
								permissionsToSet.add(permission);
							}
						}
						
						this.mySetAttribute.invoke((Object)null, new Object[]{targetPath, "posix:permissions", permissionsToSet, this.myLinkOptions});
					} else {
						this.mySetAttribute.invoke((Object)null, new Object[]{targetPath, "posix:permissions", sourcePermissions, this.myLinkOptions});
					}
					
					return true;
				}
			}
			
			return false;
		}
		
		private Collection getPermissions(Object sourcePath) throws IllegalAccessException, InvocationTargetException {
			Map attributes = (Map)this.myReadAttributes.invoke((Object)null, new Object[]{sourcePath, "posix:permissions", this.myLinkOptions});
			if(attributes == null) {
				return null;
			} else {
				Object permissions = attributes.get("permissions");
				return permissions instanceof Collection?(Collection)permissions:null;
			}
		}
	}
	
	private abstract static class Mediator {
		private Mediator() {
		}
		
		@Nullable
		protected abstract FileAttributes getAttributes(@NotNull String var1) throws Exception;
		
		protected boolean clonePermissions(@NotNull String source, @NotNull String target, boolean onlyPermissionsToExecute) throws Exception {
			return false;
		}
	}
}
