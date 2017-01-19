package com.charlatano.game.netvars

import com.charlatano.game.offsets.ClientOffsets.dwFirstClass
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap

object NetVars {
	
	internal val map = Int2ObjectArrayMap<ClassOffset>(20_000) // Cover us for a while with 20K
	
	private fun scanTable(netVars: MutableMap<Int, ClassOffset>, table: ClassTable, offset: Long, name: String) {
		for (i in 0..table.propCount - 1) {
			val prop = ClassVariable(table.propForID(i), offset)
			if (Character.isDigit(prop.name[0])) continue
			
			if (!prop.name.contains("baseclass")) {
				val netVar = ClassOffset(name, prop.name, prop.offset)
				netVars.put(hashClassAndVar(netVar.className, netVar.variableName), netVar)
			}
			
			val child = prop.table
			if (0L != child) scanTable(netVars, ClassTable(child), prop.offset, name)
		}
	}
	
	fun load() {
		map.clear() // for reloads
		
		var clientClass = Class(dwFirstClass)
		while (clientClass.readable()) {
			val table = ClassTable(clientClass.table)
			if (table.readable()) scanTable(map, table, 0, table.name)
			clientClass = Class(clientClass.next)
		}
	}
	
	internal fun hashClassAndVar(className: String, varName: String) = className.hashCode() xor varName.hashCode()
	
}