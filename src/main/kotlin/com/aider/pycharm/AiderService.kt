package com.aider.pycharm

import com.intellij.openapi.project.Project
import com.pty4j.PtyProcess
import com.pty4j.PtyProcessBuilder
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

class AiderService(private val project: Project) {
    private var process: Process? = null
    private var inputWriter: PrintWriter? = null
    private var outputReader: BufferedReader? = null
    
    fun start() {
        try {
            // Configure JNA
            System.setProperty("jna.nosys", "true")
            System.setProperty("jna.nounpack", "true")
            System.setProperty("jna.debug_load", "true")
            System.setProperty("jna.debug_load.jna", "true")
            
            // Force JNA initialization
            try {
                val native = Class.forName("com.sun.jna.Native")
                val method = native.getDeclaredMethod("load", String::class.java)
                method.isAccessible = true
                method.invoke(null, "c")
            } catch (e: Exception) {
                throw IOException("Failed to initialize JNA: ${e.message}", e)
            }
            
            // Try to find aider in common locations
            val aiderPath = findAiderPath() ?: throw IOException("Could not find aider executable. Please ensure it is installed and in your PATH")
            
            val processBuilder = PtyProcessBuilder()
                .setCommand(arrayOf(aiderPath))
                .setDirectory(project.basePath)
                .setConsole(true)
                .setRedirectErrorStream(true)
            
            process = processBuilder.start()
            inputWriter = PrintWriter(process!!.outputStream, true)
            outputReader = BufferedReader(InputStreamReader(process!!.inputStream))
        } catch (e: Exception) {
            throw IOException("Failed to start aider: ${e.message}")
        }
    }

    private fun findAiderPath(): String? {
        // Common locations for aider
        val paths = listOf(
            "/usr/local/bin/aider",
            "/opt/homebrew/bin/aider",
            System.getProperty("user.home") + "/.local/bin/aider"
        )
        
        // First check if aider is in PATH
        val process = Runtime.getRuntime().exec(arrayOf("which", "aider"))
        val result = process.waitFor()
        if (result == 0) {
            return "aider"
        }
        
        // Then check common locations
        for (path in paths) {
            if (java.io.File(path).exists()) {
                return path
            }
        }
        
        return null
    }
    
    fun sendMessage(message: String) {
        inputWriter?.println(message)
    }
    
    fun readOutput(): String? {
        return outputReader?.readLine()
    }
    
    fun stop() {
        inputWriter?.close()
        outputReader?.close()
        process?.destroy()
    }
}
