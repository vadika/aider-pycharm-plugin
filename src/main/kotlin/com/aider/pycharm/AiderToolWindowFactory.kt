package com.aider.pycharm

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.*
import java.awt.BorderLayout
import java.awt.Font
import java.awt.Insets
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.text.DefaultCaret

class AiderToolWindowFactory : ToolWindowFactory {
    private lateinit var aiderService: AiderService
    private lateinit var outputArea: JTextArea
    private lateinit var inputField: JTextField
    
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        aiderService = AiderService(project)
        
        val panel = JPanel(BorderLayout())
        
        // Output area
        outputArea = JTextArea().apply {
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
        inputField = JTextField().apply {
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
        
        panel.add(JScrollPane(outputArea), BorderLayout.CENTER)
        panel.add(inputField, BorderLayout.SOUTH)
        
        val content = ContentFactory.getInstance().createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
        
        // Start aider service
        startAiderService()
    }
    
    private fun startAiderService() {
        try {
            aiderService.start()
            
            // Start reading output in a separate thread
            Thread {
                while (true) {
                    val output = aiderService.readOutput()
                    if (output != null) {
                        SwingUtilities.invokeLater {
                            outputArea.append(output + "\n")
                        }
                    }
                }
            }.start()
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
