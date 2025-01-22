package com.aider.pycharm

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.terminal.JBTerminalWidget
import com.intellij.openapi.wm.impl.TerminalView
import com.intellij.terminal.JBTerminalSystemSettingsProviderBase
import com.intellij.terminal.JBTerminalWidget
import com.intellij.terminal.TerminalExecutionConsole
import java.io.IOException

class AiderService(private val project: Project) {
    private var terminal: ShellTerminalWidget? = null

    fun start() {
        try {
            // Get the Terminal tool window
            val terminalView = TerminalView.getInstance(project)
            val toolWindowManager = ToolWindowManager.getInstance(project)
            val terminalToolWindow = toolWindowManager.getToolWindow("Terminal")
                ?: throw IOException("Terminal tool window not found")

            // Ensure terminal window is visible
            terminalToolWindow.show()

            // Create new terminal tab
            val terminalWidget = terminalView.createLocalShellWidget(project.basePath, "Aider")
            terminal = terminalWidget as? ShellTerminalWidget
                ?: throw IOException("Failed to create terminal widget")

            // Run aider in the terminal
            terminal?.executeCommand("aider")
        } catch (e: Exception) {
            throw IOException("Failed to start aider: ${e.message}")
        }
    }

    fun sendMessage(message: String) {
        terminal?.executeCommand(message)
    }

    fun stop() {
        terminal?.executeCommand("exit")
        terminal = null
    }
}
