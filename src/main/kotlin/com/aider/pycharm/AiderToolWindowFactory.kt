package com.aider.pycharm

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import java.awt.BorderLayout
import java.awt.Font
import java.awt.Insets
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JPanel
import javax.swing.text.DefaultCaret

class AiderToolWindowFactory : ToolWindowFactory {
    private lateinit var aiderService: AiderService
    private lateinit var outputArea: JTextArea
    private lateinit var inputField: JTextField
    
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        aiderService = AiderService(project)
        
        val panel = JPanel(BorderLayout())
        
        // Output area
        outputArea = JBTextArea().apply {
            isEditable = false
            lineWrap = true
            wrapStyleWord = true
            rows = 20  // Set initial height
            columns = 50  // Set initial width
            font = Font("Monospaced", Font.PLAIN, 12)
            margin = Insets(5, 5, 5, 5)  // Add padding around text
        }
        (outputArea.caret as DefaultCaret).updatePolicy = DefaultCaret.ALWAYS_UPDATE
        
        // Input field
        inputField = JBTextField().apply {
            addKeyListener(object : KeyListener {
                override fun keyTyped(e: KeyEvent) {}
                override fun keyPressed(e: KeyEvent) {
                    if (e.keyCode == KeyEvent.VK_ENTER) {
                        val message = text
                        if (message.isNotEmpty()) {
                            sendMessage(message)
                            text = ""
                        }
                    }
                }
                override fun keyReleased(e: KeyEvent) {}
            })
        }
        
        panel.add(JBScrollPane(outputArea), BorderLayout.CENTER)
        panel.add(inputField, BorderLayout.SOUTH)
        
        val content = ContentFactory.getInstance().createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
        
        // Start aider service
        startAiderService()
    }
    
    private fun startAiderService() {
        try {
            aiderService.start()
            outputArea.append("Aider started in Terminal window.\n")
            outputArea.append("Please switch to the Terminal window to interact with Aider.\n")
        } catch (e: Exception) {
            outputArea.append("Error: ${e.message}\n")
            outputArea.append("Please ensure aider is installed. You can install it with:\n")
            outputArea.append("pip install aider-chat\n")
        }
    }
    
    private fun sendMessage(message: String) {
        outputArea.append("> $message\n")
        aiderService.sendMessage(message)
    }
}
