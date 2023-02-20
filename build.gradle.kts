import com.github.jengelman.gradle.plugins.shadow.ShadowApplicationPlugin.SHADOW_INSTALL_TASK_NAME
import com.github.jengelman.gradle.plugins.shadow.ShadowApplicationPlugin.SHADOW_SCRIPTS_TASK_NAME
import org.jetbrains.kotlin.incremental.deleteRecursivelyOrThrow

plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow")
}

group = "com.charlatano"
version = "1.3.0"

dependencies {
    val slf4jVersion = "2.0.6"
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:slf4j-nop:$slf4jVersion")

    val jnaVersion = "5.13.0"
    implementation("net.java.dev.jna:jna:$jnaVersion")
    implementation("net.java.dev.jna:jna-platform:$jnaVersion")

    val gdxVersion = "1.11.0"
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl:$gdxVersion")

    implementation("it.unimi.dsi:fastutil:8.5.11")
    implementation("net.openhft:chronicle-core:2.24ea6")

    implementation("org.jire:kna:0.4.2")

    implementation(kotlin("script-runtime"))
    implementation(kotlin("main-kts"))
    implementation(kotlin("scripting-jsr223"))
}

application {
    mainClass.set("com.charlatano.Charlatano")
    applicationDefaultJvmArgs += arrayOf(
        "-Xmx1024m",
        "-Xms512m",

        "--enable-preview",
        "--enable-native-access=ALL-UNNAMED",

        "--add-opens=java.base/java.lang=ALL-UNNAMED"
    )
}

kotlin {
    jvmToolchain(8)
}

tasks {
    compileKotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xinline-classes")
            freeCompilerArgs.add("-Xjvm-default=all")
            freeCompilerArgs.add("-Xextended-compiler-checks")
        }
    }
    compileJava {
        options.compilerArgs.add("--enable-preview")
    }
    configureShadowJar()
}

sourceSets {
    main.configure {
        kotlin {
            srcDir("settings")
        }
    }
}

fun TaskContainerScope.configureShadowJar() {
    shadowJar {
        archiveBaseName.set("Charlatano")
        archiveClassifier.set("")
        //archiveVersion.set("${project.version}")

        isZip64 = true
        //minimize() // needs to be updated for Java 19 support
    }
    named<Zip>("distZip").configure {
        enabled = false
    }
    named<Tar>("distTar").configure {
        enabled = false
    }
    named<CreateStartScripts>("startScripts").configure {
        enabled = false
    }
    named<CreateStartScripts>(SHADOW_SCRIPTS_TASK_NAME).configure {
        enabled = false
    }
    named(SHADOW_INSTALL_TASK_NAME).configure {
        enabled = false
    }
    named("shadowDistTar").configure {
        enabled = false
    }
    named("shadowDistZip").configure {
        enabled = false
    }
}

fun TaskContainerScope.configureCharlatano() {
    register("charlatano") {
        dependsOn(shadowJar)
        doLast {
            val version = version
            val name = "Charlatano $version"

            val buildDir = file("build/")

            val dir = buildDir.resolve(name)
            if (dir.exists()) dir.deleteRecursivelyOrThrow()
            dir.mkdirs()

            val jarName = "${name}.jar"
            val jar = dir.resolve(jarName)
            val allJar = buildDir.resolve("libs/Charlatano-${version}.jar")
            allJar.copyTo(jar, true)

            dir.writeStartBat(name, jarName)

            fun File.copyFromRoot(path: String) = file(path).copyTo(resolve(path), true)

            dir.copyFromRoot("LICENSE.txt")
            dir.copyFromRoot("README.md")

            val dirSettings = dir.resolve("settings")
            dirSettings.listFiles()!!.forEach {
                dirSettings.copyFromRoot(it.name)
            }
        }
    }
}

fun File.writeStartBat(name: String, jarName: String) =
    resolve("Start ${name}.bat")
        .writeText(
            """@echo off
cd /d "%~dp0"
title $name
java ${application.applicationDefaultJvmArgs.joinToString(" ")} -jar "$jarName"
pause"""
        )
