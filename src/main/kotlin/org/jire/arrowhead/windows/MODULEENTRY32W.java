/*
 * Charlatano is a premium CS:GO cheat ran on the JVM.
 * Copyright (C) 2016 Thomas Nappo, Jonathan Beaudoin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jire.arrowhead.windows;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;

import java.util.List;

/**
 * Created by Jonathan on 2016-12-18.
 */
public class MODULEENTRY32W extends Structure {
	
	int MAX_MODULE_NAME32 = 255;
	
	/**
	 * A representation of a MODULEENTRY32 structure as a reference
	 */
	public static class ByReference extends MODULEENTRY32W implements Structure.ByReference {
		public ByReference() {
		}
		
		public ByReference(Pointer memory) {
			super(memory);
		}
	}
	
	public static final List<String> FIELDS = createFieldsOrder(
			"dwSize", "th32ModuleID", "th32ProcessID", "GlblcntUsage",
			"ProccntUsage", "modBaseAddr", "modBaseSize", "hModule", "szModule", "szExePath");
	
	/**
	 * The size of the structure, in bytes. Before calling the Module32First
	 * function, set this member to sizeof(MODULEENTRY32). If you do not
	 * initialize dwSize, Module32First fails.
	 */
	public WinDef.DWORD dwSize;
	
	/**
	 * This member is no longer used, and is always set to one.
	 */
	public WinDef.DWORD th32ModuleID;
	
	/**
	 * The identifier of the process whose modules are to be examined.
	 */
	public WinDef.DWORD th32ProcessID;
	
	/**
	 * The load count of the module, which is not generally meaningful, and
	 * usually equal to 0xFFFF.
	 */
	public WinDef.DWORD GlblcntUsage;
	
	/**
	 * The load count of the module (same as GlblcntUsage), which is not
	 * generally meaningful, and usually equal to 0xFFFF.
	 */
	public WinDef.DWORD ProccntUsage;
	
	/**
	 * The base address of the module in the context of the owning process.
	 */
	public Pointer modBaseAddr;
	
	/**
	 * The size of the module, in bytes.
	 */
	public WinDef.DWORD modBaseSize;
	
	/**
	 * A handle to the module in the context of the owning process.
	 */
	public WinDef.HMODULE hModule;
	
	/**
	 * The module name.
	 */
	public char[] szModule = new char[MAX_MODULE_NAME32 + 1];
	
	/**
	 * The module path.
	 */
	public char[] szExePath = new char[com.sun.jna.platform.win32.Kernel32.MAX_PATH];
	
	public MODULEENTRY32W() {
		dwSize = new WinDef.DWORD(size());
	}
	
	public MODULEENTRY32W(Pointer memory) {
		super(memory);
		read();
	}
	
	/**
	 * @return The module name.
	 */
	public String szModule() {
		return Native.toString(this.szModule);
	}
	
	/**
	 * @return The module path.
	 */
	public String szExePath() {
		return Native.toString(this.szExePath);
	}
	
	@Override
	protected List<String> getFieldOrder() {
		return FIELDS;
	}
}