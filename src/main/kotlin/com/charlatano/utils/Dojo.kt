/*
 * Charlatano: Free and open-source (FOSS) cheat for CS:GO/CS:CO
 * Copyright (C) 2017 - Thomas G. P. Nappo, Jonathan Beaudoin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.charlatano.utils

import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
import javax.script.ScriptEngine
import javax.script.ScriptEngineFactory
import javax.script.ScriptEngineManager
import kotlin.reflect.KProperty

object Dojo {
	
	val factory = KotlinJsr223JvmLocalScriptEngineFactory()
	val engine: ScriptEngine = factory.scriptEngine
	
	fun script(script: String): Any? = engine.eval(script)
	
	operator inline fun <reified T> invoke(function: String) = engine.eval("$function()") as T
	
	operator fun invoke(function: String): Unit = invoke<Unit>(function)
	
	@Suppress("UNCHECKED_CAST")
	operator fun <T> get(name: String): T = engine.eval(name) as T
	
	operator fun <T> set(name: String, value: T) {
		engine.eval("$name = $value")
	}
	
	class Setting<out T> {
		private var value: T? = null
		
		operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
			if (value == null) value = get<T>(property.name)
			return value!!
		}
	}
	
}