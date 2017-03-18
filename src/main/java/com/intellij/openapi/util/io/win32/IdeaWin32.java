//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.openapi.util.io.win32;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.win32.FileInfo;
import com.intellij.util.SystemProperties;
import com.intellij.util.lang.UrlClassLoader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.zip.CRC32;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IdeaWin32 {
	private static final Logger LOG = Logger.getInstance("#com.intellij.openapi.util.io.win32.IdeaWin32");
	private static final boolean TRACE_ENABLED;
	private static final IdeaWin32 ourInstance;
	
	private static boolean loadBundledLibrary() throws IOException {
		String name = SystemInfo.is64Bit?"IdeaWin64":"IdeaWin32";
		URL bundled = IdeaWin32.class.getResource(name + ".dll");
		if(bundled == null) {
			return false;
		} else {
			byte[] content = FileUtil.loadBytes(bundled.openStream());
			CRC32 crc32 = new CRC32();
			crc32.update(content);
			long hash = Math.abs(crc32.getValue());
			File file = new File(FileUtil.getTempDirectory(), name + '.' + hash + ".dll");
			if(!file.exists()) {
				FileUtil.writeToFile(file, content);
			}
			
			System.load(file.getPath());
			return true;
		}
	}
	
	public static boolean isAvailable() {
		return ourInstance != null;
	}
	
	@NotNull
	public static IdeaWin32 getInstance() {
		if(!isAvailable()) {
			throw new IllegalStateException("Native filesystem for Windows is not loaded");
		} else {
			IdeaWin32 var10000 = ourInstance;
			if(ourInstance == null) {
				throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/intellij/openapi/util/io/win32/IdeaWin32", "getInstance"}));
			} else {
				return var10000;
			}
		}
	}
	
	private IdeaWin32() {
		initIDs();
	}
	
	private static native void initIDs();
	
	@Nullable
	public FileInfo getInfo(@NotNull String path) {
		path = path.replace('/', '\\');
		if(TRACE_ENABLED) {
			//LOG.trace("getInfo(" + path + ")");
			long t = System.nanoTime();
			FileInfo result = this.getInfo0(path);
			t = (System.nanoTime() - t) / 1000L;
			//LOG.trace("  " + t + " mks");
			return result;
		} else {
			return this.getInfo0(path);
		}
	}
	
	private native FileInfo getInfo0(String var1);
	
	static {
		TRACE_ENABLED = LOG.isTraceEnabled();
		IdeaWin32 instance = null;
		if(SystemInfo.isWin2kOrNewer && SystemProperties.getBooleanProperty("idea.use.native.fs.for.win", true)) {
			try {
				if(!loadBundledLibrary()) {
					UrlClassLoader.loadPlatformLibrary("IdeaWin32");
				}
				
				instance = new IdeaWin32();
				//LOG.info("Native filesystem for Windows is operational");
			} catch (Throwable var2) {
				//LOG.warn("Failed to initialize native filesystem for Windows", var2);
			}
		}
		
		ourInstance = instance;
	}
}
