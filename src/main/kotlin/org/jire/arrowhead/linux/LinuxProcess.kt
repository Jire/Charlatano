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

package org.jire.arrowhead.linux

import com.sun.jna.Pointer
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import org.jire.arrowhead.Module
import org.jire.arrowhead.Process
import java.lang.Long.parseLong
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class LinuxProcess(override val id: Int) : Process {
	
	private val local = ThreadLocal.withInitial { iovec() }
	private val remote = ThreadLocal.withInitial { iovec() }
	
	private val modulesMap = Collections.synchronizedMap(Object2ObjectArrayMap<String, LinuxModule>())
	
	override val modules: Map<String, Module> = modulesMap
	
	override fun loadModules() {
		modulesMap.clear()
		
		for (line in Files.readAllLines(Paths.get("/proc/$id/maps"))) {
			val split = line.split(" ")
			val regionSplit = split[0].split("-")
			
			val start = parseLong(regionSplit[0], 16)
			val end = parseLong(regionSplit[1], 16)
			
			val offset = parseLong(split[2], 16)
			if (offset <= 0) continue
			
			var path = "";
			var i = 5
			while (i < split.size) {
				val s = split[i].trim { it <= ' ' }
				if (s.isEmpty() && ++i > split.size) break
				else if (s.isEmpty() && !split[i].trim { it <= ' ' }.isEmpty()) path += split[i]
				else if (!s.isEmpty()) path += split[i]
				i++
			}
			
			val moduleName = path.substring(path.lastIndexOf("/") + 1, path.length)
			modulesMap.put(moduleName, LinuxModule(start, this, moduleName, end - start))
		}
	}
	
	override fun read(address: Pointer, data: Pointer, bytesToRead: Int): Boolean {
		val local = local.get()
		local.iov_base = data
		local.iov_len = bytesToRead
		
		val remote = remote.get()
		remote.iov_base = address
		remote.iov_len = bytesToRead
		
		return bytesToRead.toLong() == uio.process_vm_readv(id, local, 1, remote, 1, 0)
	}
	
	override fun write(address: Pointer, data: Pointer, bytesToWrite: Int): Boolean {
		val local = local.get()
		local.iov_base = data
		local.iov_len = bytesToWrite
		
		val remote = remote.get()
		remote.iov_base = address
		remote.iov_len = bytesToWrite
		
		return bytesToWrite.toLong() == uio.process_vm_writev(id, local, 1, remote, 1, 0)
	}
	
}