package com.aider.pycharm

import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

class AiderService(private val project: Project) {
    private var process: Process? = null
    private var inputWriter: PrintWriter? = null
    private var outputReader: BufferedReader? = null
    
    fun start() {
        val processBuilder = ProcessBuilder("aider")
        processBuilder.directory(project.basePath?.let { java.io.File(it) })
        processBuilder.redirectErrorStream(true)
        
        process = processBuilder.start()
        inputWriter = PrintWriter(process!!.outputStream, true)
        outputReader = BufferedReader(InputStreamReader(process!!.inputStream))
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
